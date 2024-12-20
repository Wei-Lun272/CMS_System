package com.wellan.Construction_Management_System.entity;

import com.wellan.Construction_Management_System.entity.baseEntity.BaseCreatedDateBean;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "consumption_history")
public class ConsumptionHistory extends BaseCreatedDateBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "site_material_id",nullable = false)
    private SiteMaterial siteMaterial;

    @Column(name = "amount")
    private Float amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "consumption_type")
    private ConsumeType consumeType;

    @Column(name = "consumption_date")
    private Timestamp consumptionDate;

    protected ConsumptionHistory(){}

    public ConsumptionHistory(SiteMaterial siteMaterial, Float amount, ConsumeType consumeType, Timestamp consumptionDate) {

        // 驗證: consumeType 不能為 null
        if (consumeType == null) {
            throw new IllegalArgumentException("庫存變動類型不能為空。");
        }

        // 驗證: 消耗日期不能為 null
        if (consumptionDate == null) {
            throw new IllegalArgumentException("消耗日期不能為空。");
        }

        // 驗證: amount 不能為空
        if (amount == null) {
            throw new IllegalArgumentException("數量不能為空。");
        }

        // 驗證: amount 根據 consumeType 的要求
        if (consumeType == ConsumeType.DISPATCH) {
            if (amount <= 0) {
                throw new IllegalArgumentException("派發量必須是正數。");
            }
            this.amount = amount; // 正數
        } else {
            if (amount <= 0) {
                throw new IllegalArgumentException("消耗量必須是正數。");
            }
            this.amount = -amount; // 消耗量記為負數
        }

        this.siteMaterial = Objects.requireNonNull(siteMaterial, "SiteMaterial 不能為空。");
        this.consumeType = consumeType;
        this.consumptionDate = consumptionDate;
    }


    public Integer getId() {
        return id;
    }

    public SiteMaterial getSiteMaterial() {
        return siteMaterial;
    }

    public void setSiteMaterial(SiteMaterial siteMaterial) {
        this.siteMaterial = siteMaterial;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public ConsumeType getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(ConsumeType consumeType) {
        this.consumeType = consumeType;
    }

    public Timestamp getConsumptionDate() {
        return consumptionDate;
    }

    public void setConsumptionDate(Timestamp consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConsumptionHistory that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(siteMaterial, that.siteMaterial) && Objects.equals(amount, that.amount) && consumeType == that.consumeType && Objects.equals(consumptionDate, that.consumptionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, consumeType, consumptionDate);
    }

    @Override
    public String toString() {
        return "ConsumptionHistory{" +
                "id=" + id +
                ", amount=" + amount +
                ", consumeType=" + consumeType +
                ", consumptionDate=" + consumptionDate +
                '}';
    }
}
