package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.model.Balance;
import java.util.Optional;

public interface BalanceRepositoryBase {
    boolean insertCustomerPackage(String msisdn, Long packageId);
}


