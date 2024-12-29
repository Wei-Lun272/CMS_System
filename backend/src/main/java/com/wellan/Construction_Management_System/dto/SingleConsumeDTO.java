package com.wellan.Construction_Management_System.dto;

import com.wellan.Construction_Management_System.entity.ConsumeType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public class SingleConsumeDTO {
    /**
     * 原物料 ID
     */
    @NotNull(message = "原物料 ID 不得為空")
    private Integer materialId;

    /**
     * 消耗數量
     */
    @NotNull(message = "消耗數量不得為空")
    @Min(value = 1, message = "消耗數量必須為正數")
    private Float consumeAmount;

    /**
     * 記錄創建時間
     */
    @NotNull(message = "記錄創建時間不得為空")
    private final Timestamp createdDate = new Timestamp(System.currentTimeMillis());

    /**
     * 消耗方式（ONCE, DAILY）
     */
    private ConsumeType consumeType;

    /**
     * 消耗生效日期
     */
    @NotNull(message = "消耗生效日期不得為空")
    @FutureOrPresent(message = "消耗生效日期必須為今天或未來日期")
    private Timestamp effectiveDate;

    protected SingleConsumeDTO() {
    }

    public SingleConsumeDTO(Integer materialId, Float consumeAmount, ConsumeType consumeType, Timestamp effectiveDate) {
        this.materialId = materialId;
        this.consumeAmount = consumeAmount;
        if (consumeType == ConsumeType.DAILY || consumeType == ConsumeType.ONCE) {
            this.consumeType = consumeType;
        } else {
            throw new IllegalArgumentException("消耗方式必須是 DAILY 或 ONCE");
        }
        this.effectiveDate = effectiveDate;
    }

    public @NotNull(message = "原物料 ID 不得為空") Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(@NotNull(message = "原物料 ID 不得為空") Integer materialId) {
        this.materialId = materialId;
    }

    public @NotNull(message = "消耗數量不得為空") @Min(value = 1, message = "消耗數量必須為正數") Float getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(@NotNull(message = "消耗數量不得為空") @Min(value = 1, message = "消耗數量必須為正數") Float consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public @NotNull(message = "記錄創建時間不得為空") Timestamp getCreatedDate() {
        return createdDate;
    }

    public ConsumeType getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(ConsumeType consumeType) {
        if (consumeType != ConsumeType.DAILY && consumeType != ConsumeType.ONCE) {
            throw new IllegalArgumentException("消耗方式必須是 DAILY 或 ONCE");
        }
        this.consumeType = consumeType;
    }

    public @NotNull(message = "消耗生效日期不得為空") @FutureOrPresent(message = "消耗生效日期必須為今天或未來日期") Timestamp getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(@NotNull(message = "消耗生效日期不得為空") @FutureOrPresent(message = "消耗生效日期必須為今天或未來日期") Timestamp effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
