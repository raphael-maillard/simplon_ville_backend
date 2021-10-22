package com.simplon.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.simplon.api.AbstractIntegrationTest;
import com.simplon.api.Repository.AlertRepository;
import com.simplon.api.RestEntity.AlertDTO;
import com.simplon.api.Security.AuthDTOs.AuthResponse;
import com.simplon.api.Security.AuthDTOs.LoginRequest;
import com.simplon.entity.Alert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class AlertControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AlertRepository alertRepository;


    @Test
    void getAlertsTest_returnOk() throws Exception {

        this.mvc.perform(get("/alert")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void getAlertById_returnOk() throws Exception {

        this.mvc.perform(get("/alert/952546ea-dda3-40c9-83cb-53272773cea9"))
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("id").value("952546ea-dda3-40c9-83cb-53272773cea9"))
                .andExpect(jsonPath("city").value("CityTest"))
                .andExpect(jsonPath("fix").isBoolean())
                .andExpect(status().isOk());
    }

    @Test
    void getAlertByIdWithoutId_returnRessourceNotFoundError() throws Exception {

        String id = "IdUnknown";

        this.mvc.perform(get("/alert/" + id)).andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void getAlertByIdWithoutId_returnNotFound() throws Exception {

        String id = null;

        this.mvc.perform(get("/alert/" + id)).andDo(print()).andExpect(status().isNoContent());
    }


    @Test
    void PostAlert_ReturnOk() throws Exception {
        AlertDTO alertDTO = new AlertDTO();
        alertDTO.setCause("Accident");
        alertDTO.setDescription("DescriptionTest");
        alertDTO.setCity("CityTest");
        alertDTO.setDate("DateTest");
        alertDTO.setTime("TimeTest");
        alertDTO.setLocation("Test");
        alertDTO.setFirstname("FirstnameTest");
        alertDTO.setUserAddress("AddressTest");
        alertDTO.setUserZipcode("ZipCodeTest");
        alertDTO.setName("NameTest");
        alertDTO.setPhoneNumber("03030303003");

        ObjectMapper mapper = new ObjectMapper();
        String AlertJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(alertDTO);

        this.mvc.perform(post("/alert").contentType(MediaType.APPLICATION_JSON)
                        .content(AlertJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void PostAlert_ReturnError() throws Exception {
        AlertDTO alertDTO = new AlertDTO();
        alertDTO.setCity("CityTest");
        alertDTO.setDate("DateTest");
        alertDTO.setTime("TimeTest");
        alertDTO.setLocation("Test");
        alertDTO.setFirstname("FirstnameTest");
        alertDTO.setUserAddress("AddressTest");
        alertDTO.setUserZipcode("ZipCodeTest");
        alertDTO.setName("NameTest");
        alertDTO.setPhoneNumber("03030303003");

        ObjectMapper mapper = new ObjectMapper();
        String AlertJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(alertDTO);

        this.mvc.perform(post("/alert").contentType(MediaType.APPLICATION_JSON)
                        .content(AlertJson))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void PostAlertDTOEmpty_ReturnError() throws Exception {
        AlertDTO alertDTO = new AlertDTO();

        ObjectMapper mapper = new ObjectMapper();
        String AlertJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(alertDTO);

        this.mvc.perform(post("/alert").contentType(MediaType.APPLICATION_JSON)
                        .content(AlertJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void PostAlertFixTrue_ReturnOk() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("ADMIN");

        Alert alert = alertRepository.findById("952546ea-dda3-40c9-83cb-53272773cea9").get();
        AlertDTO alterDTO = AlertDTO.builder()
                .id(alert.getId())
                .cause(alert.getCause())
                .description(alert.getDescription())
                .location(alert.getLocation())
                .name(alert.getName())
                .firstname(alert.getFirstname())
                .build();

        ObjectMapper mapper = new ObjectMapper();

        String jsonResult = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(loginRequest);

        var resultToken = this.mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult))
                .andExpect(status().isOk());

        MvcResult tokenReponse = resultToken.andReturn();
        String AlertJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(alterDTO);

        Gson gson = new Gson();
        var result = gson.fromJson(tokenReponse.getResponse().getContentAsString(), AuthResponse.class);


        this.mvc.perform(post("/alert/fix").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + result.getAccessToken())
                        .content(AlertJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void PostAlertFixFalseWithoutLogin_ReturnUnauthorized() throws Exception {

        Alert alert = alertRepository.findById("952546ea-dda3-40c9-83cb-53272773cea9").get();
        AlertDTO alterDTO = AlertDTO.builder()
                .id(alert.getId())
                .cause(alert.getCause())
                .description(alert.getDescription())
                .location(alert.getLocation())
                .name(alert.getName())
                .firstname(alert.getFirstname())
                .build();

        ObjectMapper mapper = new ObjectMapper();

        String AlertJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(alterDTO);

        this.mvc.perform(post("/alert/fix").contentType(MediaType.APPLICATION_JSON)
                        .content(AlertJson))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void PostDeleteAlert_ReturnOk() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("ADMIN");

        Alert alert = alertRepository.findById("952546ea-dda3-40c9-83cb-53272773cea8").get();
        AlertDTO alterDTO = AlertDTO.builder()
                .id(alert.getId())
                .cause(alert.getCause())
                .description(alert.getDescription())
                .location(alert.getLocation())
                .name(alert.getName())
                .firstname(alert.getFirstname())
                .build();

        ObjectMapper mapper = new ObjectMapper();

        String jsonResult = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(loginRequest);

        var resultToken = this.mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult))
                .andExpect(status().isOk());

        MvcResult tokenResponse = resultToken.andReturn();
        String AlertJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(alterDTO);

        Gson gson = new Gson();
        var result = gson.fromJson(tokenResponse.getResponse().getContentAsString(), AuthResponse.class);

        this.mvc.perform(post("/alert/delete").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + result.getAccessToken())
                        .content(AlertJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
