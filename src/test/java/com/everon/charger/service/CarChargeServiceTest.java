package com.everon.charger.service;

import com.everon.charger.controller.CarChargeController;
import com.everon.charger.model.ChargingSession;
import com.everon.charger.model.ChargingSessionRequest;
import com.everon.charger.model.ChargingSummaryResponse;
import com.everon.charger.model.StatusEnum;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class CarChargeServiceTest {

    private CarChargeService carChargeService;

    @Before
    public void setUp() {
        ConcurrentHashMap<UUID, ChargingSession> chargingSessionMap = new ConcurrentHashMap();

        UUID uuid = UUID.randomUUID();
        ChargingSession chargingSession = new ChargingSession(uuid,"ABC-1123", LocalDateTime.now(),
                StatusEnum.IN_PROGRESS);
        chargingSessionMap.put(uuid,chargingSession);

        UUID uuid1 = UUID.randomUUID();
        ChargingSession chargingSession1 = new ChargingSession(uuid1,"ABC-1124", LocalDateTime.now().minusMinutes(10),
                StatusEnum.FINISHED);
        chargingSessionMap.put(uuid1,chargingSession1);

        this.carChargeService = new CarChargeService(chargingSessionMap);
    }

    @Test
    public void testStoreChargingSession() {
        ChargingSessionRequest request = new ChargingSessionRequest("ABC-1125",LocalDateTime.now());
        ChargingSession chargingSession = carChargeService.storeChargingSession(request);
        assertEquals(chargingSession.getStatus(),StatusEnum.IN_PROGRESS);
        assertEquals(carChargeService.getAllChargingSessions().size(),3);
    }

    @Test
    public void testGetAllChargingSessions() {
        assertEquals(carChargeService.getAllChargingSessions().parallelStream().filter(s->s.getStationId().equals("ABC" +
                "-1123")).count(),1);
    }

    @Test
    public void testStopChargingSession() {
        String id =
                carChargeService.getAllChargingSessions().stream().filter(s->s.getStationId().equals("ABC-1123")).findFirst().get().getId().toString();
        ChargingSession chargingSession= carChargeService.stopChargingSession(id);
        assertEquals(chargingSession.getStatus(),StatusEnum.FINISHED);

    }

    @Test
    public void testGetChargingSessionSummary() {
        ChargingSummaryResponse chargingSummaryResponse = carChargeService.getChargingSessionSummary();
        assertEquals(chargingSummaryResponse.getTotalCount(),1);
    }
}