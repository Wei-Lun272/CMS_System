export const SITE_STATUS = [
    { value: "PLANNING", label: "計劃中" },
    { value: "ONGOING", label: "進行中" },
    { value: "COMPLETED", label: "已完成" },
    { value: "ON_HOLD", label: "暫停中" },
    { value: "CANCELLED", label: "已取消" },
  ];
  
  // 根據枚舉值獲取友好名稱
  export const getSiteStatusLabel = (value) => {
    const status = SITE_STATUS.find((item) => item.value === value);
    return status ? status.label : value; // 預設回傳原始值
  };
  