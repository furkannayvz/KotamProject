package com.i2i.intern.kotam.aom.configuration;

import com.i2i.intern.kotam.aom.helper.VoltDBManager;
import com.i2i.intern.kotam.aom.helper.VoltdbOperator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoltConfiguration {

    @Bean
    public VoltdbOperator voltdbOperator() {
        return new VoltdbOperator();
    }

    @Bean
    public VoltDBManager voltDBManager() {
        return new VoltDBManager();
    }
}