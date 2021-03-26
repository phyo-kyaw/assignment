package com.rubicon.waterorder.controller;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
//import org.assertj.core.api.Assert;
import com.rubicon.waterorder.model.WaterOrderLog;
import com.rubicon.waterorder.repository.WaterOrderRepository;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(5)
class ___WaterOrderController_IT {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    WaterOrderRepository waterOrderRepository;


    Long interestedJobId=0L;

    @Test
    void contextLoads() throws JsonEOFException {

        String orderList = this.testRestTemplate.getForObject("/farm/112/order/1", String.class);

        System.out.println("******** ->" + orderList.length());
    }

    @Test
    @Order(1)
    public void createOrder_1st_order_success() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        WaterOrderData wod1 = new WaterOrderData( 222L, now().plusSeconds(5).format(formatter), "PT5S", "Requested");

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
    public void createOrder_2nd_order_success() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        WaterOrderData wod1 = new WaterOrderData( 222L, now().plusSeconds(15).format(formatter), "PT5S", "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order";
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);

        ResponseEntity<Void> result = restTemplate.postForEntity(uri, request, Void.class);

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    @Order(3)
    public void createOrder_not_futureDate_fail() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //existing data in Repo
        WaterOrderData wod1 = new WaterOrderData( 222L, now().minusSeconds(10).format(formatter), "PT10S", "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order";
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);


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
    @Order(4)
    public void createOrder_overlap_beginning_fail() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //existing data in Repo
        WaterOrderData wod1 = new WaterOrderData(222L, now().plusSeconds(8).format(formatter), "PT5S", "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order";
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);


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
    @Order(5)
    public void createOrder_overlap_ending_fail() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //existing data in Repo
        WaterOrderData wod1 = new WaterOrderData(222L, now().plusSeconds(18).format(formatter), "PT5S", "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order";
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);


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
    @Order(6)
    public void getOrder_total_2_orders_success() throws URISyntaxException {

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
        assertTrue(2 == result.getBody().size());
    }

    //@Disabled
    @Test
    @Order(7)
    public void getOrderLog_total_6_logs_success() throws URISyntaxException, InterruptedException {

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //existing data in Repo
        //WaterOrderData wod1 = new WaterOrderData(111L, 222L, now().plusSeconds(10).format(formatter), 10L, "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/orderLogs" ;
        URI uri = new URI(baseUrl);

        //HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);

        Thread.sleep(21000);

/*        WaterOrderLog waterOrderLog = new WaterOrderLog();
        HttpEntity<WaterOrderLog> entity = new HttpEntity<WaterOrderLog>(waterOrderLog);
        ResponseEntity<List<WaterOrderLog>> result = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<WaterOrderLog>>() {});
        List<WaterOrderLog> waterOrderLogs = result.getBody();*/

        //ResponseEntity<List> result = restTemplate.getForEntity(uri, List.class);
        //List<WaterOrderLog> waterOrderLogs = result.getBody();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null);

        List<WaterOrderLog> waterOrderLogs = new ArrayList<>();
        ParameterizedTypeReference<List<WaterOrderLog>> responseType = new ParameterizedTypeReference<List<WaterOrderLog>>() {};
        ResponseEntity<List<WaterOrderLog>> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, responseType);
        waterOrderLogs = result.getBody();

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
        System.out.println(result.getBody());
        assertEquals(6, waterOrderLogs.size());

        Long count = waterOrderLogs
                .stream()
                .filter(x -> x.getOrderStatus().toString().equals(Status.Delivered.name()))
                .count();

