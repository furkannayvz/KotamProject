/*
package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.dto.response.CustomerLoginResponseDTO;
import com.i2i.intern.kotam.aom.model.Balance;
import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.dto.response.LoginResponseDTO;

import com.i2i.intern.kotam.aom.model.PackageEntity;
import com.i2i.intern.kotam.aom.repository.BalanceRepositoryVoltdbBase;
import com.i2i.intern.kotam.aom.repository.CustomerRepositoryBase;
import com.i2i.intern.kotam.aom.repository.PackageRepositoryOracle;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Optional;

@Service
public class CustomerServiceOracle {

    private final CustomerRepositoryBase customerRepository;
    private final BalanceRepositoryVoltdbBase balanceRepositoryVoltdb;
    private final PackageRepositoryOracle packageRepository;
    private final DataSource dataSource;





    public CustomerServiceOracle(CustomerRepositoryBase customerRepository,
                                 BalanceRepositoryVoltdbBase balanceRepositoryVoltdb,
                                 PackageRepositoryOracle packageRepository,
                                 DataSource dataSource) {
        this.customerRepository = customerRepository;
        this.balanceRepositoryVoltdb = balanceRepositoryVoltdb;
        this.packageRepository = packageRepository;
        this.dataSource = dataSource;
    }

    public boolean insertCustomer(String msisdn, String name, String surname, String email, String password, String nationalid) {
        return customerRepository.insertCustomer(msisdn, name, surname, email, password, nationalid);
    }

    /*
    public Optional<Customer> authenticateCustomer(String msisdn, String password) {
        Optional<Customer> customerOpt = customerRepository.authenticateCustomer(msisdn, password);

        if (customerOpt.isPresent()) {
            // MSISDN'e göre Balance al
            Optional<Balance> balanceOpt = balanceRepositoryVoltdb.getBalanceByMsisdn(msisdn);
            if (balanceOpt.isPresent()) {
                Long packageId = balanceOpt.get().getPackageId();

                // Package bilgisi varsa ekle
                packageRepository.getPackageDetailsById(packageId)
                        .ifPresent(customerOpt.get()::setPackageEntity);
            }
        }

        return customerOpt;
    }*/

/*
    public LoginResponseDTO authenticateCustomer(String msisdn, String password) {
        Optional<Customer> customerOpt = customerRepository.authenticateCustomer(msisdn, password);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            return LoginResponseDTO.builder()
                    .msisdn(customer.getMsisdn())
                    .name(customer.getName())
                    .surname(customer.getSurname())
                    .email(customer.getEmail())
                    .nationalId(customer.getNationalId())
                    .sDate(customer.getsDate())
                    .packageEntity(customer.getPackageEntity())
                    .build();
        }

        return null;
    }


    public boolean checkCustomerExistsByMailAndNationalId(String email, String nationalId) {
        return customerRepository.checkCustomerExistsByMailAndNationalId(email, nationalId);
    }

    public Optional<Customer> getCustomerByMsisdn(String msisdn) {
        return customerRepository.getCustomerByMsisdn(msisdn);
    }

    public boolean resetPassword(String email, String nationalId, String newPassword) {
        return customerRepository.resetPassword(email, nationalId, newPassword);
    }

    public boolean forgotPassword(String email, String nationalId) {
        return customerRepository.forgotPassword(email, nationalId);
    }

    public boolean changePassword(String email, String nationalId, String newPassword) {
        return customerRepository.changePassword(email, nationalId, newPassword);
    }
}

 */

package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.dto.response.LoginResponseDTO;
import com.i2i.intern.kotam.aom.model.Balance;
import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.model.PackageEntity;
import com.i2i.intern.kotam.aom.repository.BalanceRepositoryVoltdbBase;
import com.i2i.intern.kotam.aom.repository.CustomerRepositoryOracle;
import com.i2i.intern.kotam.aom.repository.PackageRepositoryOracle;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Optional;

@Service
public class CustomerServiceOracle {

    private final CustomerRepositoryOracle customerRepository;
    private final BalanceRepositoryVoltdbBase balanceRepositoryVoltdb;
    private final PackageRepositoryOracle packageRepository;
    private final BalanceServiceVoltdb balanceServiceVoltdb;
    private final DataSource dataSource;

    public CustomerServiceOracle(CustomerRepositoryOracle customerRepository,
                                 BalanceRepositoryVoltdbBase balanceRepositoryVoltdb,
                                 PackageRepositoryOracle packageRepository,
                                 BalanceServiceVoltdb balanceServiceVoltdb,
                                 DataSource dataSource) {
        this.customerRepository = customerRepository;
        this.balanceRepositoryVoltdb = balanceRepositoryVoltdb;
        this.packageRepository = packageRepository;
        this.balanceServiceVoltdb = balanceServiceVoltdb;
        this.dataSource = dataSource;
    }

    public boolean insertCustomer(String msisdn, String name, String surname,
                                  String email, String password, String nationalId) {
        return customerRepository.insertCustomer(msisdn, name, surname, email, password, nationalId);
    }

    public LoginResponseDTO authenticateCustomer(String msisdn, String password) {
        Optional<Customer> optionalCustomer = customerRepository.loginWithMsisdnAndPassword(msisdn, password);
        if (optionalCustomer.isEmpty()) {
            return null;
        }

        Customer customer = optionalCustomer.get();

        Optional<Balance> optionalBalance = balanceServiceVoltdb.getBalanceByMsisdn(msisdn);
        Long packageId = optionalBalance.map(Balance::getPackageId).orElse(null);

        PackageEntity packageEntity = null;
        if (packageId != null) {
            packageEntity = packageRepository.getPackageDetailsById(packageId).orElse(null);
        }

        return new LoginResponseDTO(customer, packageEntity);
    }


    public boolean checkCustomerExistsByMailAndNationalId(String email, String nationalId) {
        return customerRepository.checkCustomerExistsByMailAndNationalId(email, nationalId);
    }

    public Optional<Customer> getCustomerByMsisdn(String msisdn) {
        return customerRepository.getCustomerByMsisdn(msisdn);
    }

    public boolean resetPassword(String email, String nationalId, String newPassword) {
        return customerRepository.resetPassword(email, nationalId, newPassword);
    }

    public boolean forgotPassword(String email, String nationalId) {
        return customerRepository.forgotPassword(email, nationalId);
    }

    public boolean changePassword(String email, String nationalId, String newPassword) {
        return customerRepository.changePassword(email, nationalId, newPassword);
    }
}


