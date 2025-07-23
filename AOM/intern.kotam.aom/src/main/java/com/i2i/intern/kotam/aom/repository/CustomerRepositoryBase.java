package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.model.Customer;

import java.util.Optional;

public interface CustomerRepositoryBase {
    boolean insertCustomer( String msisdn,String name,String surname, String email, String password,String nationalid);
    Optional<Customer> authenticateCustomer(String email, String password);
    boolean checkCustomerExistsByMailAndNationalId(String email, String nationalId);
    Optional<Customer> getCustomerByMsisdn(String msisdn);
    boolean resetPassword(String email, String nationalId, String newPassword);
    boolean forgotPassword(String email, String nationalId);
    boolean changePassword(String email, String nationalId, String newPassword);

    // VoltDB implementasyonunda ek genişleme için interface olarak tanımlı
}
