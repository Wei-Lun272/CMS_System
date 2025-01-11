package com.wellan.Construction_Management_System.dto;

import com.wellan.Construction_Management_System.entity.ConsumptionHistory;
import com.wellan.Construction_Management_System.entity.MaterialUnit;
import com.wellan.Construction_Management_System.entity.SiteMaterial;

import java.util.List;

public class SiteMaterialDetailDTO {
        private Integer siteMaterialId;
        private Integer materialId;
        private String materialName;
        private String materialUnit;
        private Float stock;
        private Float alert;
        private List<ConsumptionHistoryDTO> consumptionHistory;

        // Getters, Setters, Constructors


    protected SiteMaterialDetailDTO() {
    }

    private SiteMaterialDetailDTO(Integer siteMaterialId, Integer materialId, String materialName,String materialUnit,Float stock, Float alert, List<ConsumptionHistoryDTO> consumptionHistory) {
        this.siteMaterialId = siteMaterialId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.materialUnit =materialUnit;
        this.stock = stock;
        this.alert = alert;
        this.consumptionHistory = consumptionHistory;
    }
    /**
     * 從 SiteMaterial 生成對應的 SiteMaterialDetailDTO。
     *
     * @param siteMaterial 原物料與工地的關係物件
     * @return 對應的 SiteMaterialDetailDTO
     */
    public static SiteMaterialDetailDTO createFromSiteMaterial(SiteMaterial siteMaterial) {
        // 使用 stream 進行排序並生成 DTO
        List<ConsumptionHistoryDTO> sortedConsumptionHistories = siteMaterial.getConsumptionHistories().stream()
                .sorted((history1, history2) -> history2.getEffectiveDate().compareTo(history1.getEffectiveDate())) // 按生效日期降冪排序
                .map(ConsumptionHistoryDTO::fromConsumptionHistory) // 將每個 ConsumptionHistory 轉為 DTO
                .toList(); // 收集結果為列表

        return new SiteMaterialDetailDTO(
                siteMaterial.getId(),
                siteMaterial.getMaterial().getId(),
                siteMaterial.getMaterial().getMaterialName(),
                siteMaterial.getMaterial().getUnit().getFriendlyName(),
                siteMaterial.getStock(),
                siteMaterial.getAlert(),
                sortedConsumptionHistories
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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
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
