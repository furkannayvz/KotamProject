package com.example.demo;

import com.example.demo.service.VoltDBRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChfServiceApplication implements CommandLineRunner {

	@Autowired
	private VoltDBRestService voltDBRestService;

	public static void main(String[] args) {
		SpringApplication.run(ChfServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("\n=== Testing VoltDB Connection ===");
		
		try {
			System.out.println("Connection Info: " + voltDBRestService.getConnectionInfo());
			
			boolean isConnected = voltDBRestService.testConnection();
			System.out.println("Connection Status: " + (isConnected ? " SUCCESS" : "FAILED"));
			
			if (isConnected) {
				System.out.println("\n--- Getting Max MSISDN ---");
				String maxMsisdn = voltDBRestService.getMaxMsisdn().block();
				System.out.println("Max MSISDN: " + maxMsisdn);
				
				System.out.println("\n--- Getting Max Balance ID ---");
				String maxBalanceId = voltDBRestService.getMaxBalanceId().block();
				System.out.println("Max Balance ID: " + maxBalanceId);
			}
			
		} catch (Exception e) {
			System.err.println("VoltDB test failed: " + e.getMessage());
		}
		
		System.out.println("=== VoltDB Test Complete ===\n");
	}
}
