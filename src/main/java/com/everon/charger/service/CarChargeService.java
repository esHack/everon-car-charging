package com.everon.charger.service;

import com.everon.charger.exception.CarChargeException;
import com.everon.charger.model.ChargingSession;
import com.everon.charger.model.ChargingSessionRequest;
import com.everon.charger.model.ChargingSummaryResponse;
import com.everon.charger.model.StatusEnum;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarChargeService {

    private static final int MAX_RETRIES=5;

    /* Used Concurrent hashmap for thread safe operations and to achieve
     required time complexity for insertions and retrievals */
    @Resource(name = "concurrentHashMap")
    private Map<UUID, ChargingSession> chargingSessionMap;

    public CarChargeService(Map<UUID, ChargingSession> chargingSessionMap) {
        this.chargingSessionMap = chargingSessionMap;
    }

    /**
     * Store the charging sesion
     * @param chargingSessionRequest charging session request
     * @return ChargingSession
     */
    public ChargingSession storeChargingSession(ChargingSessionRequest chargingSessionRequest) {

        if (chargingSessionRequest.getStationId().isEmpty() || chargingSessionRequest.getTimestamp()==null)
            throw new CarChargeException("stationid or timestamp missing in the request", HttpStatus.BAD_REQUEST);

        UUID uuid = getRandomUniqueUUID();
        if(uuid==null)
            throw new CarChargeException("Unable to process request", HttpStatus.INTERNAL_SERVER_ERROR);

        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setId(uuid);
        chargingSession.setStartedAt(chargingSessionRequest.getTimestamp());
        chargingSession.setStationId(chargingSessionRequest.getStationId());
        chargingSession.setStatus(StatusEnum.IN_PROGRESS);

        chargingSessionMap.put(uuid, chargingSession);
        return chargingSession;

    }

    //Get all the stored charging sessions
    public List<ChargingSession> getAllChargingSessions() {
        return chargingSessionMap.values().stream().collect(Collectors.toList());
    }

    public ChargingSession stopChargingSession(String id) {
        UUID chargingSessionId = UUID.fromString(id);

        if (!chargingSessionMap.containsKey(chargingSessionId))
            throw new CarChargeException("Charging session not found", HttpStatus.NOT_FOUND);

        ChargingSession chargingSession = chargingSessionMap.get(chargingSessionId);
        chargingSession.setStatus(StatusEnum.FINISHED);
        chargingSessionMap.put(chargingSessionId, chargingSession);
        return chargingSession;
    }

    public ChargingSummaryResponse getChargingSessionSummary() {

        ChargingSummaryResponse chargingSummaryResponse = new ChargingSummaryResponse();
        List<ChargingSession> chargingSessions =
                chargingSessionMap.values().stream().filter(s -> s.getStartedAt().isAfter(LocalDateTime.now().minus(1,
                        ChronoUnit.MINUTES))).collect(Collectors.toList());

        chargingSummaryResponse.setTotalCount(chargingSessions.size());
        chargingSummaryResponse.setStartedCount(chargingSessions.stream().filter(s -> s.getStatus().equals(StatusEnum.IN_PROGRESS)).count());
        chargingSummaryResponse.setStoppedCount(chargingSessions.stream().filter(s -> s.getStatus().equals(StatusEnum.FINISHED)).count());

        return chargingSummaryResponse;
    }

    //Just to avoid collisions which is rare
    private UUID getRandomUniqueUUID() {
        UUID uuid = UUID.randomUUID();
        for (int i = 0; i < MAX_RETRIES; i++) {
            if (!chargingSessionMap.containsKey(uuid))
                return uuid;
        }
        return null;
    }
}
