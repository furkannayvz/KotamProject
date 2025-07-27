package org.sk.i2i.evren.TGF.dto;

public class ChargingResponse {
    private boolean success;
    private String message;
    private String msisdn;
    private String serviceType;

    public ChargingResponse() {
    }

    public ChargingResponse(boolean success, String message, String msisdn, String serviceType) {
        this.success = success;
        this.message = message;
        this.msisdn = msisdn;
        this.serviceType = serviceType;
    }

    // Getters and setters
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

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public String toString() {
        return "ChargingResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", serviceType='" + serviceType + '\'' +
                '}';
    }
}
