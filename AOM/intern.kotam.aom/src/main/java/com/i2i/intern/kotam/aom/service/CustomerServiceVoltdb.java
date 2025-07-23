/*

package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.repository.CustomerRepositoryVoltdbBase;
import org.springframework.stereotype.Service;

import java.util.Optional;


public class CustomerServiceVoltdb {

    private final CustomerRepositoryVoltdbBase customerRepository;

    public CustomerServiceVoltdb(CustomerRepositoryVoltdbBase customerRepository) {
        this.customerRepository = customerRepository;
    }

    public boolean insertCustomer(Customer customer) {
        return customerRepository.insertCustomer(customer);
    }

    public Optional<Customer> getCustomerByMsisdn(String msisdn) {
        return customerRepository.getCustomerByMsisdn(msisdn);
    }

    public boolean updatePassword(String email, String nationalId, String newPassword) {
        return customerRepository.updatePassword(email, nationalId, newPassword);
    }

    public boolean checkCustomerExists(String email, String nationalId) {
        return customerRepository.checkCustomerExists(email, nationalId);
    }

    public Optional<String> getPasswordByMsisdn(String msisdn) {
        return customerRepository.getPasswordByMsisdn(msisdn);
    }

    public Optional<String> getEmailByMsisdn(String msisdn) {
        return customerRepository.getEmailByMsisdn(msisdn);
    }

    public Optional<String> getMaxMsisdn() {
        return customerRepository.getMaxMsisdn();
    }
}


 */