        assertEquals(2L, count.longValue());
    }

    @Test
    @Order(8)
    public void createOrder_3rd_order_success() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        WaterOrderData wod1 = new WaterOrderData( 333L, now().plusSeconds(2).format(formatter), "PT2S", "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order";
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);

        ResponseEntity<Void> result = restTemplate.postForEntity(uri, request, Void.class);

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
    }


    @Test
    @Order(9)
    public void getOrder_total_3_orders_success() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/orders" ;
        URI uri = new URI(baseUrl);

        //WaterOrder waterOrder = new WaterOrder(111L, 222L, now().plusSeconds(10).format(formatter), "PT10S", Status.Requested);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);

        List<WaterOrder> waterOrders = new ArrayList<>();
        ParameterizedTypeReference<List<WaterOrder>> responseType = new ParameterizedTypeReference<List<WaterOrder>>() {};
        ResponseEntity<List<WaterOrder>> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, responseType);
        waterOrders = result.getBody();

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
        System.out.println(result.getBody());
        assertTrue(3 == result.getBody().size());

        WaterOrder maxObject = Collections.max(waterOrders, new Comparator<WaterOrder>() {
            @Override
            public int compare(WaterOrder o1, WaterOrder o2) {
                if (o1.getId().longValue() == o2.getId().longValue()) {
                    return 0;
                } else if (o1.getId().longValue() < o2.getId().longValue()) {
                    return -1;
                } else if (o1.getId().longValue() > o2.getId().longValue()) {
                    return 1;
                }
                return 0;
            }
        });

        this.interestedJobId = maxObject.getId();
        System.out.println("##################" + this.interestedJobId );
                System.out.println("************ " + maxObject);
        System.out.println("************ " + waterOrders);
    }

    //@Disabled
    @Test
    @Order(10)
    public void cancelOrder_3rd_order_in_request_stage_success() throws URISyntaxException {

        List<WaterOrder> waterOrders = new ArrayList<>();
        waterOrders = waterOrderRepository.findAll();

        WaterOrder maxObject = Collections.max(waterOrders, new Comparator<WaterOrder>() {
            @Override
            public int compare(WaterOrder o1, WaterOrder o2) {
                if (o1.getId().longValue() == o2.getId().longValue()) {
                    return 0;
                } else if (o1.getId().longValue() < o2.getId().longValue()) {
                    return -1;
                } else if (o1.getId().longValue() > o2.getId().longValue()) {
                    return 1;
                }
                return 0;
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("############## " + maxObject.getId());
        WaterOrder wo1 = waterOrderRepository.findById(maxObject.getId()).get();
        WaterOrderData wod1 = new WaterOrderData(
                wo1.getId(),
                wo1.getFarmId(),
                wo1.getStartDateTime().format(formatter),
                wo1.getFlowDuration().toString(),
                "Cancelled"
        );

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order/" + wod1.getId();
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);

        ResponseEntity<Void> result = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    @Order(11)
    public void getOrderLog_total_8_logs_success() throws URISyntaxException, InterruptedException {

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/orderLogs" ;
        URI uri = new URI(baseUrl);

        //ResponseEntity<List> result = restTemplate.getForEntity(uri, List.class);
        //List<WaterOrderLog> waterOrderLogs = result.getBody();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null);

        List<WaterOrderLog> waterOrderLogs = new ArrayList<>();
        ParameterizedTypeReference<List<WaterOrderLog>> responseType = new ParameterizedTypeReference<List<WaterOrderLog>>() {};
        ResponseEntity<List<WaterOrderLog>> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, responseType);
        waterOrderLogs = result.getBody();

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
        System.out.println(result.getBody());
        assertEquals(8, waterOrderLogs.size());

        Long count = waterOrderLogs
                .stream()
                .filter(x -> x.getOrderStatus().toString().equals(Status.Cancelled.name()))
                .count();

        assertEquals(1L, count.longValue());
    }

    /////////////////////////////
    @Test
    @Order(12)
    public void createOrder_4th_order_success() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        WaterOrderData wod1 = new WaterOrderData( 444L, now().plusSeconds(2).format(formatter), "PT2S", "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order";
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);

        ResponseEntity<Void> result = restTemplate.postForEntity(uri, request, Void.class);

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
    }


    @Test
    @Order(13)
    public void getOrder_total_4_orders_success() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/orders" ;
        URI uri = new URI(baseUrl);

        //WaterOrder waterOrder = new WaterOrder(111L, 222L, now().plusSeconds(10).format(formatter), "PT10S", Status.Requested);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);

        List<WaterOrder> waterOrders = new ArrayList<>();
        ParameterizedTypeReference<List<WaterOrder>> responseType = new ParameterizedTypeReference<List<WaterOrder>>() {};
        ResponseEntity<List<WaterOrder>> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, responseType);
        waterOrders = result.getBody();

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
        System.out.println(result.getBody());
        assertTrue(4 == result.getBody().size());

        WaterOrder maxObject = Collections.max(waterOrders, new Comparator<WaterOrder>() {
            @Override
            public int compare(WaterOrder o1, WaterOrder o2) {
                if (o1.getId().longValue() == o2.getId().longValue()) {
                    return 0;
                } else if (o1.getId().longValue() < o2.getId().longValue()) {
                    return -1;
                } else if (o1.getId().longValue() > o2.getId().longValue()) {
                    return 1;
                }
                return 0;
            }
        });

        this.interestedJobId = maxObject.getId();
        System.out.println("##################" + this.interestedJobId );
        System.out.println("************ " + maxObject);
        System.out.println("************ " + waterOrders);
    }

    //@Disabled
    @Test
    @Order(14)
    public void cancelOrder_4th_order_in_start_stage_success() throws URISyntaxException, InterruptedException {

        List<WaterOrder> waterOrders = new ArrayList<>();
        waterOrders = waterOrderRepository.findAll();

        WaterOrder maxObject = Collections.max(waterOrders, new Comparator<WaterOrder>() {
            @Override
            public int compare(WaterOrder o1, WaterOrder o2) {
                if (o1.getId().longValue() == o2.getId().longValue()) {
                    return 0;
                } else if (o1.getId().longValue() < o2.getId().longValue()) {
                    return -1;
                } else if (o1.getId().longValue() > o2.getId().longValue()) {
                    return 1;
                }
                return 0;
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("############## " + maxObject.getId());
        WaterOrder wo1 = waterOrderRepository.findById(maxObject.getId()).get();
        WaterOrderData wod1 = new WaterOrderData(
                wo1.getId(),
                wo1.getFarmId(),
                wo1.getStartDateTime().format(formatter),
                wo1.getFlowDuration().toString(),
                "Cancelled"
        );

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order/" + wod1.getId();
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);

        Thread.sleep(2500);

        ResponseEntity<Void> result = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    @Order(15)
    public void getOrderLog_total_11_logs_success() throws URISyntaxException, InterruptedException {

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/orderLogs" ;
        URI uri = new URI(baseUrl);

        //ResponseEntity<List> result = restTemplate.getForEntity(uri, List.class);
        //List<WaterOrderLog> waterOrderLogs = result.getBody();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null);

        List<WaterOrderLog> waterOrderLogs = new ArrayList<>();
        ParameterizedTypeReference<List<WaterOrderLog>> responseType = new ParameterizedTypeReference<List<WaterOrderLog>>() {};
        ResponseEntity<List<WaterOrderLog>> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, responseType);
        waterOrderLogs = result.getBody();

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
        System.out.println(result.getBody());
        assertEquals(11, waterOrderLogs.size());

        Long count = waterOrderLogs
                .stream()
                .filter(x -> x.getOrderStatus().toString().equals(Status.Cancelled.name()))
                .count();

        assertEquals(2L, count.longValue());
    }

    /////////////////////////////
    @Test
    @Order(16)
    public void createOrder_5th_order_success() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        WaterOrderData wod1 = new WaterOrderData( 444L, now().plusSeconds(2).format(formatter), "PT2S", "Requested");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order";
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);

        ResponseEntity<Void> result = restTemplate.postForEntity(uri, request, Void.class);

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
    }


    @Test
    @Order(17)
    public void getOrder_total_5_orders_success() throws URISyntaxException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/orders" ;
        URI uri = new URI(baseUrl);

        //WaterOrder waterOrder = new WaterOrder(111L, 222L, now().plusSeconds(10).format(formatter), "PT10S", Status.Requested);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);

        List<WaterOrder> waterOrders = new ArrayList<>();
        ParameterizedTypeReference<List<WaterOrder>> responseType = new ParameterizedTypeReference<List<WaterOrder>>() {};
        ResponseEntity<List<WaterOrder>> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, responseType);
        waterOrders = result.getBody();

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
        System.out.println(result.getBody());
        assertTrue(5 == result.getBody().size());

        WaterOrder maxObject = Collections.max(waterOrders, new Comparator<WaterOrder>() {
            @Override
            public int compare(WaterOrder o1, WaterOrder o2) {
                if (o1.getId().longValue() == o2.getId().longValue()) {
                    return 0;
                } else if (o1.getId().longValue() < o2.getId().longValue()) {
                    return -1;
                } else if (o1.getId().longValue() > o2.getId().longValue()) {
                    return 1;
                }
                return 0;
            }
        });
        
    }

    //@Disabled
    @Test
    @Order(18)
    public void cancelOrder_5th_order_in_delivered_stage_fail() throws URISyntaxException, InterruptedException {

        List<WaterOrder> waterOrders = new ArrayList<>();
        waterOrders = waterOrderRepository.findAll();

        WaterOrder maxObject = Collections.max(waterOrders, new Comparator<WaterOrder>() {
            @Override
            public int compare(WaterOrder o1, WaterOrder o2) {
                if (o1.getId().longValue() == o2.getId().longValue()) {
                    return 0;
                } else if (o1.getId().longValue() < o2.getId().longValue()) {
                    return -1;
                } else if (o1.getId().longValue() > o2.getId().longValue()) {
                    return 1;
                }
                return 0;
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("############## " + maxObject.getId());
        WaterOrder wo1 = waterOrderRepository.findById(maxObject.getId()).get();
        WaterOrderData wod1 = new WaterOrderData(
                wo1.getId(),
                wo1.getFarmId(),
                wo1.getStartDateTime().format(formatter),
                wo1.getFlowDuration().toString(),
                "Cancelled"
        );

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/farm/" + wod1.getFarmId() + "/order/" + wod1.getId();
        URI uri = new URI(baseUrl);

        HttpEntity<WaterOrderData> request = new HttpEntity<>(wod1);

        Thread.sleep(4500);



        try
        {
            ResponseEntity<Void> result = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);
            Assertions.fail();
        }
        catch(HttpClientErrorException ex)
        {
            //Verify bad request and missing header
            assertEquals(400, ex.getRawStatusCode());
            //assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
        }

        //Verify request succeed
        //assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    @Order(19)
    public void getOrderLog_total_14_logs_success() throws URISyntaxException, InterruptedException {

        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:" + randomServerPort + "/orderLogs" ;
        URI uri = new URI(baseUrl);

        //ResponseEntity<List> result = restTemplate.getForEntity(uri, List.class);
        //List<WaterOrderLog> waterOrderLogs = result.getBody();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null);

        List<WaterOrderLog> waterOrderLogs = new ArrayList<>();
        ParameterizedTypeReference<List<WaterOrderLog>> responseType = new ParameterizedTypeReference<List<WaterOrderLog>>() {};
        ResponseEntity<List<WaterOrderLog>> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, responseType);
        waterOrderLogs = result.getBody();

        //Verify request succeed
        assertEquals(200, result.getStatusCodeValue());
        System.out.println(result.getBody());
        assertEquals(14, waterOrderLogs.size());

        Long count = waterOrderLogs
                .stream()
                .filter(x -> x.getOrderStatus().toString().equals(Status.Cancelled.name()))
                .count();

        assertEquals(2L, count.longValue());
    }

}