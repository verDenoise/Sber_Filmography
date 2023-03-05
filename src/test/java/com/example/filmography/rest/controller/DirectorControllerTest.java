package com.example.filmography.rest.controller;

import com.example.filmography.CommonTest;
import com.example.filmography.TestData;
import com.example.filmography.dto.AddFilmDto;
import com.example.filmography.dto.DirectorDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DirectorControllerTest extends CommonTest {

    private final DirectorDto directorDto = new DirectorDto("testFIO", "testPosition", new HashSet<>());
    private final DirectorDto newDirectorDto = new DirectorDto("newTestFIO", "newTestPosition", new HashSet<>());
    private final Long id = 2802l;

    @Order(1)
    @Test
    void createDirector() throws Exception {

        String result = mvc.perform(
                        post("/rest/director")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .headers(headers)
                                .content(asJsonString(directorDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(result);
    }

    @Order(2)
    @Test
    void getDirectorList() throws Exception {

        String result = mvc.perform(get("/rest/director/list").headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(result);
    }

    @Order(5)
    @Test
    void deleteDirector() throws Exception {
        String result = mvc.perform(delete("/rest/director/{id}", id).headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(result);
    }

    @Order(3)
    @Test
    void getDirectorById() throws Exception {
        String result = mvc.perform(get("/rest/director/{id}", id).headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(result);
    }

    @Order(4)
    @Test
    void updateDirector() throws Exception {
        String result = mvc.perform(put("/rest/director/{id}", id)
                        .headers(headers)
                        .content(asJsonString(newDirectorDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println(result);
    }

    @Test
    void getDirectorsWithFilms() throws Exception {

        String result = mvc.perform(get("/rest/director/director-films").headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(result);
    }

}
