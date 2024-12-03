package com.wellan.Construction_Management_System.repository;

import com.wellan.Construction_Management_System.entity.Material;
import com.wellan.Construction_Management_System.entity.MaterialUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material,Integer> {
    boolean existsByMaterialNameAndUnit(String materialName, MaterialUnit unit);
}
