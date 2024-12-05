package com.wellan.Construction_Management_System.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name",nullable = false)
    private String materialName;
    @Enumerated(EnumType.STRING)
    @Column(name = "unit",nullable = false)
    private MaterialUnit unit;
    @Column(name = "stock",nullable = false,columnDefinition = "int DEFAULT 0")
    private int stock;
    @Column(name="alert_number" ,nullable = false,columnDefinition = "int DEFAULT 0")
    private int alertNumber;

    public Material() {
    }

    public Material(String materialName, MaterialUnit unit, int stock, int alertNumber) {
        this.materialName = materialName;
        this.unit = unit;
        this.stock = stock;
        this.alertNumber = alertNumber;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public MaterialUnit getUnit() {
        return unit;
    }

    public void setUnit(MaterialUnit unit) {
        this.unit = unit;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getAlertNumber() {
        return alertNumber;
    }

    public void setAlertNumber(int alertNumber) {
        this.alertNumber = alertNumber;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", materialName='" + materialName + '\'' +
                ", unit=" + unit.getFriendlyName() +
                ", stock=" + stock +
                ", alertNumber=" + alertNumber +
                '}';
    }
}
