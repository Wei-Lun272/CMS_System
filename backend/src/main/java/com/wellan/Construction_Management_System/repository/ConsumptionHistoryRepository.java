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
}
