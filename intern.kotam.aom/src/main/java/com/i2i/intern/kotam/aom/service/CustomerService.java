package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.dto.CustomerDto;
import com.i2i.intern.kotam.aom.mapper.CustomerMapper;
import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.repository.CustomerRepository;
import com.i2i.intern.kotam.aom.helper.VoltdbOperator;
import com.i2i.intern.kotam.aom.request.RegisterCustomerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.voltdb.client.ProcCallException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final VoltdbOperator voltdbOperator;

    public CustomerService(CustomerRepository customerRepository,
                           CustomerMapper customerMapper,
                           VoltdbOperator voltdbOperator) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.voltdbOperator = voltdbOperator;
    }

    public CustomerDto retrieveCustomerByMsisdn(String msisdn)
            throws IOException, InterruptedException, ProcCallException {

        logger.info("Retrieving customer by MSISDN: {}", msisdn);

        Optional<VoltCustomer> voltCustomerOpt = voltdbOperator.getCustomerByMsisdn(msisdn);

        if (voltCustomerOpt.isEmpty()) {
            logger.warn("Customer not found for MSISDN: {}", msisdn);
            throw new RuntimeException("Customer not found for MSISDN: " + msisdn);
        }

        CustomerDto customerDto = transformVoltCustomerToDto(voltCustomerOpt.get());
        logger.info("Customer retrieved successfully for MSISDN: {}", msisdn);
        return customerDto;
    }

    public List<CustomerDto> fetchAllCustomers() throws SQLException, ClassNotFoundException {
        logger.info("Fetching all customers from database");

        List<Customer> customers = customerRepository.getAllCustomers();

        if (customers.isEmpty()) {
            logger.warn("No customers found in database");
            return List.of();
        }

        return convertCustomersToDto(customers);
    }

    public boolean authenticateCustomer(String email, String password)
            throws SQLException, ClassNotFoundException {
        return customerRepository.authenticateCustomer(email, password);
    }


    public String processCustomerRegistration(RegisterCustomerRequest request)
            throws SQLException, ClassNotFoundException, IOException, InterruptedException, ProcCallException {

        logger.info("Processing customer registration for MSISDN: {}", request.msisdn());

        customerRepository.createCustomerInOracle(request);

        logger.info("Customer registration completed successfully for MSISDN: {}", request.msisdn());
        return "Customer registered successfully";
    }

    public void updateCustomerPassword(String email, String tcNumber, String newPassword)
            throws SQLException, ClassNotFoundException, IOException, InterruptedException, ProcCallException {

        logger.info("Updating customer password for email: {}", email);

        customerRepository.updatePasswordInOracle(email, tcNumber, newPassword);
        customerRepository.updatePasswordInVoltDB(email, tcNumber, newPassword);

        logger.info("Password updated successfully for email: {}", email);
    }

    // Yardımcı dönüşüm metodları
    private CustomerDto transformVoltCustomerToDto(VoltCustomer voltCustomer) {
        return customerMapper.voltCustomerBalanceToCustomerDto(voltCustomer);
    }

    private List<CustomerDto> convertCustomersToDto(List<Customer> customers) {
        return customers.stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    // VoltCustomer inner class
    public static class VoltCustomer {
        private final String msisdn;
        private final String name;
        private final String surname;
        private final String email;
        private final String tcNumber;

        public VoltCustomer(String msisdn, String name, String surname, String email, String tcNumber) {
            this.msisdn = msisdn;
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.tcNumber = tcNumber;
        }

        public String getMsisdn() { return msisdn; }
        public String getName() { return name; }
        public String getSurname() { return surname; }
        public String getEmail() { return email; }
        public String getTcNumber() { return tcNumber; }
    }
}
