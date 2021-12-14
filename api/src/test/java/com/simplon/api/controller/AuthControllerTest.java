package com.simplon.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.simplon.api.AbstractIntegrationTest;
import com.simplon.api.security.AuthDTOs.LoginRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest  extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginBadCredentialsTest() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("Test");
        loginRequest.setPassword("Test");

        String requestJson=mappObjectToString(loginRequest );

        this.mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isBadRequest());

    }

    @Test
    void loginWrongCredentialsTest() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("Test@test.com");
        loginRequest.setPassword("Test");

        String requestJson= mappObjectToString(loginRequest );

        this.mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isUnauthorized());
    }

    @Test
    void loginWithCorrectCredentialsTest() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("TEST@SIMPLONVILLE.COM");
        loginRequest.setPassword("ADMIN");

        String requestJson= mappObjectToString(loginRequest);

        this.mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isOk());
    }

    private String mappObjectToString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }
}


