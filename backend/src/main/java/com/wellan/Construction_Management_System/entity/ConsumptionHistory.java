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
