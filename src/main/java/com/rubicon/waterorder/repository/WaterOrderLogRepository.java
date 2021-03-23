package com.rubicon.waterorder.repository;

import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WaterOrderLogRepository extends JpaRepository<WaterOrderLog, Long> {

}
