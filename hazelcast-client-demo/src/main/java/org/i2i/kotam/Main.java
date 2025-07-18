package org.i2i.kotam;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hazelcast application is starting...");

        HazelcastManager.main(args);

        System.out.println("The application is complete.");
    }
}
