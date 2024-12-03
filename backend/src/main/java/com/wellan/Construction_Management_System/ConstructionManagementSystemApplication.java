package com.wellan.Construction_Management_System;

import com.wellan.Construction_Management_System.entity.Material;
import com.wellan.Construction_Management_System.entity.MaterialUnit;
import com.wellan.Construction_Management_System.service.MaterialService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ConstructionManagementSystemApplication implements CommandLineRunner {
	private final  MaterialService materialService;

	public ConstructionManagementSystemApplication(MaterialService materialService) {
		this.materialService = materialService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ConstructionManagementSystemApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		materialService.deleteMaterialById(2);
		String materialName = "橡膠";
		Material newMaterial = new Material(materialName, MaterialUnit.TON, 10, 5);
		Material newMaterial2 = new Material("水泥", MaterialUnit.BAG, 100, 10);
		materialService.addMaterial(newMaterial);
		materialService.addMaterial(newMaterial2);
		System.out.println("All Materials:");
		materialService.getAll().forEach(System.out::println);
	}
}
