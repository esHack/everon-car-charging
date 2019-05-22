package com.everon.charger.controller;

import com.everon.charger.model.ChargingSession;
import com.everon.charger.model.ChargingSessionRequest;
import com.everon.charger.model.ChargingSummaryResponse;
import com.everon.charger.service.CarChargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chargingSessions")
public class CarChargeController {

    private CarChargeService carChargeService;

    public CarChargeController(CarChargeService carChargeService) {
        this.carChargeService = carChargeService;
    }

    @PostMapping
    public ResponseEntity<ChargingSession> storeCarChargingSession(@RequestBody ChargingSessionRequest chargingSessionRequest) {
        return ResponseEntity.ok(carChargeService.storeChargingSession(chargingSessionRequest));
    }

    @GetMapping
    public ResponseEntity<List<ChargingSession>> getChargingSessions(){
        List<ChargingSession> chargingSessions = carChargeService.getAllChargingSessions();
        return ResponseEntity.ok(chargingSessions) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChargingSession> stopChargingSession(@PathVariable String id) {
        return ResponseEntity.ok(carChargeService.stopChargingSession(id));
    }

    @GetMapping("/summary")
    public ResponseEntity<ChargingSummaryResponse> getChargingSessionSummary(){
        return ResponseEntity.ok(carChargeService.getChargingSessionSummary()) ;
    }
}
