package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.model.Balance;
import com.i2i.intern.kotam.aom.repository.BalanceRepositoryOracle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BalanceServiceOracle {

    private final BalanceRepositoryOracle balanceRepository;


    public BalanceServiceOracle(BalanceRepositoryOracle balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public boolean insertCustomerPackage(String msisdn, Long package_id) {
        return balanceRepository.insertCustomerPackage( msisdn, package_id);
    }


}
