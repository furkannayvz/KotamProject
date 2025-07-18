package org.i2i.kotam;

public class HazelcastManager {

    public static void main(String[] args) {
        // TGF operasyonu: Hazelcast'e veri yaz
        HazelcastTGFOperation tgfOperation = new HazelcastTGFOperation();
        tgfOperation.writeSampleData();

        // AOM operasyonu: Hazelcast'ten verileri oku
        HazelcastMWOperation mwOperation = new HazelcastMWOperation();
        mwOperation.readAndPrintAllUsageData();
    }
}
