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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    public boolean createOrder(Long farmId, WaterOrderData waterOrderData) {

        if (!waterOrderValidator.isValidDate(waterOrderData.getStartDateTime())) {
            return false;
        }

        if (!waterOrderValidator.isFutureDate(waterOrderData.getStartDateTime())) {
            return false;
        }

        if (waterOrderValidator.isOrderOverlap(waterOrderData)) {
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
            log.error("Incoming Cancel Request : Water Order Id : [ " + waterOrderData.getId() + " ] is not in Cancelled state.");
            return false;
        }

        //check with db if it's been delivered or cancelled.
        if (waterOrderValidator.isDeliveredOrCancelled(waterOrderData)) {
            log.error("Water Order Id : [ " + waterOrderData.getId() + " ] is in Delivered or Cancelled state.");
            return false;
        }

        WaterOrder waterOrderToCancel = waterOrderMapper.constructWaterOrder(waterOrderData);

        log.info("Water Order Id : [ " + waterOrderData.getId() + " ] is going to be Cancelled.");
        //scheduler to cancel the task
        schedulerService.setApplicationEventPublisher(this.publisher);
        schedulerService.cancelTask(waterOrderToCancel);


        return true;
    }

    public List<WaterOrderLog> findBy_farmId(Long farmId) {

        Query query = (Query) entityManager.createNativeQuery("select wol_.id , wol_.created_date_time, wol_.order_status, wol_.water_order_id from water_order_log wol_ inner join water_order wo_ on wol_.water_order_id=wo_.id where wo_.farm_id=?");
        query.setParameter(1, farmId);
        System.out.println(query.getParameter(1).toString());
        List<WaterOrderLog> resultList = query.getResultList();
        System.out.println("JPA " + resultList.size());

        return resultList;
    }


}
