package com.simplon.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.simplon.api.AbstractIntegrationTest;
import com.simplon.api.RestEntity.UserDTO;
import com.simplon.api.Security.AuthDTOs.AuthResponse;
import com.simplon.api.Security.AuthDTOs.LoginRequest;
import com.simplon.api.Security.Services.TokenProvider;
import com.simplon.api.Service.UserService;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest  extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    private Object accessToken;

    @BeforeEach
    public void login() throws Exception {

        Gson gson = new Gson();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("TEST@SIMPLONVILLE.COM");
        loginRequest.setPassword("ADMIN");

        String requestJson= mappObjectToString(loginRequest);

        this.mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isOk()).andDo(mvcResult -> {
            AuthResponse authResponse = gson.fromJson(mvcResult.getResponse().getContentAsString(),AuthResponse.class);
            this.accessToken = authResponse.getAccessToken();
        });

    }


    @Test
    public void getUserWithIncorrectEmailTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("Test");

        String requestJson= mappObjectToString(userDTO);

        this.mockMvc.perform(post("/user").header("Authorization","Bearer " +this.accessToken).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isBadRequest());
    }

    @Test
    public void getUserWithWrongEmailTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("Test@Test.com");

        String requestJson= mappObjectToString(userDTO);

        this.mockMvc.perform(post("/user").header("Authorization","Bearer " +this.accessToken).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isNoContent());
    }

    @Test
    public void getUserWithCorrectEmailTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("TEST@SIMPLONVILLE.COM");

        String requestJson= mappObjectToString(userDTO);

        this.mockMvc.perform(post("/user").header("Authorization","Bearer " +this.accessToken).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isOk());
    }


    private String mappObjectToString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }
}
