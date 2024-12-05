package com.wellan.Construction_Management_System.repository;

import com.wellan.Construction_Management_System.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface SiteRepository extends JpaRepository<Site,Integer> {
    public boolean existsByLatitudeAndLongitudeAndIdNot(BigDecimal latitude,BigDecimal longitude,int id);
}
