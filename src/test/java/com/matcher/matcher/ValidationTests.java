package com.matcher.matcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ValidationTests {
    @BeforeEach public void initialise() {

    }
    @Autowired
    private MockMvc mockMvc;
    Order falseOrder = new Order("", 200, 10, "buy");
    Order order1 = new Order("Wallace", 100, 10, "buy");
    Order largeAccount = new Order("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 200, 10, "buy");
    Order badAction = new Order("Wallace", 100, 10, "give");

    @Test
    void checkEmptyAccount() throws Exception{
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(order1);

        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = ow.writeValueAsString(falseOrder);

        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/orders")).andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)));
    }

    @Test
    void checkLargeAccount() throws Exception{
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(order1);

        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = ow.writeValueAsString(largeAccount);

        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/orders")).andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)));
    }

    @Test
    void checkNullAction() throws Exception{
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(order1);

        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = ow.writeValueAsString(badAction);

        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/orders")).andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)));
    }
}
