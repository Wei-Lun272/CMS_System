package com.wellan.Construction_Management_System.dto;

import java.sql.Timestamp;

public class UpdateConsumptionDTO {
    Float newAmount;
    Timestamp newEffectiveDate;

    protected UpdateConsumptionDTO() {
    }

    public UpdateConsumptionDTO(Float newAmount, Timestamp newEffectiveDate) {
        this.newAmount = newAmount;
        this.newEffectiveDate = newEffectiveDate;
    }

    public Float getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(Float newAmount) {
        this.newAmount = newAmount;
    }

    public Timestamp getNewEffectiveDate() {
        return newEffectiveDate;
    }

    public void setNewEffectiveDate(Timestamp newEffectiveDate) {
        this.newEffectiveDate = newEffectiveDate;
    }
}
