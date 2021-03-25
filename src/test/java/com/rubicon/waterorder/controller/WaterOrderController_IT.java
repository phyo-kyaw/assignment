package com.rubicon.waterorder.controller;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
//import org.assertj.core.api.Assert;
import com.rubicon.waterorder.model.WaterOrderLog;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
//import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WaterOrderController_IT {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() throws JsonEOFException {

        String orderList = this.testRestTemplate.getForObject("/farm/112/order/1", String.class);

        System.out.println("******** ->" + orderList.length());
    }

    @Test
    @Order(1)
    public void createOrder_success() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //existing data in Repo
        WaterOrderData wod1 = new WaterOrderData(111L, 222L, now().plusSeconds(10).format(formatter), "PT10S", "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order";
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);

        ResponseEntity<Void> result = restTemplate.postForEntity(uri, request, Void.class);

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    @Order(2)
    public void createOrder_futureDate_fail() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //existing data in Repo
        WaterOrderData wod1 = new WaterOrderData(111L, 222L, now().minusSeconds(10).format(formatter), "PT10S", "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order";
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);

        //ResponseEntity<Void> result = restTemplate.postForEntity(uri, request, Void.class);

        //Verify request succeed
        //assertEquals(400, result.getStatusCodeValue());
        try
        {
            restTemplate.postForEntity(uri, request, Void.class);
            Assertions.fail();
        }
        catch(HttpClientErrorException ex)
        {
            //Verify bad request and missing header
            assertEquals(400, ex.getRawStatusCode());
            //assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
        }

    }

    @Test
    @Order(3)
    public void getOrder_success() throws URISyntaxException {

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //existing data in Repo
        //WaterOrderData wod1 = new WaterOrderData(111L, 222L, now().plusSeconds(10).format(formatter), 10L, "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/orders" ;
        URI uri = new URI(baseUrl);

        //HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);
        //WaterOrder waterOrder = new WaterOrder();
        //HttpEntity<WaterOrder> entity = new HttpEntity<WaterOrder>(waterOrder);
        //ResponseEntity<List<WaterOrder>> result = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<WaterOrder>>() {});
        //List<WaterOrder> waterOrders = result.getBody();

        //ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        ResponseEntity<List> result = restTemplate.getForEntity(uri, List.class);

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
        System.out.println(result.getBody());
        assertTrue(1 == result.getBody().size());
    }

    @Test
    @Order(4)
    public void getOrderLog_success() throws URISyntaxException, InterruptedException {

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //existing data in Repo
        //WaterOrderData wod1 = new WaterOrderData(111L, 222L, now().plusSeconds(10).format(formatter), 10L, "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/orderLogs" ;
        URI uri = new URI(baseUrl);

        //HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);

        Thread.sleep(25000);

/*        WaterOrderLog waterOrderLog = new WaterOrderLog();
        HttpEntity<WaterOrderLog> entity = new HttpEntity<WaterOrderLog>(waterOrderLog);
        ResponseEntity<List<WaterOrderLog>> result = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<WaterOrderLog>>() {});
        List<WaterOrderLog> waterOrderLogs = result.getBody();*/

        ResponseEntity<List> result = restTemplate.getForEntity(uri, List.class);
        List<WaterOrderLog> waterOrderLogs = result.getBody();

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
        System.out.println(result.getBody());
        assertEquals(3, waterOrderLogs.size());
    }



}