package com.everon.charger;

import com.everon.charger.model.ChargingSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(name="concurrentHashMap")
    public Map<UUID, ChargingSession> chargingSessionMap(){
        return new ConcurrentHashMap();
    }
}
