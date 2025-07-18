package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.dto.CustomerDto;
import com.i2i.intern.kotam.aom.mapper.CustomerMapper;
import com.i2i.intern.kotam.aom.repository.CustomerRepository;
import com.i2i.intern.kotam.aom.helper.VoltdbOperator;
import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.model.User;
import com.i2i.intern.kotam.aom.request.RegisterCustomerRequest;
import com.i2i.intern.kotam.aom.response.AuthenticationResponse;
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

    /**
     * MSISDN ile müşteri bilgilerini getir
     * @param msisdn Müşteri telefon numarası
     * @return CustomerDto
     * @throws IOException
     * @throws InterruptedException
     * @throws ProcCallException
     */
    public CustomerDto retrieveCustomerByMsisdn(String msisdn)
            throws IOException, InterruptedException, ProcCallException {

        logger.info("Retrieving customer by MSISDN: {}", msisdn);

        try {
            // VoltDB'den müşteri bilgilerini al
            Optional<VoltCustomer> voltCustomerOpt = voltdbOperator.getCustomerByMsisdn(msisdn);

            if (voltCustomerOpt.isEmpty()) {
                logger.warn("Customer not found for MSISDN: {}", msisdn);
                throw new RuntimeException("Customer not found for MSISDN: " + msisdn);
            }

            VoltCustomer voltCustomer = voltCustomerOpt.get();
            CustomerDto customerDto = transformVoltCustomerToDto(voltCustomer);

            logger.info("Customer retrieved successfully for MSISDN: {}", msisdn);
            return customerDto;

        } catch (Exception e) {
            logger.error("Error retrieving customer for MSISDN: {}", msisdn, e);
            throw new RuntimeException("Failed to retrieve customer: " + e.getMessage(), e);
        }
    }

    /**
     * Tüm müşterileri getir
     * @return List<CustomerDto>
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<CustomerDto> fetchAllCustomers() throws SQLException, ClassNotFoundException {
        logger.info("Fetching all customers from database");

        try {
            List<Customer> customers = customerRepository.getAllCustomers();

            if (customers.isEmpty()) {
                logger.warn("No customers found in database");
                return List.of();
            }

            List<CustomerDto> customerDtos = convertCustomersToDto(customers);

            logger.info("Successfully fetched {} customers", customerDtos.size());
            return customerDtos;

        } catch (Exception e) {
            logger.error("Error fetching all customers", e);
            throw new RuntimeException("Failed to fetch customers: " + e.getMessage(), e);
        }
    }

    /**
     * Oracle'da müşteri kaydı oluştur
     * @param request Register request
     * @return AuthenticationResponse
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public AuthenticationResponse processCustomerRegistration(RegisterCustomerRequest request)
            throws SQLException, ClassNotFoundException {

        logger.info("Processing customer registration for MSISDN: {}", request.msisdn());

        try {
            AuthenticationResponse response = customerRepository.createCustomerInOracle(request);

            if (response == null) {
                logger.error("Registration failed for MSISDN: {}", request.msisdn());
                throw new RuntimeException("Registration failed for MSISDN: " + request.msisdn());
            }

            logger.info("Customer registration completed successfully for MSISDN: {}", request.msisdn());
            return response;

        } catch (Exception e) {
            logger.error("Error processing customer registration for MSISDN: {}", request.msisdn(), e);
            throw new RuntimeException("Failed to process customer registration: " + e.getMessage(), e);
        }
    }

    /**
     * MSISDN ile kullanıcı ara
     * @param msisdn Telefon numarası
     * @return User
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public User searchUserByMsisdn(String msisdn) throws SQLException, ClassNotFoundException {
        logger.info("Searching user by MSISDN: {}", msisdn);

        try {
            Optional<User> userOpt = customerRepository.findByMsisdn(msisdn);

            if (userOpt.isEmpty()) {
                logger.warn("User not found for MSISDN: {}", msisdn);
                throw new RuntimeException("User not found for MSISDN: " + msisdn);
            }

            User user = userOpt.get();
            logger.info("User found successfully for MSISDN: {}", msisdn);
            return user;

        } catch (Exception e) {
            logger.error("Error searching user by MSISDN: {}", msisdn, e);
            throw new RuntimeException("Failed to search user: " + e.getMessage(), e);
        }
    }

    /**
     * Müşteri şifresini güncelle
     * @param email Email adresi
     * @param tcNumber TC kimlik numarası
     * @param newPassword Yeni şifre
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void updateCustomerPassword(String email, String tcNumber, String newPassword)
            throws SQLException, ClassNotFoundException {

        logger.info("Updating customer password for email: {}", email);

        try {
            // Oracle'da şifre güncelle
            customerRepository.updatePasswordInOracle(email, tcNumber, newPassword);

            // VoltDB'de şifre güncelle
            voltdbOperator.updatePassword(email, tcNumber, newPassword);

            logger.info("Password updated successfully for email: {}", email);

        } catch (Exception e) {
            logger.error("Error updating password for email: {}", email, e);
            throw new RuntimeException("Failed to update password: " + e.getMessage(), e);
        }
    }

    // Private helper methods
    private CustomerDto transformVoltCustomerToDto(VoltCustomer voltCustomer) {
        logger.debug("Transforming VoltCustomer to CustomerDto");

        return customerMapper.voltCustomerBalanceToCustomerDto(voltCustomer);
    }

    private List<CustomerDto> convertCustomersToDto(List<Customer> customers) {
        logger.debug("Converting {} customers to DTOs", customers.size());

        return customers.stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    // VoltCustomer inner class (VoltDB modülü hazır olana kadar)
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

        // Getters
        public String getMsisdn() { return msisdn; }
        public String getName() { return name; }
        public String getSurname() { return surname; }
        public String getEmail() { return email; }
        public String getTcNumber() { return tcNumber; }
    }
}