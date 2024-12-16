import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Box, TextField, Button, Typography, MenuItem } from "@mui/material";

// MaterialUnit 的枚舉類型（value 為常數名，label 為友好名稱）
const MATERIAL_UNITS = [
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

export default function EditMaterialPage() {
  const { id } = useParams(); // 獲取路由參數
  const navigate = useNavigate(); // 用於導航
  const [material, setMaterial] = useState({
    materialName: "",
    unit: "",
    stock: 0,
    alertNumber: 0,
  });
  const [error, setError] = useState(null);

  useEffect(() => {
    if (id) {
      // 如果有 id，則加載原物料資料
      const fetchMaterial = async () => {
        try {
          const response = await fetch(`http://localhost:8080/materials/${id}`);
          if (!response.ok) {
            throw new Error("無法獲取原物料資料");
          }
          const data = await response.json();
          setMaterial(data);
        } catch (err) {
          setError(err.message);
        }
      };

      fetchMaterial();
    }
  }, [id]);

  const handleSave = async () => {
    try {
      const method = id ? "PUT" : "POST"; // 如果有 id，則為更新
      const url = id
        ? `http://localhost:8080/materials/${id}`
        : "http://localhost:8080/materials";
      const response = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(material),
      });

      if (!response.ok) {
        throw new Error("保存失敗");
      }

      alert(id ? "更新成功！" : "新增成功！");
      navigate("/materials");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <Box sx={{ maxWidth: 600, margin: "0 auto", padding: "16px" }}>
      <Typography variant="h4" gutterBottom>
        {id ? "編輯原物料" : "新增原物料"}
      </Typography>

      {/* 原物料名稱 */}
      <TextField
        label="名稱"
        value={material.materialName}
        onChange={(e) => setMaterial({ ...material, materialName: e.target.value })}
        fullWidth
        margin="normal"
        required
      />

      {/* 單位選單 */}
      <TextField
        select
        label="單位"
        value={material.unit}
        onChange={(e) => setMaterial({ ...material, unit: e.target.value })}
        fullWidth
        margin="normal"
        required
      >
        {MATERIAL_UNITS.map((unit) => (
          <MenuItem key={unit.value} value={unit.value}>
            {unit.label}
          </MenuItem>
        ))}
      </TextField>

      {/* 庫存數量 */}
      <TextField
        label="庫存數量"
        type="number"
        value={material.stock}
        onChange={(e) =>
          setMaterial({ ...material, stock: Math.max(0, parseInt(e.target.value, 10) || 0) })
        }
        fullWidth
        margin="normal"
        required
      />

      {/* 警戒數量 */}
      <TextField
        label="警戒數量"
        type="number"
        value={material.alertNumber}
        onChange={(e) =>
          setMaterial({
            ...material,
            alertNumber: Math.max(0, parseInt(e.target.value, 10) || 0),
          })
        }
        fullWidth
        margin="normal"
        required
      />

      {/* 錯誤訊息 */}
      {error && (
        <Typography variant="body1" color="error" gutterBottom>
          {error}
        </Typography>
      )}

      {/* 保存和取消按鈕 */}
      <Button
        variant="contained"
        color="primary"
        onClick={handleSave}
        sx={{ marginTop: "16px" }}
      >
        保存
      </Button>
      <Button
        variant="outlined"
        color="secondary"
        onClick={() => navigate("/materials")}
        sx={{ marginTop: "16px", marginLeft: "8px" }}
      >
        取消
      </Button>
    </Box>
  );
}
