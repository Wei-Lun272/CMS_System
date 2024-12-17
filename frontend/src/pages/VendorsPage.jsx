import React, { useEffect, useState } from "react";
import VendorItem from "../components/VendorItem";
import { Box, Button, Typography } from "@mui/material";
import { NavLink } from "react-router-dom";

export default function VendorsPage() {
  const [vendors, setVendors] = useState([]); // 儲存廠商資料
  const [loading, setLoading] = useState(true); // 加載狀態
  const [error, setError] = useState(null); // 錯誤訊息

  // 發送 HTTP 請求獲取廠商列表
  useEffect(() => {
    const fetchVendors = async () => {
      try {
        const response = await fetch("http://localhost:8080/vendors");
        if (!response.ok) throw new Error("無法載入廠商資料，請稍後再試。");
        const data = await response.json();
        setVendors(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchVendors();
  }, []);

  // 處理刪除邏輯
  const handleDelete = (id) => {
    if (window.confirm("確定要刪除此廠商嗎？")) {
      setVendors((prev) => prev.filter((vendor) => vendor.id !== id));
    }
  };

  return (
    <Box sx={{ padding: "16px" }}>
      <Typography variant="h4" gutterBottom>
        廠商列表
      </Typography>
      <Button
      component={NavLink}
      to="/vendors/new"
      variant="contained"
      color="primary"
      sx={{
        marginBottom: "16px",
        '&:hover': { backgroundColor: 'darkblue' }, // hover 顏色
      }}
    >
      新增
    </Button>
      {/* 顯示加載中 */}
      {loading && (
        <Typography variant="body1" color="textSecondary">
          正在載入資料...
        </Typography>
      )}

      {/* 顯示錯誤訊息 */}
      {error && (
        <Typography variant="body1" color="error">
          {error}
        </Typography>
      )}

      {/* 顯示當前廠商列表或「沒有資料」的提示 */}
      {!loading && !error && vendors.length === 0 && (
        <Typography variant="body1" color="textSecondary">
          目前沒有廠商資料。
        </Typography>
      )}

      {!loading && !error && vendors.length > 0 && (
        <>
          {vendors.map((vendor) => (
            <VendorItem
              key={vendor.id}
              vendor={vendor}
              onDelete={handleDelete}
            />
          ))}
        </>
      )}
    </Box>
  );
}
