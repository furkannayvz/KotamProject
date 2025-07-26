package org.sk.i2i.evren.TGF.config;


import com.hazelcast.client.config.ClientConfig;

public class Configuration {
    public static ClientConfig getConfig() {
        ClientConfig config = new ClientConfig();

        // Hazelcast sunucusunun IP ve port bilgisi
        config.getNetworkConfig().addAddress("34.140.179.239:5701");

        // Cluster ismi (Hazelcast sunucusunda tanımlı olan isimle birebir aynı olmalı)
        config.setClusterName("dev");

        return config;
    }
}

