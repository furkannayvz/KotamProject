package com.i2i.intern.kotam.aom.dto.response;

import com.i2i.intern.kotam.aom.dto.CustomerDTO;

public class LoginResponseDTO {
    private boolean success;
    private String message;

    // Gerekirse kullanıcı bilgileri de döndürülebilir
    private CustomerDTO customer;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }
}
