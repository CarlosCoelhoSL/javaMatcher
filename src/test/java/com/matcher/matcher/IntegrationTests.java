package com.matcher.matcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.matcher.matcher.accountDB.Account;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CCOrderRepository ccOrderRepository;

    @Autowired
    private tradeOrderRepository tradeOrderRepository;

    @Test
    @Order(1)
    public void shouldAddAnAccount() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        UserPass wallaceUserPass = new UserPass("Wallace", "Gromit");
        String wallaceUPJSON = ow.writeValueAsString(wallaceUserPass);

        mockMvc.perform(post("/account/newAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wallaceUPJSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wallaceUPJSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                 .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @Order(2)
    public void shouldAddOrder() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        UserPass wallaceUserPass = new UserPass("Wallace", "Gromit");
        String wallaceUPJSON = ow.writeValueAsString(wallaceUserPass);
        CCOrder order1 = new CCOrder(System.nanoTime(),"Wallace", 100, 10, "buy");
        String order1JSON = ow.writeValueAsString(order1);

        MvcResult result = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wallaceUPJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String token = content.substring(10,content.length()-2);
        String tokenHeader = "Bearer "+token;

        mockMvc.perform(put("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(order1JSON)
                .header("Authorization", tokenHeader))
                .andExpect(content().string(containsString("Order added: ")))
                .andExpect(status().isOk());

        assertEquals(1, ccOrderRepository.findAll().size());

    }

    @Test
    @Order(3)
    public void shouldAddSecondOrderAndTrade() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        UserPass gromitUserPass = new UserPass("Gromit", "Wallace");
        String gromitUPJSON = ow.writeValueAsString(gromitUserPass);

        mockMvc.perform(post("/account/newAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gromitUPJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        CCOrder order2 = new CCOrder(System.nanoTime(),"Gromit", 100, 10, "sell");
        String order2JSON = ow.writeValueAsString(order2);

        MvcResult result = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gromitUPJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String token = content.substring(10,content.length()-2);
        String tokenHeader = "Bearer "+token;

        mockMvc.perform(put("/addOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(order2JSON)
                        .header("Authorization", tokenHeader))
                .andExpect(content().string(containsString("Order added: ")))
                .andExpect(status().isOk());

        assertEquals(0, ccOrderRepository.findAll().size());
        assertEquals(1,tradeOrderRepository.findAll().size());
        assertEquals("Wallace",tradeOrderRepository.findAll().get(0).getBuyer());
        assertEquals("Gromit",tradeOrderRepository.findAll().get(0).getSeller());
    }

    @Test
    @Order(4)
    public void checkAggregationOfTwoOrders() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        UserPass gromitUserPass = new UserPass("Gromit", "Wallace");
        String gromitUPJSON = ow.writeValueAsString(gromitUserPass);

        MvcResult result = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gromitUPJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String token = content.substring(10,content.length()-2);
        String tokenHeader = "Bearer "+token;

        CCOrder order2 = new CCOrder(System.nanoTime(),"Gromit", 100, 10, "sell");
        String order2JSON = ow.writeValueAsString(order2);

        mockMvc.perform(put("/addOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(order2JSON)
                        .header("Authorization", tokenHeader))
                .andExpect(content().string(containsString("Order added: ")))
                .andExpect(status().isOk());

        mockMvc.perform(put("/addOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(order2JSON)
                        .header("Authorization", tokenHeader))
                .andExpect(content().string(containsString("Order added: ")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/aggregateOrders")
                .header("Authorization", tokenHeader))
                .andExpect(jsonPath("$[0].quantity").value(200));

    }

    @Test
    @Order(5)
    public void checkPrivateOrders() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        UserPass gromitUserPass = new UserPass("Gromit", "Wallace");
        String gromitUPJSON = ow.writeValueAsString(gromitUserPass);

        MvcResult result = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gromitUPJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String token = content.substring(10,content.length()-2);
        String tokenHeader = "Bearer "+token;

        mockMvc.perform(get("/privateOrders")
                .header("Authorization",tokenHeader))
                .andExpect(jsonPath("$[0].account").value("Gromit"))
                .andExpect(jsonPath("$[1].account").value("Gromit"));
    }
}
