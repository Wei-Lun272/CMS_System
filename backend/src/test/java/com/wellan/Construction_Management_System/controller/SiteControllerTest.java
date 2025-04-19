package com.wellan.Construction_Management_System.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wellan.Construction_Management_System.dto.*;
import com.wellan.Construction_Management_System.entity.ConsumeType;
import com.wellan.Construction_Management_System.entity.Site;
import com.wellan.Construction_Management_System.entity.SiteMaterial;
import com.wellan.Construction_Management_System.entity.SiteStatus;
import com.wellan.Construction_Management_System.service.SiteService;
import com.wellan.Construction_Management_System.service.StockOperationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SiteService siteService;

    @MockBean
    private StockOperationService stockOperationService;

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddSite() throws Exception {
        Site site = new Site("工地A", "台北", SiteStatus.ONGOING, "新工程");
        when(siteService.addSite(any(Site.class))).thenReturn(site);

        mockMvc.perform(post("/sites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(site)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.siteName").value("工地A"));
    }

    @Test
    void testFindAllSites() throws Exception {
        List<Site> siteList = List.of(new Site("A", "地址", SiteStatus.ONGOING, "desc"));
        when(siteService.getAllSite()).thenReturn(siteList);

        mockMvc.perform(get("/sites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].siteName").value("A"));
    }

    @Test
    void testFindSiteById() throws Exception {
        Site site = new Site("B", "地址", SiteStatus.ONGOING, "desc");
        when(siteService.getSiteById(3)).thenReturn(site);

        mockMvc.perform(get("/sites/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.siteName").value("B"));
    }

    @Test
    void testFindSiteDetailById() throws Exception {
        Site site = new Site("C", "地址", SiteStatus.ONGOING, "desc");
        List<SiteMaterial> materials = new ArrayList<>();
        when(siteService.getSiteById(1)).thenReturn(site);
        when(stockOperationService.getMaterialsFromSite(1)).thenReturn(materials);

        mockMvc.perform(get("/sites/1/detail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.siteName").value("C"));
    }

    @Test
    void testUpdateSiteById() throws Exception {
        Site updatedSite = new Site("更新工地", "新地址", SiteStatus.COMPLETED, "更新描述");
        when(siteService.updateSiteById(eq(1), any(Site.class))).thenReturn(updatedSite);

        mockMvc.perform(put("/sites/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedSite)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.siteName").value("更新工地"));
    }

    @Test
    void testDeleteSiteById() throws Exception {
        doNothing().when(siteService).deleteSite(1);

        mockMvc.perform(delete("/sites/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDispatchMaterialToSite() throws Exception {
        List<SingleDispatchDTO> list = List.of(new SingleDispatchDTO());
        when(stockOperationService.batchDispatchMaterials(eq(1), anyList())).thenReturn(1);

        mockMvc.perform(post("/sites/1/dispatch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(list)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void testConsumeMaterialsFromSite() throws Exception {
        List<SingleConsumeDTO> list = List.of(
                new SingleConsumeDTO(1, 10f, ConsumeType.ONCE, new Timestamp(System.currentTimeMillis()))
        );
        when(stockOperationService.batchConsumeMaterials(eq(1), anyList())).thenReturn(1);

        mockMvc.perform(post("/sites/1/consume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(list)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void testGetMaterialById() throws Exception {
        when(stockOperationService.getMaterialsFromSite(1)).thenReturn(List.of());

        mockMvc.perform(get("/sites/1/materials"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSiteAlertStatus() throws Exception {
        Map<Timestamp, Float> dummyMap = new HashMap<>();
        dummyMap.put(new Timestamp(System.currentTimeMillis()), 5.0f);

        AlertPredictionDTO alertDto = new AlertPredictionDTO(
                1, "鋼筋", 100f, 20f, dummyMap, null
        );

        when(stockOperationService.predictStockAlert(1)).thenReturn(List.of(alertDto));

        mockMvc.perform(get("/sites/1/alert"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testGetStockAlertPredictions() throws Exception {
        when(stockOperationService.predictStockAlert(1)).thenReturn(List.of());

        mockMvc.perform(get("/sites/1/alert-predictions"))
                .andExpect(status().isOk());
    }
}
