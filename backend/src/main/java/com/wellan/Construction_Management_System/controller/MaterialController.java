package com.wellan.Construction_Management_System.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/materials")
public class MaterialController {
    @GetMapping("/")
    public String getAllMaterial(){
        return "Materials List";
    }
}
