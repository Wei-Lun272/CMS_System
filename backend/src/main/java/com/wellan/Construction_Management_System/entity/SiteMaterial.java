package com.wellan.Construction_Management_System.entity;

import com.wellan.Construction_Management_System.entity.baseEntity.BaseCreatedDateBean;
import com.wellan.Construction_Management_System.entity.baseEntity.BaseFullDateBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * SiteMaterial 類別用於記錄工地與原物料之間的庫存與警戒。
 * 包含工地、原物料、庫存、警戒值及修改日期等信息。
 */
@Entity
@Table(name = "site_material")
public class SiteMaterial extends BaseFullDateBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 關聯的工地
     */
    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    /**
     * 關聯的原物料
     */
    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    /**
     * 消耗量
     */
    @Min(value = 0,message = "庫存不得為負數")
    @Column(name = "stock", nullable = false)
    private float stock;



    /**
     * 對應的警戒值
     */
    @Min(value = 0,message = "警戒值不得為負數")
    @Column(name = "alert",nullable = false)
    private float alert;
    /**
     * 紀錄創建日期，由JPA審計功能維護
     */


    /**
     * 預設構造方法（供 JPA 使用）
     */
    protected SiteMaterial() {
    }

    /**
     * 建構方法，初始化 SiteMaterial 物件。
     *
     * @param site     工地信息，不可為 null。
     * @param material 原物料信息，不可為 null。
     * @param stock  庫存，必須大於 0。
     * @param alert     警戒值，必須大於 0。
     * @throws IllegalArgumentException 如果參數不符合要求。
     */
    public SiteMaterial(Site site, Material material, Float stock, Float alert) {
        // 驗證 site 不能為 null
        if (site == null) {
            throw new IllegalArgumentException("工地 (site) 不能為空。");
        }

        // 驗證 material 不能為 null
        if (material == null) {
            throw new IllegalArgumentException("原物料 (material) 不能為空。");
        }

        // 驗證 stock (庫存) 不能為 null 且不能為負數
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("庫存 (stock) 必須是非負數且不能為空。");
        }

        // 驗證 alert (警戒量) 不能為 null 且不能為負數
        if (alert == null || alert < 0) {
            throw new IllegalArgumentException("警戒量 (alert) 必須是非負數且不能為空。");
        }

        // 警戒量不能大於庫存量
        if (alert > stock) {
            throw new IllegalArgumentException("警戒量 (alert) 不能大於庫存量 (stock)。");
        }

        this.site = site;
        this.material = material;
        this.stock = stock;
        this.alert = alert;
    }

    public Integer getId() {
        return id;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Min(value = 0, message = "庫存不得為負數")
    public float getStock() {
        return stock;
    }

    public void setStock(@Min(value = 0, message = "庫存不得為負數") float stock) {
        this.stock = stock;
    }

    @Min(value = 0, message = "警戒值不得為負數")
    public float getAlert() {
        return alert;
    }

    public void setAlert(@Min(value = 0, message = "警戒值不得為負數") float alert) {
        this.alert = alert;
    }

    @Override
    public String toString() {
        return "SiteMaterial{" +
                "id=" + id +
                ", site=" + site +
                ", material=" + material +
                ", stock=" + stock +
                ", alert=" + alert +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteMaterial that = (SiteMaterial) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}