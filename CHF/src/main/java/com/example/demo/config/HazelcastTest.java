package com.example.demo.config;

import java.util.Collection;
import org.i2i.kotam.HazelcastSimulatorOperation;

public class HazelcastTest {
    public static void main(String[] args) {
        try {
            System.out.println("Fetching MSISDNs from Hazelcast...");
            Collection<Object> msisdnList = HazelcastSimulatorOperation.getAllMsisdn();

            System.out.println("Retrieved " + msisdnList.size() + " MSISDN(s):");
            for (Object obj : msisdnList) {
                String msisdn = (String) obj;
                System.out.println("MSISDN: " + msisdn);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch MSISDNs: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
