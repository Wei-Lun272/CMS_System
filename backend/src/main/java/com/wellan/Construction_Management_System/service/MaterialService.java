package com.wellan.Construction_Management_System.service;

import com.wellan.Construction_Management_System.entity.Material;
import com.wellan.Construction_Management_System.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {
    @Autowired
    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }
    public void addMaterial(Material newMaterial){
        if(!materialRepository.existsByMaterialNameAndUnit(newMaterial.getMaterialName(),newMaterial.getUnit())){
            materialRepository.save(newMaterial);
        }
    }
    public List<Material> getAll(){
        return  materialRepository.findAll();
    }
    public void deleteMaterialById(int id) {
        if (materialRepository.existsById(id)) {
            materialRepository.deleteById(id);
            System.out.println("Material with ID " + id + " has been deleted.");
        } else {
            System.out.println("Material with ID " + id + " does not exist.");
        }
    }

}
