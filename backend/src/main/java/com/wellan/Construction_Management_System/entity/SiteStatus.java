package com.wellan.Construction_Management_System.entity;

public enum SiteStatus {
    PLANNING("計劃中"),       // 工地正在規劃階段
    ONGOING("進行中"),       // 工地施工中
    COMPLETED("已完成"),     // 工地已完工
    ON_HOLD("暫停中"),       // 工地暫停施工
    CANCELLED("已取消");     // 工地已取消

    private final String friendlyName; // 友好名稱，用於顯示給使用者

    // 构造函数
    SiteStatus(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    // 獲取友好名稱
    public String getFriendlyName() {
        return friendlyName;
    }



}

