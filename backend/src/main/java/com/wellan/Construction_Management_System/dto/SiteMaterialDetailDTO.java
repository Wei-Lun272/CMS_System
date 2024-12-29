package com.wellan.Construction_Management_System.dto;

import com.wellan.Construction_Management_System.entity.ConsumptionHistory;
import com.wellan.Construction_Management_System.entity.SiteMaterial;

import java.util.List;

public class SiteMaterialDetailDTO {
        private Integer siteMaterialId;
        private Integer materialId;
        private Float stock;
        private Float alert;
        private List<ConsumptionHistoryDTO> consumptionHistory;

        // Getters, Setters, Constructors


    protected SiteMaterialDetailDTO() {
    }

    private SiteMaterialDetailDTO(Integer siteMaterialId, Integer materialId, Float stock, Float alert, List<ConsumptionHistoryDTO> consumptionHistory) {
        this.siteMaterialId = siteMaterialId;
        this.materialId = materialId;
        this.stock = stock;
        this.alert = alert;
        this.consumptionHistory = consumptionHistory;
    }
    public static SiteMaterialDetailDTO createFromSiteMaterial(SiteMaterial siteMaterial){
        return new SiteMaterialDetailDTO(
                siteMaterial.getId(),
                siteMaterial.getMaterial().getId(),
                siteMaterial.getStock(),
                siteMaterial.getAlert(),
                siteMaterial.getConsumptionHistories().stream()
                        .map(ConsumptionHistoryDTO::fromConsumptionHistory) // 將每個 ConsumptionHistory 轉換為 DTO
                        .toList() // 收集為一個列表
                );
    }

    public Integer getSiteMaterialId() {
        return siteMaterialId;
    }

    public void setSiteMaterialId(Integer siteMaterialId) {
        this.siteMaterialId = siteMaterialId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Float getStock() {
        return stock;
    }

    public void setStock(Float stock) {
        this.stock = stock;
    }

    public Float getAlert() {
        return alert;
    }

    public void setAlert(Float alert) {
        this.alert = alert;
    }

    public List<ConsumptionHistoryDTO> getConsumptionHistory() {
        return consumptionHistory;
    }

    public void setConsumptionHistory(List<ConsumptionHistoryDTO> consumptionHistory) {
        this.consumptionHistory = consumptionHistory;
    }
}
