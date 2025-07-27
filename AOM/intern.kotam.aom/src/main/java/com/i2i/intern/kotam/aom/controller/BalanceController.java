package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.dto.BalanceDTO;
import com.i2i.intern.kotam.aom.dto.PackageDTO;
import com.i2i.intern.kotam.aom.dto.request.BalanceRequestDTO;
import com.i2i.intern.kotam.aom.model.Balance;
import com.i2i.intern.kotam.aom.model.PackageEntity;
import com.i2i.intern.kotam.aom.service.BalanceServiceOracle;
import com.i2i.intern.kotam.aom.service.BalanceServiceVoltdb;
import com.i2i.intern.kotam.aom.service.PackageServiceOracle;
import com.i2i.intern.kotam.aom.service.HazelcastService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api/balances")
@Tag(name = "Bakiye İşlemleri", description = "Bakiye ekleme ve kontrol işlemleri")
public class BalanceController {

    private final PackageServiceOracle packageServiceOracle;
    private final BalanceServiceVoltdb balanceServiceVoltdb;
    private final HazelcastService hazelcastService;
    private final BalanceServiceOracle balanceServiceOracle;

    public BalanceController(PackageServiceOracle packageServiceOracle,
                             BalanceServiceVoltdb balanceServiceVoltdb,
                             HazelcastService hazelcastService,
                             BalanceServiceOracle balanceServiceOracle) {
        this.packageServiceOracle = packageServiceOracle;
        this.balanceServiceVoltdb = balanceServiceVoltdb;
        this.hazelcastService = hazelcastService;
        this.balanceServiceOracle = balanceServiceOracle;
    }

    // GET /api/balances/{msisdn}
    @GetMapping("/{msisdn}")
    public ResponseEntity<BalanceDTO> getBalanceByMsisdn(@PathVariable String msisdn) {
        Optional<Balance> optionalBalance = balanceServiceVoltdb.getBalanceByMsisdn(msisdn);

        if (optionalBalance.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        Balance balance = optionalBalance.get();

        // 👉 VoltDB’den gelen balance nesnesinde sadece packageId var
        // Oracle’dan o packageId ile PackageEntity’yi çekelim
        Optional<PackageEntity> optionalPkg = packageServiceOracle.getPackageDetailsById(balance.getPackageId());
        if (optionalPkg.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // paket bulunamazsa hata
        }

        PackageEntity pkg = optionalPkg.get();

        // PackageDTO oluştur
        PackageDTO pkgDto = new PackageDTO();
        pkgDto.setId(pkg.getId());
        pkgDto.setDataQuota(pkg.getDataQuota() != null ? pkg.getDataQuota().intValue() : 0);
        pkgDto.setSmsQuota(pkg.getSmsQuota() != null ? pkg.getSmsQuota().intValue() : 0);
        pkgDto.setMinutesQuota(pkg.getMinutesQuota() != null ? pkg.getMinutesQuota().intValue() : 0);
        pkgDto.setPrice(pkg.getPrice() != null ? pkg.getPrice() : 0.0);
        pkgDto.setPeriod(pkg.getPeriod() != null ? pkg.getPeriod() : 0);
        pkgDto.setPackageName(pkg.getPackageName());

        // BalanceDTO oluştur
        BalanceDTO dto = new BalanceDTO();
        dto.setBalanceId(balance.getBalanceId());
        dto.setMsisdn(balance.getMsisdn());
        dto.setLeftData(balance.getLeftData());
        dto.setLeftSms(balance.getLeftSms());
        dto.setLeftMinutes(balance.getLeftMinutes());
        dto.setGetsDate(balance.getsDate());
        dto.setPackageEntity(pkgDto); // ✅ Artık doğru dolu

        return ResponseEntity.ok(dto);
    }



    // POST /api/balances
    @PostMapping("/balances")
    public ResponseEntity<String> insertBalance(@RequestBody BalanceRequestDTO dto) {
        try {
            System.out.println("Gelen DTO:\n" + dto);

            // 1. Paket bilgilerini al (Oracle'dan)
            Optional<PackageEntity> optionalPackage = packageServiceOracle.getPackageDetailsById(dto.getPackageId());
            if (optionalPackage.isEmpty()) {
                return ResponseEntity.badRequest().body("Paket bulunamadı.");
            }

            PackageEntity selectedPackage = optionalPackage.get();

            // 2. Balance ID üret
            Long newBalanceId = balanceServiceVoltdb.getMaxBalanceId().orElse(100L) + 1;

            // 3. Balance nesnesi oluştur
            Balance balance = new Balance();
            balance.setBalanceId(newBalanceId);
            balance.setMsisdn(dto.getMsisdn());
            balance.setLeftData(selectedPackage.getDataQuota());
            balance.setLeftSms(selectedPackage.getSmsQuota());
            balance.setLeftMinutes(selectedPackage.getMinutesQuota());
            balance.setsDate(Timestamp.from(Instant.now()));
            balance.setPackageEntity(selectedPackage); // package bilgilerini ilişkilendir

            System.out.println("Oluşturulan Balance Entity:\n" + balance);

            // VoltDB’ye yaz
            boolean successVolt = balanceServiceVoltdb.insertBalance(balance);
            if (!successVolt) {
                return ResponseEntity.badRequest().body("Bakiye VoltDB'ye eklenemedi.");
            }

            // Oracle’a da yaz
            boolean successOracle = balanceServiceOracle.insertCustomerPackage(
                    balance.getMsisdn(),
                    balance.getPackageEntity().getId()
            );
            if (!successOracle) {
                return ResponseEntity.badRequest().body("Bakiye Oracle’a eklenemedi.");
            }

            // Hazelcast’e gönder
            String hazelcastResult = hazelcastService.sendMsisdnToHazelcast(dto.getMsisdn(), "active");
            System.out.println("Hazelcast sonucu: " + hazelcastResult);

            return ResponseEntity.ok("Bakiye hem VoltDB hem Oracle’a başarıyla eklendi ve MSISDN aktif olarak işaretlendi.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        }
    }



    //  GET /api/balances/max-id
    @GetMapping("/max-id")
    public ResponseEntity<Long> getMaxBalanceId() {
        Optional<Long> maxId = balanceServiceVoltdb.getMaxBalanceId();
        return maxId.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
