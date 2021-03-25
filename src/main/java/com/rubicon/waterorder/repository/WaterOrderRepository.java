package com.rubicon.waterorder.repository;

import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WaterOrderRepository extends JpaRepository<WaterOrder, Long> {


/*   @Query(value = "SELECT wo FROM WaterOrder wo INNER JOIN WaterOrderLog wol WHERE wo.farmId=?1")
    List<WaterOrder> findBy_farmId(Long farmId);*/

     List<WaterOrder> findByIdAndFarmId(long id, long farmId);


    @Query(value = "SELECT wo FROM WaterOrder wo WHERE wo.farmId = ?1 AND wo.startDateTime BETWEEN ?2 AND ?3 ORDER BY wo.startDateTime DESC")
     List<WaterOrder> findByFarmIdAndByStartDateTimeBetweenOrderByStartDateTimeDesc(
            Long farmId,
            LocalDateTime dateTimeStart,
            LocalDateTime dateTimeEnd) ;

    @Query(value = "SELECT wo FROM WaterOrder wo WHERE wo.farmId = ?1 AND wo.startDateTime BETWEEN ?2 AND ?3 ORDER BY wo.startDateTime ASC")
    List<WaterOrder> findByFarmIdAndByStartDateTimeBetweenOrderByStartDateTimeAsc(
            Long farmId,
            LocalDateTime dateTimeStart,
            LocalDateTime  dateTimeEnd);

    List<WaterOrder> findTop2ByOrderByIdDesc();

    List<WaterOrder> findAllByStartDateTimeBetweenOrderByStartDateTimeDesc(
            LocalDateTime dateTimeStart,
            LocalDateTime  dateTimeEnd);

    List<WaterOrder> findAllByStartDateTimeBetweenOrderByStartDateTimeAsc(
            LocalDateTime dateTimeStart,
            LocalDateTime  dateTimeEnd);
}
