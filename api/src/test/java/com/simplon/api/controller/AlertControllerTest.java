package com.simplon.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.simplon.api.AbstractIntegrationTest;
import com.simplon.api.repository.AlertRepository;
import com.simplon.api.restEntity.AlertDTO;
import com.simplon.api.security.AuthDTOs.AuthResponse;
import com.simplon.api.security.AuthDTOs.LoginRequest;
import com.simplon.entity.Alert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
public class AlertControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AlertRepository alertRepository;

    private Object accessToken;

    @BeforeEach
    public void login() throws Exception {

        Gson gson = new Gson();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("TEST@SIMPLONVILLE.COM");
        loginRequest.setPassword("ADMIN");

        String requestJson= mappObjectToString(loginRequest);

        this.mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isOk()).andDo(mvcResult -> {
            AuthResponse authResponse = gson.fromJson(mvcResult.getResponse().getContentAsString(),AuthResponse.class);
            this.accessToken = authResponse.getAccessToken();
        });
    }

    @Test
    void getAlertsTest_returnOk() throws Exception {

        this.mvc.perform(get("/alert").header("Authorization","Bearer " +this.accessToken)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void getAlertById_returnOk() throws Exception {

        this.mvc.perform(get("/alert/952546ea-dda3-40c9-83cb-53272773cea9").header("Authorization","Bearer " +this.accessToken))
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

        this.mvc.perform(get("/alert/" + id).header("Authorization","Bearer " +this.accessToken)).andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void getAlertByIdWithoutId_returnNotFound() throws Exception {

        String id = null;

        this.mvc.perform(get("/alert/" + id).header("Authorization","Bearer " +this.accessToken)).andDo(print()).andExpect(status().isNoContent());
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

    private String mappObjectToString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

}
