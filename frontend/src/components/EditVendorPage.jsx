import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Box, TextField, Button, Typography, Paper } from "@mui/material";

export default function EditVendorPage() {
  const { id } = useParams(); // 獲取 URL 中的 id 參數
  const navigate = useNavigate(); // 用於導航回上一頁
  const [vendor, setVendor] = useState({
    name: "",
    contactInfo: "",
  });
  const [error, setError] = useState(null);

  // 編輯模式: 載入現有廠商資料
  useEffect(() => {
    if (id) {
      const fetchVendor = async () => {
        try {
          const response = await fetch(`http://localhost:8080/vendors/${id}`);
          if (!response.ok) {
            throw new Error("無法加載廠商資料。");
          }
          const data = await response.json();
          setVendor(data);
        } catch (err) {
          setError(err.message);
        }
      };

      fetchVendor();
    }
  }, [id]);

  // 處理表單輸入變化
  const handleChange = (e) => {
    const { name, value } = e.target;
    setVendor((prevVendor) => ({ ...prevVendor, [name]: value }));
  };

  // 處理表單提交
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(
        id ? `http://localhost:8080/vendors/${id}` : "http://localhost:8080/vendors",
        {
          method: id ? "PUT" : "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(vendor),
        }
      );

      if (!response.ok) {
        throw new Error(id ? "更新廠商失敗。" : "創建廠商失敗。");
      }

      alert(id ? "廠商更新成功！" : "廠商創建成功！");
      navigate("/vendors"); // 返回廠商列表頁面
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <Box sx={{ maxWidth: 600, margin: "auto", padding: 3 }}>
      <Paper elevation={3} sx={{ padding: 3 }}>
        <Typography variant="h4" gutterBottom>
          {id ? "編輯廠商" : "新增廠商"}
        </Typography>

        {/* 顯示錯誤訊息 */}
        {error && (
          <Typography variant="body1" color="error" gutterBottom>
            {error}
          </Typography>
        )}

        {/* 表單 */}
        <form onSubmit={handleSubmit}>
          <TextField
            label="廠商名稱"
            name="name"
            value={vendor.name}
            onChange={handleChange}
            fullWidth
            required
            margin="normal"
          />

          <TextField
            label="聯絡資訊"
            name="contactInfo"
            value={vendor.contactInfo}
            onChange={handleChange}
            fullWidth
            margin="normal"
          />

          <Box sx={{ marginTop: 3, display: "flex", justifyContent: "space-between" }}>
            <Button type="submit" variant="contained" color="primary">
              {id ? "更新" : "創建"}
            </Button>
            <Button variant="outlined" color="secondary" onClick={() => navigate("/vendors")}>
              取消
            </Button>
          </Box>
        </form>
      </Paper>
    </Box>
  );
}
