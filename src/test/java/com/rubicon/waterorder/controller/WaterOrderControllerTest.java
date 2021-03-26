package com.rubicon.waterorder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rubicon.waterorder.model.*;
import com.rubicon.waterorder.repository.WaterOrderLogRepository;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import com.rubicon.waterorder.service.WaterOrderProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static java.time.LocalDateTime.now;
//import static org.hamcrest.core.IsInstanceOf.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@ExtendWith(MockitoExtension.class)
@Order(4)
class WaterOrderControllerTest {

    @Mock
    WaterOrderRepository waterOrderRepository;

    @Mock
    WaterOrderLogRepository waterOrderLogRepository;

    @Mock
    WaterOrderProcessor waterOrderProcessor;


    @InjectMocks
    WaterOrderController controller;

    MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));




    @BeforeEach
    void setUp() {
        List<WaterOrder> waterOrders = new ArrayList<>();
        WaterOrder waterOrder = new WaterOrder();
        waterOrders.add(waterOrder);

        List<WaterOrderLog> waterOrderLogs = new ArrayList<>();
        WaterOrderLog waterOrderLog = new WaterOrderLog();
        waterOrderLogs.add(waterOrderLog);



        //when(waterOrderRepository.findByIdAndFarmId(anyLong(), anyLong())).thenReturn(waterOrders);

        //when(waterOrderProcessor.createOrder(1L, waterOrderData)).thenReturn(true);
        //when(waterOrderLogRepository.findAll(anyLong())).thenReturn(waterOrderLogs);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @MockitoSettings(strictness = Strictness.WARN)
    @Test
    void createOrder_test() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        WaterOrderData waterOrderData = new WaterOrderData( 5300L, 112L, now().plusSeconds(10).format(formatter) , "PT10S", "Requested");

        given(waterOrderProcessor.createOrder(eq(112L), argThat(new WaterOrderDataWithID(waterOrderData)))).willReturn(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(waterOrderData );


        mockMvc.perform(post("/farm/112/order")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson)
                //.accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

    }

    @MockitoSettings(strictness = Strictness.WARN)
    @Test
    void cancelOrder_test() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        WaterOrderData waterOrderData = new WaterOrderData( 1515L,112L, now().plusSeconds(10).format(formatter) , "PT10S", "Requested");

        //given(waterOrderProcessor.cancelOrder(eq(112L), isA(WaterOrderData.class))).willReturn(true);
        given(waterOrderProcessor.cancelOrder(eq(112L), argThat(new WaterOrderDataWithID(waterOrderData)))).willReturn(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(waterOrderData );

        System.out.println(mapper.writeValueAsString(requestJson));

        mockMvc.perform(put("/farm/112/order/1515")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson)
                //.accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

    }

    @Test
    void getOrderLogs_test() throws Exception {
        //when
        List<WaterOrderLog> waterOrderLogs = new ArrayList<>();
        WaterOrderLog waterOrderLog = new WaterOrderLog();
        waterOrderLogs.add(waterOrderLog);

        when(waterOrderProcessor.findBy_farmId(anyLong())).thenReturn(waterOrderLogs);


        //then
        mockMvc.perform(get("/farm/112/orderLog"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


        //then
        verify(waterOrderProcessor).findBy_farmId(anyLong());
        //assertEquals(1, waterOrderList.size());
    }

    @Test
    void getWaterOrderLogs_test() throws Exception {
        //when
        List<WaterOrderLog> waterOrderLogs = new ArrayList<>();
        WaterOrderLog waterOrderLog = new WaterOrderLog();
        waterOrderLogs.add(waterOrderLog);

        when(waterOrderLogRepository.findBy_farmId(anyLong())).thenReturn(waterOrderLogs);


        //then
        mockMvc.perform(get("/farm/112/waterOrderLogs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


        //then
        verify(waterOrderLogRepository).findBy_farmId(anyLong());
        //assertEquals(1, waterOrderList.size());
    }

    @Test
    void getOrder_test() throws Exception {
        //when
        //List<WaterOrder> waterOrderList =
        List<WaterOrder> waterOrders = new ArrayList<>();
        WaterOrder waterOrder = new WaterOrder();
        waterOrders.add(waterOrder);

        when(waterOrderRepository.findByIdAndFarmId(anyLong(), anyLong())).thenReturn(waterOrders);


        //then
        mockMvc.perform(get("/farm/112/order/11"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        //then
        verify(waterOrderRepository).findByIdAndFarmId(anyLong(), anyLong());


    }


}



