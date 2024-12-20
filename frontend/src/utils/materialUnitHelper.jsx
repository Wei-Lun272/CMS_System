// MaterialUnit 枚舉對應的陣列
export const MATERIAL_UNITS = [
    { value: "KILOGRAM", label: "公斤" },
    { value: "TON", label: "噸" },
    { value: "GRAM", label: "克" },
    { value: "LITER", label: "公升" },
    { value: "MILLILITER", label: "毫升" },
    { value: "CUBIC_METER", label: "立方公尺" },
    { value: "PIECE", label: "件" },
    { value: "BOX", label: "箱" },
    { value: "PACK", label: "包" },
    { value: "PALLET", label: "棧板" },
    { value: "ROLL", label: "卷" },
    { value: "BAG", label: "袋" },
    { value: "SHEET", label: "片" },
  ];
  
  // 根據枚舉值獲取友好名稱
  export const getMaterialUnitLabel = (value) => {
    const unit = MATERIAL_UNITS.find((item) => item.value === value);
    return unit ? unit.label : value; // 如果找不到，預設返回原始值
  };
  