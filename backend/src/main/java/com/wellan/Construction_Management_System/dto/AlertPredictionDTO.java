package com.wellan.Construction_Management_System.dto;

import java.sql.Timestamp;
import java.util.Map;

public class AlertPredictionDTO {
    private int materialId;
    private String materialName;
    private float currentStock;
    private float alertValue;
    private Map<Timestamp, Float> dailyConsumptionMap;
    private Timestamp alertDate;

    public AlertPredictionDTO(int materialId, String materialName, float currentStock, float alertValue, Map<Timestamp, Float> dailyConsumptionMap, Timestamp alertDate) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.currentStock = currentStock;
        this.alertValue = alertValue;
        this.dailyConsumptionMap = dailyConsumptionMap;
        this.alertDate = alertDate;
    }

    // Getters and Setters省略

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public float getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(float currentStock) {
        this.currentStock = currentStock;
    }

    public float getAlertValue() {
        return alertValue;
    }

    public void setAlertValue(float alertValue) {
        this.alertValue = alertValue;
    }

    public Map<Timestamp, Float> getDailyConsumptionMap() {
        return dailyConsumptionMap;
    }

    public void setDailyConsumptionMap(Map<Timestamp, Float> dailyConsumptionMap) {
        this.dailyConsumptionMap = dailyConsumptionMap;
    }

    public Timestamp getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Timestamp alertDate) {
        this.alertDate = alertDate;
    }
}
