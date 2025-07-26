package com.i2i.intern.kotam.aom;

import java.io.IOException;
import com.i2i.intern.kotam.aom.configuration.DataSourceConfig;
import com.i2i.intern.kotam.aom.helper.VoltDbOperator;
import com.i2i.intern.kotam.aom.model.Balance;
import com.i2i.intern.kotam.aom.model.PackageEntity;

import com.i2i.intern.kotam.aom.repository.BalanceRepositoryVoltdb;
import com.i2i.intern.kotam.aom.repository.CustomerRepositoryOracle;
import com.i2i.intern.kotam.aom.repository.PackageRepository;
import com.i2i.intern.kotam.aom.repository.PackageRepositoryOracle;
import com.i2i.intern.kotam.aom.service.BalanceServiceVoltdb;
import com.i2i.intern.kotam.aom.service.HazelcastService;
//import com.i2i.intern.kotam.aom.service.PackageSelectionService;
import com.i2i.intern.kotam.aom.service.PackageServiceOracle;
import org.json.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException{
		SpringApplication.run(Application.class, args);

		//PackageEntity packageEntity = new PackageEntity(1L, "name", 100L , 100L , 100L , 50D , true,30);
		//BalanceRepository Test = new BalanceRepository(DataSourceConfig.customDataSource());
		//Test.insertBalance("5512345678",1L, 100L,100L,100L);

		//CustomerRepositoryOracle Test2 = new CustomerRepositoryOracle();
		//Test2.authenticateCustomer("5554443322","esma123");

		//CustomerRepositoryOracle Test3 = new CustomerRepositoryOracle();
		//Test3.insertCustomer("1236754545","berra","kk","berra@gmail.com","090","12121212112");


		//CustomerRepositoryOracle Test4 = new CustomerRepositoryOracle();
		//Test4.checkCustomerExistsByMailAndNationalId("berra@gmail.com","12121212112");

		//CustomerRepositoryOracle Test5 = new CustomerRepositoryOracle();
		//Test5.getCustomerByMsisdn("1236754545");

		//CustomerRepositoryOracle Test6 = new CustomerRepositoryOracle();
		//Test6.resetPassword("berra@gmail.com","12121212112","010");

		//CustomerRepositoryOracle Test7 = new CustomerRepositoryOracle();
		//Test7.changePassword("berra@gmail.com","12121212112","123");

		//CustomerRepositoryOracle Test8 = new CustomerRepositoryOracle();
		//Test8.resetPassword("berra@gmail.com","12121212112","120");

		//PackageRepository Test9 = new PackageRepository();
		//Test9.getAllPackages();

		//PackageRepository Test10 = new PackageRepository();
		//Test10.getPackageDetailsById(1L);

		//PackageRepository Test11 = new PackageRepository();
		//Test11.getPackageDetailsByIdCursor(1L);

		//PackageRepository Test12 = new PackageRepository();
		//Test12.getPackageDetailsByName("Genç Tarifesi");

		//PackageRepository Test13 = new PackageRepository();
		//Test13.getPackageIdByName("Genç Tarifesi");

		// ---------- Balance --------------
		//VoltDbOperator voltDbOperator = new VoltDbOperator();
		//voltDbOperator.updateSmsBalance("5487013311", 20);
		//voltDbOperator.updateMinutesBalance("5486754172", 75);
		//voltDbOperator.updateDataBalance("5486754172", 512);

		/*
		VoltDbOperator operator = new VoltDbOperator();
		JSONObject result = operator.getBalanceInfoByMsisdn("5387013311");

		if (result != null) {
			System.out.println("Balance Bilgisi:");
			System.out.println(result.toString(4)); // JSON çıktıyı güzelce formatlar
		}*/

		/*
		VoltDbOperator operator = new VoltDbOperator();
		operator.updateSmsBalance("5387013311", 99); // test için
		JSONObject result = operator.getBalanceInfoByMsisdn("5387013311");
		System.out.println(result.toString(4));
		 */

		/*
		VoltDbOperator operator = new VoltDbOperator();

		JSONObject balanceJson = new JSONObject();
		balanceJson.put("BALANCE_ID", 3);
		balanceJson.put("MSISDN", "5387013311");
		balanceJson.put("PACKAGE_ID", 2);
		balanceJson.put("BAL_LEFT_MINUTES", 100);
		balanceJson.put("BAL_LEFT_SMS", 200);
		balanceJson.put("BAL_LEFT_DATA", 500);
		balanceJson.put("SDATE", Instant.now().toString());

		operator.insertBalance(balanceJson);
		*/

	}

	/*

	@Bean
	public CommandLineRunner testVoltConnection(VoltDbOperator voltDbOperator) {
		return args -> {
			try {
				int maxId = voltDbOperator.getMaxBalanceId();
				System.out.println(" VoltDB bağlantısı başarılı! Max Balance ID: " + maxId);
			} catch (Exception e) {
				System.err.println(" VoltDB bağlantı hatası: " + e.getMessage());
			}
		};
	}*/
}

















/*
public PackageEntity(Long id, String name, Long dataQuota, Long smsQuota, Long minutesQuota, Double price, Boolean isActive, Integer period) {
        this.id = id;
        this.name = name;
        this.dataQuota = dataQuota;
        this.smsQuota = smsQuota;
        this.minutesQuota = minutesQuota;
        this.price = price;
        this.isActive = isActive;
        this.period = period;
    }
*/
