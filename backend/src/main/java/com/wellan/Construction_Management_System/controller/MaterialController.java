package com.wellan.Construction_Management_System.controller;

import com.wellan.Construction_Management_System.entity.Material;
import com.wellan.Construction_Management_System.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/materials")
public class MaterialController {

    private final  MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService=materialService;
    }
    //獲取所有原物料資料
    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterial(){
        List<Material> materialList = materialService.getAll();

        return  ResponseEntity.ok(materialList);
    }
    //創建新原物料
    @PostMapping
    public  ResponseEntity<Material> addMaterial(@Valid  @RequestBody Material newMaterial){
        Material createMaterial = materialService.addMaterial(newMaterial);
        return ResponseEntity.status(HttpStatus.CREATED).body(createMaterial);
    }
    //根據id獲取對應原物料資料
    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Integer id){
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }
    //根據id更新對應原物料
    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterialById(@PathVariable Integer id,@Valid @RequestBody Material updateMaterial){
        return ResponseEntity.ok(materialService.updateMaterialById(id,updateMaterial));
    }
    //刪除對應id的原物料
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterialById(@PathVariable Integer id){
        materialService.deleteMaterialById(id);
        return ResponseEntity.noContent().build();
    }

}
