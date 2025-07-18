package org.i2i.kotam.utils.configurations;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.i2i.kotam.utils.constants.StringConstants;

public class Configuration {
    private static HazelcastInstance hazelcastInstance;

    public static HazelcastInstance getHazelcastInstance() {
        if (hazelcastInstance == null) {
            ClientConfig config = new ClientConfig();
            config.setProperty("hazelcast.logging.type", "slf4j"); // isteğe bağlı
            config.getNetworkConfig().addAddress(StringConstants.HAZELCAST_IP);
            hazelcastInstance = HazelcastClient.newHazelcastClient(config);
        }
        return hazelcastInstance;
    }
}