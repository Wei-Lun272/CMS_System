package com.wellan.Construction_Management_System.repository;

import com.wellan.Construction_Management_System.entity.Material;
import com.wellan.Construction_Management_System.entity.Site;
import com.wellan.Construction_Management_System.entity.SiteMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiteMaterialRepository extends JpaRepository<SiteMaterial,Integer> {
    Optional<SiteMaterial> findBySiteAndMaterial(Site site, Material material);
    List<SiteMaterial> findBySite(Site site);
    Boolean findByMaterial(Material material);
}