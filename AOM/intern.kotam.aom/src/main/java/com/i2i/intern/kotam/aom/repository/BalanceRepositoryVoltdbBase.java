package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.model.Balance;

import java.util.Optional;

public interface BalanceRepositoryVoltdbBase {

    Optional<Balance> getBalanceByMsisdn(String msisdn);

    boolean insertBalance(Balance balance);

    boolean updateMinutesByMsisdn(String msisdn, Long minutes);

    boolean updateSmsByMsisdn(String msisdn, Long sms);

    boolean updateDataByMsisdn(String msisdn, Long data);

    Optional<Long> getMaxBalanceId();
}
