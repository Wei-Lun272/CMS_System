package com.wellan.Construction_Management_System.repository;

import com.wellan.Construction_Management_System.entity.ConsumeType;
import com.wellan.Construction_Management_System.entity.ConsumptionHistory;
import com.wellan.Construction_Management_System.entity.SiteMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface ConsumptionHistoryRepository extends JpaRepository<ConsumptionHistory,Integer> {
    List<ConsumptionHistory> findBySiteMaterial(SiteMaterial siteMaterial);

    List<ConsumptionHistory> findBySiteMaterialAndConsumeType(SiteMaterial siteMaterial, ConsumeType consumeType);
    List<ConsumptionHistory> findByEffectiveDateAndExpired(Timestamp timestamp,boolean expired);
    List<ConsumptionHistory> findBySiteMaterialAndConsumeTypeAndEffectiveDate(SiteMaterial siteMaterial,ConsumeType consumeType,Timestamp effectiveDate);
    /**
     * 查找指定原物料的未來單次消耗與派發事件。
     *
     * @param siteMaterial   原物料-工地關係
     * @param consumeTypes   消耗類型（單次消耗與派發）
     * @param afterDate      查詢起始日期
     * @return 符合條件的消耗與派發歷史
     */
    List<ConsumptionHistory> findBySiteMaterialAndConsumeTypeInAndEffectiveDateAfter(
            SiteMaterial siteMaterial,
            List<ConsumeType> consumeTypes,
            Timestamp afterDate
    );
}
