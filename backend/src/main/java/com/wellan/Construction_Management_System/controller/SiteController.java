package com.wellan.Construction_Management_System.controller;

import com.wellan.Construction_Management_System.entity.Site;
import com.wellan.Construction_Management_System.service.SiteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sites")
public class SiteController {

    private final SiteService siteService;

    @Autowired
    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    //CREATE
    @PostMapping
    public ResponseEntity<Site> addSite(@Valid @RequestBody Site site){
        Site addedSite = siteService.addSite(site);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedSite);
    }
    //READ
    @GetMapping
    public ResponseEntity<List<Site>> findAllSite(){
        List<Site> allSite = siteService.getAllSite();
        return ResponseEntity.ok(allSite);
    }
    @GetMapping("/{sId}")
    public ResponseEntity<Site> findSiteById(@PathVariable int sId){
        Site site = siteService.getSiteById(sId);
        return ResponseEntity.ok(site);
    }
    //UPDATE
    @PutMapping("/{sId}")
    public ResponseEntity<Site> updateSiteById(@PathVariable int sId,
                                               @Valid @RequestBody Site updateSite){
        Site site = siteService.updateSiteById(sId, updateSite);
        return ResponseEntity.ok(site);
    }
    @DeleteMapping("/{sId}")
    public ResponseEntity<Void> deleteSiteById(@PathVariable int sId){
        siteService.deleteSite(sId);
        return ResponseEntity.noContent().build();
    }

}
