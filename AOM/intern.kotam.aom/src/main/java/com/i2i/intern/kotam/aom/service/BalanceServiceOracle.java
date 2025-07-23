/*
package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.model.Balance;
import com.i2i.intern.kotam.aom.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


public class BalanceServiceOracle {

    private final BalanceRepository balanceRepository;


    public BalanceServiceOracle(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public Optional<Balance> getBalanceByMsisdn(String msisdn) {
        return balanceRepository.getBalanceByMsisdn(msisdn);
    }

    public boolean insertBalance(String msisdn, Long package_id , Long left_minutes , Long left_sms , Long left_data ) {
        return balanceRepository.insertBalance( msisdn, package_id , left_minutes , left_sms , left_data );
    }

    public boolean updateBalance(String msisdn, Balance updatedBalance) {
        return balanceRepository.updateBalance(msisdn, updatedBalance);
    }

    public void updateDataBalance(String msisdn, Long data) {
        balanceRepository.updateDataBalance(msisdn, data);
    }

    public void updateSmsBalance(String msisdn, Long sms) {
        balanceRepository.updateSmsBalance(msisdn, sms);
    }

    public void updateMinutesBalance(String msisdn, Long minutes) {
        balanceRepository.updateMinutesBalance(msisdn, minutes);
    }
}

 */
