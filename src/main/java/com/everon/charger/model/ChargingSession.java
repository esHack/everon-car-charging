package com.everon.charger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargingSession {

    private UUID id;
    private String stationId;
    private LocalDateTime startedAt;
    private StatusEnum status;

}
