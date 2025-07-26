package org.sk.i2i.evren.TGF.util;

/*
import org.i2i.kotam.HazelcastSimulatorOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenerator {

    private static final Random rand = new Random();
    private static List allMsisdn = new ArrayList<>(HazelcastSimulatorOperation.getAllMsisdn());

    public static int randomLocation() {
        return 1 + rand.nextInt(9);
    }
    public static int randomDataUsage() {
        return 1 + rand.nextInt(49);
    }
    public static int randomRatingGroup() {
        return 1 + rand.nextInt(5);
    }
    public static int randomDuration() {
        return 5 + rand.nextInt(115);
    }
    public static String randomMsisdn() {

        if(allMsisdn.isEmpty()) {
            System.out.println("hazelcast is empty...");
            return "5000000000";
        }

        int randIndex = rand.nextInt(allMsisdn.size());
        return allMsisdn.get(randIndex).toString();
    }
    public static void fetchMsisdn() {

        allMsisdn = new ArrayList<>(HazelcastSimulatorOperation.getAllMsisdn());
        System.out.println("msisdn list was updated:  " + allMsisdn.size());

/*        System.out.println("fetchMsisdn: size " + allMsisdn.size());
        for (Object msisdn : allMsisdn) {
            System.out.println(msisdn);
        }//kapali burasi
    }
}
            */


import org.i2i.kotam.HazelcastSimulatorOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenerator {

    private static final Random rand = new Random();
    private static List<Object> allMsisdn = new ArrayList<>();

    // Lokasyon 1-9 arası
    public static int randomLocation() {
        return 1 + rand.nextInt(9);
    }

    // 1 ile 49 arasında rastgele veri kullanımı (MB)
    public static int randomDataUsage() {
        return 1 + rand.nextInt(49);
    }

    // Rating group 1-5 arası
    public static int randomRatingGroup() {
        return 1 + rand.nextInt(5);
    }

    // Süre: 5 ile 120 saniye arası
    public static int randomDuration() {
        return 5 + rand.nextInt(116); // 5 dahil, 120 hariç
    }

    // MSISDN listeden rastgele seçim
    public static String randomMsisdn() {
        if (allMsisdn.isEmpty()) {
            System.out.println("⚠ Hazelcast'ten MSISDN listesi çekilmemiş ya da boş.");
            return "5000000000";
        }

        int randIndex = rand.nextInt(allMsisdn.size());
        return allMsisdn.get(randIndex).toString();
    }

    // Hazelcast'ten MSISDN listesini çek
    public static void fetchMsisdn() {
        try {
            allMsisdn = new ArrayList<>(HazelcastSimulatorOperation.getAllMsisdn());
            System.out.println("MSISDN listesi Hazelcast'ten güncellendi. Toplam: " + allMsisdn.size());
        } catch (Exception e) {
            System.out.println("MSISDN fetch işlemi başarısız: " + e.getMessage());
            allMsisdn.clear();
        }
    }
}

