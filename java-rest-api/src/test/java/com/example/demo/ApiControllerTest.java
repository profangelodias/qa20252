package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.controller.ApiController;
import com.example.demo.model.Item;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ApiController.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception {
        Item item = new Item(null, "Item 1", "Description 1");

        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)));
    }

    // GET - SUCESSO

    @Test
    public void shouldReturnItemInJson() throws Exception {
        mockMvc.perform(get("/api/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldReturnItemInXml() throws Exception {
        mockMvc.perform(get("/api/items/1")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML));
    }

    // GET - ERRO

    @Test
    public void shouldReturn404WhenItemNotFound() throws Exception {
        mockMvc.perform(get("/api/items/999"))
                .andExpect(status().isNotFound());
    }

    // POST - SUCESSO

    @Test
    public void shouldCreateItemSuccessfully() throws Exception {
        Item item = new Item(null, "Item 2", "Description 2");

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated());
    }

    // POST - VALIDAÇÕES

    @Test
    public void shouldReturn400WhenNameIsNull() throws Exception {
        Item item = new Item(null, null, "Description");

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenNameIsEmpty() throws Exception {
        Item item = new Item(null, "", "Description");

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenDescriptionIsEmpty() throws Exception {
        Item item = new Item(null, "Item", "");

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isBadRequest());
    }

    // SEARCH - GET /search

    @Test
    public void shouldFindItemByName() throws Exception {
        mockMvc.perform(get("/api/items/search")
                        .param("name", "Item 1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn404WhenSearchItemNotFound() throws Exception {
        mockMvc.perform(get("/api/items/search")
                        .param("name", "Inexistente"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn400WhenSearchNameIsEmpty() throws Exception {
        mockMvc.perform(get("/api/items/search")
                        .param("name", ""))
                .andExpect(status().isBadRequest());
    }

    // PATCH - UPDATE DESCRIPTION

    @Test
    public void shouldUpdateDescriptionSuccessfully() throws Exception {
        mockMvc.perform(patch("/api/items/1/description")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Nova descrição\""))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn400WhenDescriptionIsEmptyOnUpdate() throws Exception {
        mockMvc.perform(patch("/api/items/1/description")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn404WhenUpdatingNonExistingItem() throws Exception {
        mockMvc.perform(patch("/api/items/999/description")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Nova descrição\""))
                .andExpect(status().isNotFound());
    }
}
