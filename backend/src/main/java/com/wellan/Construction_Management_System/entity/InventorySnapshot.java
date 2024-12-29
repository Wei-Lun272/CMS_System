package com.wellan.Construction_Management_System.entity;

import com.wellan.Construction_Management_System.entity.baseEntity.BaseFullDateBean;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * InventorySnapshot 實體類 - 用於記錄每次庫存變動後的快照。
 */
@Entity
@Table(name = "inventory_snapshot")
public class InventorySnapshot  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "site_material_id", nullable = false)
    private SiteMaterial siteMaterial;

    @Column(name = "stock_after", nullable = false)
    private float stockAfter;

    @Column(name = "change_amount", nullable = false)
    private float changeAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false)
    private ConsumeType changeType;

    @Column(name = "change_date", nullable = false)
    private Timestamp changeDate;

    @Column(name = "description")
    private String description;

    protected InventorySnapshot() {}

    public InventorySnapshot(SiteMaterial siteMaterial, float stockAfter, float changeAmount,
                             ConsumeType changeType, Timestamp changeDate, String description) {
        this.siteMaterial = Objects.requireNonNull(siteMaterial, "SiteMaterial 不能為空");
        this.stockAfter = stockAfter;
        this.changeAmount = changeAmount;
        this.changeType = Objects.requireNonNull(changeType, "變動類型不能為空");
        this.changeDate = Objects.requireNonNull(changeDate, "變動日期不能為空");
        this.description = description;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public SiteMaterial getSiteMaterial() {
        return siteMaterial;
    }

    public void setSiteMaterial(SiteMaterial siteMaterial) {
        this.siteMaterial = siteMaterial;
    }

    public float getStockAfter() {
        return stockAfter;
    }

    public void setStockAfter(float stockAfter) {
        this.stockAfter = stockAfter;
    }

    public float getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(float changeAmount) {
        this.changeAmount = changeAmount;
    }

    public ConsumeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ConsumeType changeType) {
        this.changeType = changeType;
    }

    public Timestamp getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Timestamp changeDate) {
        this.changeDate = changeDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventorySnapshot that = (InventorySnapshot) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "InventorySnapshot{" +
                "id=" + id +
                ", siteMaterial=" + siteMaterial +
                ", stockAfter=" + stockAfter +
                ", changeAmount=" + changeAmount +
                ", changeType=" + changeType +
                ", changeDate=" + changeDate +
                ", description='" + description + '\'' +
                '}';
    }
}
