package com.wellan.Construction_Management_System.service;

import com.wellan.Construction_Management_System.ConstructionManagementSystemApplication;
import com.wellan.Construction_Management_System.entity.Site;
import com.wellan.Construction_Management_System.repository.SiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteService {
    @Autowired
    private final SiteRepository siteRepository;

    private static final Logger logger = LoggerFactory.getLogger(SiteService.class);


    public SiteService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }
    public void addSite(Site newSite){
        siteRepository.save(newSite);
        logger.info("已新增新工地資訊:"+newSite.getSiteName());
    }
}
