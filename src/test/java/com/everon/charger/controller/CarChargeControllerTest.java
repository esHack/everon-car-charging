package com.everon.charger.controller;

import com.everon.charger.exception.CarChargeException;
import com.everon.charger.model.ChargingSessionRequest;
import com.everon.charger.model.ChargingSummaryResponse;
import com.everon.charger.service.CarChargeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CarChargeControllerTest {

    private CarChargeService carChargeService;
    private CarChargeController carChargeController;

    @Before
    public void setUp() {
        this.carChargeService = new CarChargeService(new ConcurrentHashMap());
        this.carChargeController = new CarChargeController(carChargeService);
    }

    @Test
    public void ShouldGetAllTheChargingSessionsStored() {
        ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest("ABC-1113232333",
                LocalDateTime.parse("2019-05-06T19:00:20.529"));

        carChargeController.storeCarChargingSession(chargingSessionRequest);

        chargingSessionRequest = new ChargingSessionRequest("ABC-1113232",
                LocalDateTime.parse("2019-05-01T19:00:20.529"));
        carChargeController.storeCarChargingSession(chargingSessionRequest);

        assertThat(carChargeController.getChargingSessions().getBody().size()).isEqualTo(2);
    }

    @Test(expected = CarChargeException.class)
    public void shouldReturnBadRequestIfStationIdIsNull(){
        ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest("",
                LocalDateTime.parse("2019-05-06T19:00:20.529"));
        carChargeController.storeCarChargingSession(chargingSessionRequest);
    }

    @Test
    public void givenStationIdStoreTheValueInCarChargeSessionMap() {
        ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest("ABC-1113232333",
                LocalDateTime.parse("2019-05-06T19:00:20.529"));

        assertThat(carChargeController.storeCarChargingSession(chargingSessionRequest).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(carChargeService.getAllChargingSessions().size()).isEqualTo(1);

        chargingSessionRequest = new ChargingSessionRequest("ABC-1113232",
                LocalDateTime.parse("2019-05-01T19:00:20.529"));
        assertThat(carChargeController.storeCarChargingSession(chargingSessionRequest).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(carChargeService.getAllChargingSessions().size()).isEqualTo(2);
    }

    @Test
    public void ShouldGetSummaryOfAllTheChargingSessionsStored() {
        ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest("ABC-1113232333",
                LocalDateTime.now().minus(2, ChronoUnit.MINUTES));
        carChargeController.storeCarChargingSession(chargingSessionRequest);

        chargingSessionRequest = new ChargingSessionRequest("ABD-1113232", LocalDateTime.now());
        carChargeController.storeCarChargingSession(chargingSessionRequest);

        chargingSessionRequest = new ChargingSessionRequest("ABD-1113755", LocalDateTime.now());
        carChargeController.storeCarChargingSession(chargingSessionRequest);

        chargingSessionRequest = new ChargingSessionRequest("DBC-111321", LocalDateTime.now());
        carChargeController.storeCarChargingSession(chargingSessionRequest);

        String id =
                carChargeService.getAllChargingSessions().stream().filter(s->s.getStationId().equals("ABD-1113232")).findFirst().get().getId().toString();
        carChargeController.stopChargingSession(id);


        ResponseEntity<ChargingSummaryResponse> chargingSummaryResponse =
                carChargeController.getChargingSessionSummary();
        assertThat(chargingSummaryResponse.getBody().getTotalCount()).isEqualTo(3);
        assertThat(chargingSummaryResponse.getBody().getStartedCount()).isEqualTo(2);
        assertThat(chargingSummaryResponse.getBody().getStoppedCount()).isEqualTo(1);

    }

}