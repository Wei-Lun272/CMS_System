package com.wellan.Construction_Management_System.service;

import com.wellan.Construction_Management_System.entity.Vendor;
import com.wellan.Construction_Management_System.repository.VendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VendorService {
    private static final Logger logger = LoggerFactory.getLogger(VendorService.class);

    private VendorRepository vendorRepository;
    @Autowired
    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }
    //create
    public Vendor addVendor(Vendor vendor){
        Vendor newVendor = vendorRepository.save(vendor);
        logger.info("已新增廠商：名稱 - {}，ID - {}", newVendor.getName(), newVendor.getId());
        return newVendor;
    }
    public Vendor updateVendorById(int id,Vendor updateVendor){
        //先確認是否有對應id的廠商存在，否則拋出錯誤
        Vendor vendor = vendorRepository.findById(id).orElseThrow(
                () -> {
                    logger.warn("此id：{}對應的廠商並不存在", id);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "此id:" + id + "對應的廠商不存在");
                }
        );
        vendor.setName(updateVendor.getName());
        vendor.setContactInfo(updateVendor.getContactInfo());
        Vendor saved = vendorRepository.save(vendor);
        logger.info("以更新id為:{} 的廠商資訊",id);
        return saved;
    }
    public Vendor getVendorById(int id){
        Vendor vendor = vendorRepository.findById(id).orElseThrow(
                () -> {
                    logger.warn("此id：{}對應的廠商並不存在", id);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "此id:" + id + "對應的廠商不存在");

                }
        );
        return vendor;
    }
    public void deleteVendorById(int id){
        if(vendorRepository.existsById(id)){
            vendorRepository.deleteById(id);
            logger.info("廠商已刪除");
        }else {
            logger.warn("此id：{}對應的廠商並不存在", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"此id:" + id + "對應的廠商不存在");
        }
    }
}
