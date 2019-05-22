package com.everon.charger.controller;

import com.everon.charger.model.ChargingSession;
import com.everon.charger.model.ChargingSessionRequest;
import com.everon.charger.model.StatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void givenStationIdStoreTheValueInCarChargeSessionMap() throws Exception {
        ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest("ABC-1113232333",
                LocalDateTime.parse("2019-05-06T19:00:20.529"));
        mvc.perform(MockMvcRequestBuilders
                .post("/chargingSessions")
                .content(mapper.writeValueAsString(chargingSessionRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.stationId", is("ABC-1113232333")));

    }


    @Test
    public void givenNullStationIdShouldReturnBadRequest() throws Exception {
        ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest("",
                LocalDateTime.parse("2019-05-06T19:00:20.529"));
        mvc.perform(MockMvcRequestBuilders
                .post("/chargingSessions")
                .content(mapper.writeValueAsString(chargingSessionRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void ShouldGetAllTheChargingSessionsStored() throws Exception {
        ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest("ABC-1113232333",
                LocalDateTime.parse("2019-05-06T19:00:20.529"));

        mvc.perform(MockMvcRequestBuilders
                .post("/chargingSessions")
                .content(mapper.writeValueAsString(chargingSessionRequest))
                .contentType(MediaType.APPLICATION_JSON));


        mvc.perform(MockMvcRequestBuilders
                .get("/chargingSessions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void shouldReturnNotFoundIfIdIsNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .put("/chargingSessions/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void ShouldStopTheChargingSessionsStored() throws Exception {
        ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest("ABC-1113232333",
                LocalDateTime.parse("2019-05-06T19:00:20.529"));

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .post("/chargingSessions")
                .content(mapper.writeValueAsString(chargingSessionRequest))
                .contentType(MediaType.APPLICATION_JSON));

        String json = resultActions.andReturn().getResponse().getContentAsString();
        ChargingSession chargingSession = mapper.readValue(json, ChargingSession.class);


        mvc.perform(MockMvcRequestBuilders
                .put("/chargingSessions/" + chargingSession.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(StatusEnum.FINISHED.toString())));

    }
}
