package com.wellan.Construction_Management_System.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * SiteMaterial 類別用於記錄工地與原物料之間的消耗關係。
 * 包含工地、原物料、消耗量及日期等信息。
 */
@Entity
@Table(name = "site_material")
public class SiteMaterial {

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
    @Column(name = "quantity", nullable = false)
    private float quantity;

    /**
     * 消耗發生的日期
     */
    @Column(name = "date", nullable = false)
    private LocalDate date;

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
     * @param quantity 消耗量，必須大於 0。
     * @param date     消耗日期，不可為 null。
     * @throws IllegalArgumentException 如果參數不符合要求。
     */
    public SiteMaterial(Site site, Material material, float quantity, LocalDate date) {
        if (site == null) {
            throw new IllegalArgumentException("工地信息不可為空");
        }
        if (material == null) {
            throw new IllegalArgumentException("原物料不可為空");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("消耗量必須大於 0");
        }
        if (date == null) {
            throw new IllegalArgumentException("日期不可為空");
        }
        this.site = site;
        this.material = material;
        this.quantity = quantity;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = Objects.requireNonNull(site, "工地信息不可為空");
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = Objects.requireNonNull(material, "原物料不可為空");
    }

    public float getQuantity() {
        return quantity;
    }

    /**
     * 設置消耗量。
     *
     * @param quantity 消耗量，必須大於 0。
     * @throws IllegalArgumentException 如果消耗量小於等於 0。
     */
    public void setQuantity(float quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("消耗量必須大於 0");
        }
        this.quantity = quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = Objects.requireNonNull(date, "日期不可為空");
    }

    @Override
    public String toString() {
        return "SiteMaterial{" +
                "id=" + id +
                ", site=" + site +
                ", material=" + material +
                ", quantity=" + quantity +
                ", date=" + date +
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