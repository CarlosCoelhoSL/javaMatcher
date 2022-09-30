package com.matcher.matcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void checkHarrydumbudmb() throws Exception {
        mockMvc.perform(get("/orders")).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldAddOrder() throws Exception {
        Order order1 = new Order("Wallace", 100, 10, "buy");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(order1);

        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/orders")).andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)));
    }

    @Test
    public void shouldMatchTwoOrders() throws Exception {
        Order order1 = new Order("Wallace", 100, 10, "buy");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(order1);

        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        Order order2 = new Order("Gromit", 100, 10, "sell");
        json = ow.writeValueAsString(order2);

        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/tradeOrders")).andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].seller",is("Gromit")));
    }

    @Test
    public void shouldAggregateTwoOrders() throws Exception {
        Order order1 = new Order("Wallace", 100, 10, "buy");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(order1);
        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        mockMvc.perform(get("/aggregateOrders")).andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].quantity",is(200)));
    }
}
