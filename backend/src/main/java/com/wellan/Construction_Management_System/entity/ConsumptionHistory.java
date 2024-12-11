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
    @JoinColumn(name = "site_material_id")
    private Integer siteMaterialId;

    @Column(name = "amount")
    private Float amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "consumption_type")
    private ConsumeType consumeType;

    @Column(name = "consumption_date")
    private Timestamp consumptionDate;
    protected ConsumptionHistory(){}
    public ConsumptionHistory(Integer siteMaterialId, Float amount, ConsumeType consumeType, Timestamp consumptionDate) {
        if (siteMaterialId == null || siteMaterialId < 0) {
            throw new IllegalArgumentException("工地原物料 ID 必須是非負整數且不能為空。");
        }
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("消耗量必須是正數且不能為空。");
        }
        if (consumeType == null) {
            throw new IllegalArgumentException("消耗類型不能為空。");
        }
        if (consumptionDate == null) {
            throw new IllegalArgumentException("消耗日期不能為空。");
        }
        if (consumptionDate.after(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("消耗日期不能是未來日期。");
        }

        this.siteMaterialId = siteMaterialId;
        this.amount = amount;
        this.consumeType = consumeType;
        this.consumptionDate = consumptionDate;
    }


    public Integer getId() {
        return id;
    }

    public Integer getSiteMaterialId() {
        return siteMaterialId;
    }

    public void setSiteMaterialId(Integer siteMaterialId) {
        this.siteMaterialId = siteMaterialId;
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
        return Objects.equals(id, that.id) && Objects.equals(siteMaterialId, that.siteMaterialId) && Objects.equals(amount, that.amount) && consumeType == that.consumeType && Objects.equals(consumptionDate, that.consumptionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, siteMaterialId, amount, consumeType, consumptionDate);
    }

    @Override
    public String toString() {
        return "ConsumptionHistory{" +
                "id=" + id +
                ", siteMaterialId=" + siteMaterialId +
                ", amount=" + amount +
                ", consumeType=" + consumeType +
                ", consumptionDate=" + consumptionDate +
                '}';
    }
}
