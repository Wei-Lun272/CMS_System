package com.wellan.Construction_Management_System.service;

import com.wellan.Construction_Management_System.entity.Material;
import com.wellan.Construction_Management_System.repository.MaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {
    private final MaterialRepository materialRepository;

    private static  final Logger logger = LoggerFactory.getLogger(MaterialService.class);
    @Autowired
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }
    public Material addMaterial(Material newMaterial){
        if(materialRepository.existsByMaterialNameAndUnit(newMaterial.getMaterialName(),newMaterial.getUnit())){
             throw  new ResponseStatusException(HttpStatus.CONFLICT,"相同名稱和單位的原物料已經存在，無法創建");
        }
        return materialRepository.save(newMaterial);
    }
    //按照id獲取對應Material(Optional)
    public Material getMaterialById(int id) {
        Optional<Material> material = materialRepository.findById(id);
        if (material.isPresent()) {
            return material.get();
        } else {
            logger.warn("Material with ID " + id + " does not exist.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Material with ID " + id + " does not exist.");
        }
    }

    // 按照 id 更新對應的 Material
    public Material updateMaterialById(int id, Material updatedMaterial) {
        // 確認是否存在對應的原物料
        Material originMaterial = materialRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Material with ID " + id + " does not exist.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Material with ID " + id + " does not exist.");
                });

        // 確認是否已存在相同名稱和單位的原物料
        if (materialRepository.existsByMaterialNameAndUnit(updatedMaterial.getMaterialName(), updatedMaterial.getUnit())) {
            logger.warn("Material with name '{}' and unit '{}' already exists. Update failed.",
                    updatedMaterial.getMaterialName(), updatedMaterial.getUnit());
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Material with name '" + updatedMaterial.getMaterialName() + "' and unit '" + updatedMaterial.getUnit() + "' already exists.");
        }

        // 更新資料
        originMaterial.setMaterialName(updatedMaterial.getMaterialName());
        originMaterial.setStock(updatedMaterial.getStock());
        originMaterial.setUnit(updatedMaterial.getUnit());
        originMaterial.setAlertNumber(updatedMaterial.getAlertNumber());

        // 保存更新後的資料
        Material savedMaterial = materialRepository.save(originMaterial);
        logger.info("Material with ID " + id + " has been successfully updated.");

        return savedMaterial;
    }

    public List<Material> getAll(){
        return  materialRepository.findAll();
    }
    // 按照 ID 刪除 Material
    public void deleteMaterialById(int id) {
        if (materialRepository.existsById(id)) {
            materialRepository.deleteById(id);
            logger.info("Material with ID " + id + " has been deleted.");
        } else {
            logger.warn("Material with ID " + id + " does not exist. Delete failed.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Material with ID " + id + " does not exist.");
        }
    }

}
