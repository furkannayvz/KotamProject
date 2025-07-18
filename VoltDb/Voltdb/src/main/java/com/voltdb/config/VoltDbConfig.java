package com.voltdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;

@Configuration
public class VoltDbConfig {

    @Bean(destroyMethod = "close")
    public Client voltClient() throws Exception {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientFactory.createClient(clientConfig);
        //client.createConnection("localhost", 21212);
        client.createConnection("localhost" + ":" + 21212);
        return client;
    }
}

