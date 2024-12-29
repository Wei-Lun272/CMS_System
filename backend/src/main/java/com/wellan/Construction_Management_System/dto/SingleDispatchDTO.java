package com.wellan.Construction_Management_System.dto;

import com.wellan.Construction_Management_System.entity.ConsumeType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public class SingleDispatchDTO {
    /**
     * 原物料 ID
     */
    @NotNull(message = "原物料 ID 不得為空")
    private Integer materialId;

    /**
     * 派發數量
     */
    @NotNull(message = "派發數量不得為空")
    @Min(value = 1, message = "派發數量必須為正數")
    private Float dispatchAmount;

    @NotNull(message = "警戒值不得為空")
    @Min(value = 0,message = "警戒值必須大於零")
    private Float alertAmount;
    /**
     * 派發日期
     */
    @NotNull(message = "派發日期不得為空")
    @FutureOrPresent(message = "派發日期不能為過去日期")
    private Timestamp dispatchDate;

    /**
     * 派發方式（Dispatch）
     */
    private final ConsumeType consumeType = ConsumeType.DISPATCH;

    /**
     * 派發生效日期
     */
    @NotNull(message = "派發生效日期不得為空")
    @FutureOrPresent(message = "派發生效日期必須為今天或未來日期")
    private Timestamp effectiveDate;

    public SingleDispatchDTO() {
    }

    public SingleDispatchDTO(Integer materialId, Float dispatchAmount, Float alertAmount, Timestamp dispatchDate, Timestamp effectiveDate) {
        this.materialId = materialId;
        this.dispatchAmount = dispatchAmount;
        this.alertAmount = alertAmount;
        this.effectiveDate = effectiveDate;
    // 如果派發日期為空，則設置為當前日期
        this.dispatchDate = (dispatchDate != null) ? dispatchDate : new Timestamp(System.currentTimeMillis());
    }

    public @NotNull(message = "原物料 ID 不得為空") Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(@NotNull(message = "原物料 ID 不得為空") Integer materialId) {
        this.materialId = materialId;
    }

    public @NotNull(message = "派發數量不得為空") @Min(value = 1, message = "派發數量必須為正數") Float getDispatchAmount() {
        return dispatchAmount;
    }

    public void setDispatchAmount(@NotNull(message = "派發數量不得為空") @Min(value = 1, message = "派發數量必須為正數") Float dispatchAmount) {
        this.dispatchAmount = dispatchAmount;
    }

    public @NotNull(message = "警戒值不得為空") @Min(value = 0, message = "警戒值必須大於零") Float getAlertAmount() {
        return alertAmount;
    }

    public void setAlertAmount(@NotNull(message = "警戒值不得為空") @Min(value = 0, message = "警戒值必須大於零") Float alertAmount) {
        this.alertAmount = alertAmount;
    }

    public @NotNull(message = "派發日期不得為空") @FutureOrPresent(message = "派發日期不能為過去日期") Timestamp getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(@NotNull(message = "派發日期不得為空") @FutureOrPresent(message = "派發日期不能為過去日期") Timestamp dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public @NotNull(message = "派發方式不得為空") ConsumeType getConsumeType() {
        return consumeType;
    }

//    public void setConsumeType(@NotNull(message = "消耗方式不得為空") ConsumeType consumeType) {
//        this.consumeType = consumeType;
//    }

    public @NotNull(message = "派發生效日期不得為空") @FutureOrPresent(message = "消耗生效日期必須為今天或未來日期") Timestamp getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(@NotNull(message = "派發生效日期不得為空") @FutureOrPresent(message = "消耗生效日期必須為今天或未來日期") Timestamp effectiveDate) {
        this.effectiveDate = effectiveDate;
    }


}
