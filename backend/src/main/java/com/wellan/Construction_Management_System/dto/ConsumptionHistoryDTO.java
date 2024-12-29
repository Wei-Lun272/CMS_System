package com.wellan.Construction_Management_System.dto;

import com.wellan.Construction_Management_System.entity.ConsumeType;
import com.wellan.Construction_Management_System.entity.ConsumptionHistory;


import java.sql.Timestamp;

public class ConsumptionHistoryDTO {

    private Integer historyId;
    private Float amount;
    private ConsumeType consumeType;
    private Timestamp effectiveDate;
    private Boolean expired;
    private Timestamp createdDate;

    protected ConsumptionHistoryDTO() {
    }

    private ConsumptionHistoryDTO(Integer historyId, Float amount, ConsumeType consumeType, Timestamp effectiveDate, Boolean expired, Timestamp createdDate) {
        this.historyId = historyId;
        this.amount = amount;
        this.consumeType = consumeType;
        this.effectiveDate = effectiveDate;
        this.expired = expired;
        this.createdDate = createdDate;
    }

    public static ConsumptionHistoryDTO fromConsumptionHistory(ConsumptionHistory history) {
        return new ConsumptionHistoryDTO(history.getId(),history.getAmount(),history.getConsumeType(),
                history.getEffectiveDate(),history.getExpired(),history.getCreatedDate());
    }

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
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

    public Timestamp getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Timestamp effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "ConsumptionHistoryDTO{" +
                "historyId=" + historyId +
                ", amount=" + amount +
                ", consumeType=" + consumeType +
                ", effectiveDate=" + effectiveDate +
                ", expired=" + expired +
                ", createdDate=" + createdDate +
                '}';
    }
}
