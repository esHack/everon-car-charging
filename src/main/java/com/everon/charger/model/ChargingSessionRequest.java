package com.everon.charger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargingSessionRequest {

    private String stationId;
    private LocalDateTime timestamp;
}
