package com.wellan.Construction_Management_System;

import com.wellan.Construction_Management_System.entity.Material;
import com.wellan.Construction_Management_System.entity.MaterialUnit;
import com.wellan.Construction_Management_System.entity.Site;
import com.wellan.Construction_Management_System.entity.SiteStatus;
import com.wellan.Construction_Management_System.service.MaterialService;
import com.wellan.Construction_Management_System.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;

@EnableJpaAuditing
@SpringBootApplication
public class ConstructionManagementSystemApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(ConstructionManagementSystemApplication.class);
	private final SiteService siteService;

	public ConstructionManagementSystemApplication(SiteService siteService) {
		this.siteService = siteService;
		logger.info("程式已成功運行");
	}

	public static void main(String[] args) {
		SpringApplication.run(ConstructionManagementSystemApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
//		materialService.deleteMaterialById(2);
//		String materialName = "橡膠";
//		Material newMaterial = new Material(materialName, MaterialUnit.TON, 10, 5);
//		Material newMaterial2 = new Material("水泥", MaterialUnit.BAG, 100, 10);
//		materialService.addMaterial(newMaterial);
//		materialService.addMaterial(newMaterial2);
//		System.out.println("All Materials:");
//		materialService.getAll().forEach(System.out::println);
//		Site newSite = new Site("台中捷運","台中市市政府", SiteStatus.PLANNING,"準備拿下");
//		Site newSite2 = new  Site("台北萬華豪宅區",new BigDecimal(61.90649542),new BigDecimal(-82.00716074),"凱達格蘭大道87號",SiteStatus.CANCELLED,"太貴了放棄");
//		siteService.addSite(newSite);
//		siteService.addSite(newSite2);
//		logger.info("試運行創建兩個工地成功");
	}
}
