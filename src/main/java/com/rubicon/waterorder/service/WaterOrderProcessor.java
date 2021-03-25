package com.rubicon.waterorder.service;

import com.rubicon.waterorder.mapper.WaterOrderLogMapper;
import com.rubicon.waterorder.mapper.WaterOrderMapper;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.model.WaterOrderLog;
import com.rubicon.waterorder.repository.WaterOrderLogRepository;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import com.rubicon.waterorder.validator.WaterOrderValidator;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.net.URI;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Service
public class WaterOrderProcessor {

    WaterOrderRepository waterOrderRepository;

    WaterOrderLogRepository waterOrderLogRepository;

    ApplicationEventPublisher publisher;

    WaterOrderMapper waterOrderMapper;

    WaterOrderValidator waterOrderValidator;

    WaterOrderLogMapper waterOrderLogMapper;

    SchedulerService schedulerService = SchedulerService.getInstance();

    @PersistenceContext
    private EntityManager entityManager;


    public WaterOrderProcessor() {
    }

    @Autowired
    public WaterOrderProcessor(WaterOrderRepository waterOrderRepository,
                               WaterOrderLogRepository waterOrderLogRepository,
                               ApplicationEventPublisher publisher,
                               WaterOrderMapper waterOrderMapper,
                               WaterOrderValidator waterOrderValidator,
                               WaterOrderLogMapper waterOrderLogMapper) {
        this.waterOrderRepository = waterOrderRepository;
        this.waterOrderLogRepository = waterOrderLogRepository;
        this.publisher = publisher;
        this.waterOrderMapper = waterOrderMapper;
        this.waterOrderValidator = waterOrderValidator;
        this.waterOrderLogMapper = waterOrderLogMapper;
    }

    public boolean createOrder(Long farmId, WaterOrderData waterOrderData){

        if(!waterOrderValidator.isValidDate(waterOrderData.getStartDateTime())){
            return false;
        }

        if(!waterOrderValidator.isFutureDate(waterOrderData.getStartDateTime())){
            return false;
        }

        if(waterOrderValidator.isOrderOverlap(waterOrderData)){
            return false;
        }

        WaterOrder waterOrderToCreate = waterOrderMapper.constructWaterOrder(waterOrderData);
        WaterOrder waterOrderCreated = waterOrderRepository.save(waterOrderToCreate);

        WaterOrderLog waterOrderLogToCreate = waterOrderLogMapper.constructWaterOrderLog(waterOrderCreated);
        WaterOrderLog waterOrderLogCreated = waterOrderLogRepository.save(waterOrderLogToCreate);

        schedulerService.setApplicationEventPublisher(this.publisher);
        schedulerService.scheduleStartTask(waterOrderCreated);

        return true;
    }

    public boolean cancelOrder(Long farmId, WaterOrderData waterOrderData) {

        //requested waterOrderData must be with cancel status
        if (!waterOrderData.getOrderStatus().toString().equals(Status.Cancelled.toString())) {
            return false;
        }

        //check with db if it's been delivered or cancelled.
        if (waterOrderValidator.isDeliveredOrCancelled(waterOrderData)) {
            return false;
        }

        WaterOrder waterOrderToCancel = waterOrderMapper.constructWaterOrder(waterOrderData);

        //scheduler to cancel the task
        schedulerService.setApplicationEventPublisher(this.publisher);
        schedulerService.cancelTask(waterOrderToCancel);

        return true;
    }

    public List<WaterOrderLog> findBy_farmId(Long farmId) {
        //Query query = (Query) entityManager.createQuery("SELECT wol FROM WaterOrderLog wol Left JOIN wol.WaterOrder wol WHERE wo.farmId = :farmId");
        Query query = (Query) entityManager.createNativeQuery("select wol_.id , wol_.created_date_time, wol_.order_status, wol_.water_order_id from water_order_log wol_ inner join water_order wo_ on wol_.water_order_id=wo_.id where wo_.farm_id=?");
        //query.setParameter("farmId", farmId);
        query.setParameter(1, farmId);
        System.out.println(query.getParameter(1).toString());
        List<WaterOrderLog> resultList = query.getResultList();
        System.out.println("JPA " + resultList.size());


        return resultList;
    }

    public void test() throws InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        WaterOrder waterOrder = new WaterOrder();
        waterOrder = new WaterOrder( null, 1111L,now().plusSeconds(10).format(formatter), 10L, Status.Requested);

        WaterOrder waterOrderSaved =  waterOrderRepository.save(waterOrder);

        WaterOrderLog waterOrderLog = new WaterOrderLog();
        waterOrderLog = new WaterOrderLog(null, waterOrder, waterOrder.getOrderStatus(), parse(now().format(formatter),formatter) );

        WaterOrderLog waterOrderLogSaved = waterOrderLogRepository.save(waterOrderLog);

        assertNotNull(waterOrderLogSaved);
        assertEquals(Status.Requested, waterOrderLogSaved.getOrderStatus());
        assertEquals(1, waterOrderLogRepository.findBy_farmId(1111L).size());

        Thread.sleep(2000);

        waterOrderSaved.setOrderStatus(Status.Started);
        waterOrderSaved =  waterOrderRepository.save(waterOrder);

        assertEquals(Status.Started, waterOrderSaved.getOrderStatus());


        WaterOrderLog waterOrderLog1 = new WaterOrderLog(null, waterOrderSaved, waterOrderSaved.getOrderStatus(), parse(now().format(formatter),formatter) );

        waterOrderLogSaved = waterOrderLogRepository.save(waterOrderLog1);
        assertEquals(2, waterOrderLogRepository.findBy_farmId(1111L).size());

    }
}
