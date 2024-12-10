package com.wellan.Construction_Management_System.entity;

import com.wellan.Construction_Management_System.entity.baseEntity.BaseFullDateBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Material Entity - 代表建設管理系統中的原物料。
 * 包含原物料的名稱、單位、庫存、警戒數量、每日消耗量，以及與工地的關聯。
 */
@Entity
@Table(name = "materials")
public class Material extends BaseFullDateBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 原物料名稱，必須不可為空。
     */
    @NotBlank(message = "名稱不得為空。")
    @Column(name = "name", nullable = false)
    private String materialName;

    /**
     * 原物料的單位，使用枚舉類型 MaterialUnit。
     */
    @NotNull(message = "請選擇所使用的計量單位。")
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private MaterialUnit unit;

    /**
     * 庫存數量，預設為 0，不可為負數。
     */
    @Min(value = 0,message = "庫存數量不可為負數")
    @Column(name = "stock", nullable = false, columnDefinition = "int DEFAULT 0")
    private int stock;

    /**
     * 警戒數量，當庫存低於此數量時需發出警告，預設為 0，不可為負數。
     */
    @Min(value = 0,message = "警戒數量不可為負數")
    @Column(name = "alert_number", nullable = false, columnDefinition = "int DEFAULT 0")
    private int alertNumber;





    /**
     * 與工地的關聯，表示此原物料在哪些工地中被使用。
     */
    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SiteMaterial> siteMaterials = new ArrayList<>();

    /**
     * 預設建構方法，供 JPA 使用。
     */
    protected Material() {
    }

    /**
     * 帶參建構方法，用於創建 Material 實例。
     *
     * @param materialName    原物料名稱，必須不可為空
     * @param unit            單位，必須不可為空
     * @param stock           庫存數量，必須大於等於 0
     * @param alertNumber     警戒數量，必須大於等於 0
     */
    public Material(String materialName, MaterialUnit unit, int stock, int alertNumber) {
        this.materialName = Objects.requireNonNull(materialName, "原物料名稱不可為空");
        this.unit = Objects.requireNonNull(unit, "單位不可為空");
        if (stock < 0) throw new IllegalArgumentException("庫存量不得為負數");
        if (alertNumber < 0) throw new IllegalArgumentException("警戒數量不得為負數");

        this.stock = stock;
        this.alertNumber = alertNumber;
    }



    /**
     * 取得原物料名稱。
     *
     * @return 原物料名稱
     */
    public String getMaterialName() {
        return materialName;
    }

    /**
     * 設定原物料名稱。
     *
     * @param materialName 原物料名稱，必須不可為空
     */
    public void setMaterialName(String materialName) {
        this.materialName = Objects.requireNonNull(materialName, "原物料名稱不可為空");
    }

    /**
     * 取得原物料單位。
     *
     * @return 單位
     */
    public MaterialUnit getUnit() {
        return unit;
    }

    /**
     * 設定原物料單位。
     *
     * @param unit 單位，必須不可為空
     */
    public void setUnit(MaterialUnit unit) {
        this.unit = Objects.requireNonNull(unit, "單位不可為空");
    }

    /**
     * 取得庫存數量。
     *
     * @return 庫存數量
     */
    public int getStock() {
        return stock;
    }

    /**
     * 設定庫存數量。
     *
     * @param stock 庫存數量，必須大於等於 0
     */
    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("庫存量不得為負數");
        this.stock = stock;
    }

    /**
     * 取得警戒數量。
     *
     * @return 警戒數量
     */
    public int getAlertNumber() {
        return alertNumber;
    }

    /**
     * 設定警戒數量。
     *
     * @param alertNumber 警戒數量，必須大於等於 0
     */
    public void setAlertNumber(int alertNumber) {
        if (alertNumber < 0) throw new IllegalArgumentException("警戒數量不得為負數");
        this.alertNumber = alertNumber;
    }



    /**
     * 取得與工地的關聯。
     *
     * @return 與工地的關聯列表
     */
    public List<SiteMaterial> getSiteMaterials() {
        return siteMaterials;
    }

    /**
     * 設定與工地的關聯。
     *
     * @param siteMaterials 與工地的關聯列表
     */
    public void setSiteMaterials(List<SiteMaterial> siteMaterials) {
        this.siteMaterials = siteMaterials;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", materialName='" + materialName + '\'' +
                ", unit=" + unit +
                ", stock=" + stock +
                ", alertNumber=" + alertNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return id != null && id.equals(material.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
