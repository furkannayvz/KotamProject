package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.repository.CustomerRepositoryBase;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceOracle {

    private final CustomerRepositoryBase customerRepository;

    public CustomerServiceOracle(CustomerRepositoryBase customerRepository) {
        this.customerRepository = customerRepository;
    }

    public boolean insertCustomer( String msisdn,String name,String surname, String email, String password,String nationalid) {
        return customerRepository.insertCustomer( msisdn, name, surname,  email,  password, nationalid);
    }

    public Optional<Customer> authenticateCustomer(String email, String password) {
        return customerRepository.authenticateCustomer(email, password);
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
