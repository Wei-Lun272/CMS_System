package com.wellan.Construction_Management_System.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wellan.Construction_Management_System.dto.AlertPredictionDTO;
import com.wellan.Construction_Management_System.dto.SingleConsumeDTO;
import com.wellan.Construction_Management_System.dto.SingleDispatchDTO;
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
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SiteService siteService;

    @MockBean
    private StockOperationService stockOperationService;

    @Test
    void testAddSite() throws Exception {
        Site site = new Site("工地A", "地址", SiteStatus.ONGOING, "新建案");
        when(siteService.addSite(any(Site.class))).thenReturn(site);

        mockMvc.perform(post("/sites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(site)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.siteName").value("工地A"));
    }

    @Test
    void testGetAllSites() throws Exception {
        when(siteService.getAllSite()).thenReturn(List.of(new Site("工地A", "地址", SiteStatus.ONGOING, "描述")));

        mockMvc.perform(get("/sites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].siteName").value("工地A"));
    }

    @Test
    void testGetSiteById() throws Exception {
        when(siteService.getSiteById(1)).thenReturn(new Site("工地B", "地址B", SiteStatus.PLANNING, "說明"));

        mockMvc.perform(get("/sites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.siteName").value("工地B"));
    }

    @Test
    void testGetSiteDetailById() throws Exception {
        Site site = new Site("工地C", "地址C", SiteStatus.ONGOING, "備註");
        List<SiteMaterial> materials = List.of();
        when(siteService.getSiteById(1)).thenReturn(site);
        when(stockOperationService.getMaterialsFromSite(1)).thenReturn(materials);

        mockMvc.perform(get("/sites/1/detail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.siteName").value("工地C"));
    }

    @Test
    void testUpdateSiteById() throws Exception {
        Site updated = new Site("更新工地", "更新地址", SiteStatus.COMPLETED, "完工說明");
        when(siteService.updateSiteById(eq(1), any(Site.class))).thenReturn(updated);

        mockMvc.perform(put("/sites/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.siteName").value("更新工地"));
    }

    @Test
    void testDeleteSite() throws Exception {
        doNothing().when(siteService).deleteSite(1);

        mockMvc.perform(delete("/sites/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDispatchMaterial() throws Exception {
        List<SingleDispatchDTO> list = List.of(new SingleDispatchDTO());
        when(stockOperationService.batchDispatchMaterials(eq(1), anyList())).thenReturn(1);

        mockMvc.perform(post("/sites/1/dispatch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(list)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void testConsumeMaterial() throws Exception {
        List<SingleConsumeDTO> list = List.of(
                new SingleConsumeDTO(1, 5.0f, ConsumeType.ONCE, new Timestamp(System.currentTimeMillis()))
        );
        when(stockOperationService.batchConsumeMaterials(eq(1), anyList())).thenReturn(1);

        mockMvc.perform(post("/sites/1/consume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(list)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void testGetMaterialsBySite() throws Exception {
        when(stockOperationService.getMaterialsFromSite(1)).thenReturn(List.of());

        mockMvc.perform(get("/sites/1/materials"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSiteAlertStatus() throws Exception {
        AlertPredictionDTO dto = new AlertPredictionDTO(1, "水泥", 50, 20, new HashMap<>(), null);
        when(stockOperationService.predictStockAlert(1)).thenReturn(List.of(dto));

        mockMvc.perform(get("/sites/1/alert"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testGetAlertPredictions() throws Exception {
        when(stockOperationService.predictStockAlert(1)).thenReturn(List.of());

        mockMvc.perform(get("/sites/1/alert-predictions"))
                .andExpect(status().isOk());
    }
}
