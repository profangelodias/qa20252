package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    // Este método roda ANTES de cada teste
    @BeforeEach
    public void setUp() throws Exception {
        // Limpa o estado do controller (simulado) antes de cada teste
        // (Embora o @WebMvcTest já isole, isso é uma boa prática para clareza)
        // A melhoria aqui é criar um item que os testes GET possam usar.
        Item item = new Item(1L, "Item 1", "Description 1");
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)));
    }

    @Test
    public void testGetItemJson() throws Exception {
        mockMvc.perform(get("/api/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"name\":\"Item 1\",\"description\":\"Description 1\"}"));
    }

    @Test
    public void testGetItemXml() throws Exception {
        mockMvc.perform(get("/api/items/1")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andExpect(content().xml("<Item><id>1</id><name>Item 1</name><description>Description 1</description></Item>"));
    }

    @Test
    public void testCreateItem() throws Exception {
        // O ID será gerado pelo servidor, então podemos omiti-lo ou usar qualquer valor
        Item newItem = new Item(null, "Item 2", "Description 2");
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isCreated()); // Agora o teste espera 201 Created
    }

    @Test
    public void testGetItemNotFound() throws Exception {
        mockMvc.perform(get("/api/items/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}