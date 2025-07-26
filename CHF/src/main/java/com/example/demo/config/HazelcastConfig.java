package com.example.demo.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.Arrays;

@Configuration
public class HazelcastConfig {
    
    @Value("${hazelcast.cluster.name:dev}")
    private String clusterName;
    
    @Value("${hazelcast.addresses:localhost:5701}")
    private String[] addresses;
    
    @Value("${hazelcast.connection.timeout:30000}")
    private int connectionTimeout;
    
    @Value("${hazelcast.connection.attempt.limit:3}")
    private int connectionAttemptLimit;
    
    @Value("${hazelcast.connection.attempt.period:3000}")
    private int connectionAttemptPeriod;
    
    private HazelcastInstance hazelcastInstance;
    
    @Bean
    public HazelcastInstance hazelcastInstance() {
        try {
            ClientConfig config = new ClientConfig();
            
            config.setClusterName(clusterName);
            
            ClientNetworkConfig networkConfig = config.getNetworkConfig();
            networkConfig.addAddress(addresses);
            networkConfig.setSmartRouting(true);
            networkConfig.setRedoOperation(true);
            networkConfig.setConnectionTimeout(connectionTimeout);
            networkConfig.getConnectionRetryConfig()
                    .setInitialBackoffMillis(1000)
                    .setMaxBackoffMillis(30000)
                    .setMultiplier(2)
                    .setClusterConnectTimeoutMillis(connectionTimeout)
                    .setJitterRange(0.2);
            
            networkConfig.getConnectionRetryConfig().setClusterConnectTimeoutMillis(connectionTimeout);
            
            hazelcastInstance = HazelcastClient.newHazelcastClient(config);
            
            System.out.println(" Hazelcast client connected successfully to cluster: " + clusterName);
            System.out.println(" Connected to addresses: " + Arrays.toString(addresses));
            
            testConnection();
            
            return hazelcastInstance;
            
        } catch (Exception e) {
            System.err.println("Failed to connect to Hazelcast: " + e.getMessage());
            throw new RuntimeException("Could not connect to Hazelcast cluster: " + clusterName, e);
        }
    }
    
    private void testConnection() {
        try {
            IMap<String, String> testMap = hazelcastInstance.getMap("connection-test");
            testMap.put("test-key", "test-value");
            String value = testMap.get("test-key");
            
            if ("test-value".equals(value)) {
                System.out.println("Hazelcast connection test successful");
                testMap.remove("test-key");
            } else {
                System.err.println("Hazelcast connection test failed: unexpected value");
            }
        } catch (Exception e) {
            System.err.println("Hazelcast connection test failed: " + e.getMessage());
        }
    }
    
    @Bean
    public IMap<String, Object> tgfRequestMap() {
        return hazelcastInstance().getMap("tgf-requests");
    }
    
    @Bean
    public IMap<String, Object> chfResponseMap() {
        return hazelcastInstance().getMap("chf-responses");
    }
    
    @PreDestroy
    public void cleanup() {
        if (hazelcastInstance != null) {
            try {
                hazelcastInstance.shutdown();
                System.out.println("Hazelcast client shutdown");
            } catch (Exception e) {
                System.err.println("Error shutting down Hazelcast client: " + e.getMessage());
            }
        }
    }
}
