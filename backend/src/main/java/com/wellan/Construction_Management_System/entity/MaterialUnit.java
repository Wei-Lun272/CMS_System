package com.wellan.Construction_Management_System.entity;

public enum MaterialUnit {
    // 枚舉值與友好名稱
    KILOGRAM("公斤"),
    TON("噸"),
    GRAM("克"),
    LITER("公升"),
    MILLILITER("毫升"),
    CUBIC_METER("立方公尺"),
    PIECE("件"),
    BOX("箱"),
    PACK("包"),
    PALLET("棧板"),
    ROLL("卷"),
    BAG("袋"),
    SHEET("片");

    // 定義友好名稱的字段
    private final String friendlyName;

    // 建構函數
    MaterialUnit(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    // 獲取友好名稱的方法
    public String getFriendlyName() {
        return friendlyName;
    }
}

