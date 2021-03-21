package com.rubicon.waterorder.repository;

import com.rubicon.waterorder.model.WaterOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WaterOrderRepository extends JpaRepository<WaterOrder, Long> {

    List<WaterOrder> findAllByStartDateTimeBetweenOrderByStartDateTimeDesc(
            LocalDateTime dateTimeStart,
            LocalDateTime  dateTimeEnd);

    List<WaterOrder> findAllByStartDateTimeBetweenOrderByStartDateTimeAsc(
            LocalDateTime dateTimeStart,
            LocalDateTime  dateTimeEnd);
}
