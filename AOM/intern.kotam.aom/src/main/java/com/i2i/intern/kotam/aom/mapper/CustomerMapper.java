package com.i2i.intern.kotam.aom.mapper;

import com.i2i.intern.kotam.aom.dto.CustomerDto;
import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.service.CustomerService;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDto customerToCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .customerId(customer.getCustomerId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .email(customer.getEmail())
                .msisdn(customer.getMsisdn())
                .sDate(customer.getSDate())
                .TCNumber(customer.getTCNumber())
                .build();
    }

    public CustomerDto voltCustomerBalanceToCustomerDto(CustomerService.VoltCustomer voltCustomer) {
        return CustomerDto.builder()
                .customerId(null) // VoltCustomer'da ID yok
                .name(voltCustomer.getName())
                .surname(voltCustomer.getSurname())
                .email(voltCustomer.getEmail())
                .msisdn(voltCustomer.getMsisdn())
                .sDate(null) // VoltCustomer'da tarih yok
                .TCNumber(voltCustomer.getTcNumber())
                .build();
    }
}