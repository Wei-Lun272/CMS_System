package com.wellan.Construction_Management_System.controller;

import com.wellan.Construction_Management_System.entity.Vendor;
import com.wellan.Construction_Management_System.service.VendorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendors")
public class VendorController {
    private final VendorService vendorService;
    private static final Logger logger = LoggerFactory.getLogger(VendorController.class);
    @Autowired
    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }
    //CREATE
    @PostMapping
    public ResponseEntity<Vendor> addVendor(@Valid @RequestBody Vendor vendor){
        logger.info("開始新增廠商：{}",vendor.getName());
        Vendor addedVendor = vendorService.addVendor(vendor);
        logger.info("新增成功，廠商名稱：{},ID：{}",vendor.getName(),vendor.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedVendor);
    }
    //READ
    @GetMapping
    public ResponseEntity<List<Vendor>> findAllVendor(){
        List<Vendor> vendorList = vendorService.getAllVendor();
        return ResponseEntity.ok(vendorList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable int id){
        Vendor vendorById = vendorService.getVendorById(id);
        return ResponseEntity.ok(vendorById);
    }
    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable int id,
                                               @Valid @RequestBody Vendor updatedVendor){
        logger.info("正在更新id:{}的廠商資料",id);
        Vendor updated = vendorService.updateVendorById(id, updatedVendor);
        logger.info("已更新資料。");
        return ResponseEntity.ok(updated);
    }
    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable int id){
        logger.info("預計刪除id為:{}的廠商資料",id);
        vendorService.deleteVendorById(id);
        logger.info("已刪除id為:{}的廠商資料",id);
        return ResponseEntity.noContent().build();
    }
}
