package com.rubicon.waterorder.repository;

import com.rubicon.waterorder.model.WaterOrderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterOrderLogRepository extends JpaRepository<WaterOrderLog, Long> {

   @Query(value = "select wol_.id , wol_.created_date_time, wol_.order_status, wol_.water_order_id from water_order_log wol_ inner join water_order wo_ on wol_.water_order_id=wo_.id where wo_.farm_id=?",
            nativeQuery = true)
    List<WaterOrderLog> findBy_farmId(Long farmId);

    @Query(value = "select wol_.id , wol_.created_date_time, wol_.order_status, wol_.water_order_id from water_order_log wol_ inner join water_order wo_ on wol_.water_order_id=wo_.id where wo_.farm_id=? and wo_.id=?",
            nativeQuery = true)
    List<WaterOrderLog> findBy_farmId_waterOrderId(Long farmId, Long waterOrderId);

/*   //Not working
    @Query(value = "SELECT wol FROM WaterOrder wo INNER JOIN WaterOrderLog wol WHERE wo.farmId=?1")
    List<WaterOrderLog> findBy_farmId1(Long farmId);*/




}
