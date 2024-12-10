package com.wellan.Construction_Management_System.entity;

import com.wellan.Construction_Management_System.entity.baseEntity.BaseCreatedDateBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

/**
 * SiteMaterial 類別用於記錄工地與原物料之間的消耗關係。
 * 包含工地、原物料、消耗量及日期等信息。
 */
@Entity
@Table(name = "site_material")
public class SiteMaterial extends BaseCreatedDateBean {

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
    @Min(value = 0,message = "消耗量不得為負數")
    @Column(name = "consumption", nullable = false)
    private float consumption;

//    daily_consumption：當前生效的每日消耗量。
//    pending_daily_consumption：待生效的每日消耗量。
//    effective_date：待生效的日期。
    /**
     * 消耗類型，可分為單次與日常消耗
     */
    @Column(name = "consume_type",nullable = false)
    private ConsumeType consumeType;
    /**
     * 消耗發生的日期
     */
    @Column(name = "date", nullable = false)
    private Timestamp date;
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
     * @param consumption  消耗量，必須大於 0。
     * @param date     消耗日期，不可為 null。
     * @throws IllegalArgumentException 如果參數不符合要求。
     */
    public SiteMaterial(Site site, Material material, float consumption, Timestamp date) {
        if (site == null) {
            throw new IllegalArgumentException("工地信息不可為空");
        }
        if (material == null) {
            throw new IllegalArgumentException("原物料不可為空");
        }
        if (consumption <= 0) {
            throw new IllegalArgumentException("消耗量必須大於 0");
        }
        if (date == null) {
            throw new IllegalArgumentException("日期不可為空");
        }
        this.site = site;
        this.material = material;
        this.consumption = consumption;
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

    public float getConsumption() {
        return consumption;
    }

    /**
     * 設置消耗量。
     *
     * @param quantity 消耗量，必須大於 0。
     * @throws IllegalArgumentException 如果消耗量小於等於 0。
     */
    public void setConsumption(float quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("消耗量必須大於 0");
        }
        this.consumption = quantity;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = Objects.requireNonNull(date, "日期不可為空");
    }

    @Override
    public String toString() {
        return "SiteMaterial{" +
                "id=" + id +
                ", site=" + site +
                ", material=" + material +
                ", quantity=" + consumption +
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