import React, { useEffect, useState } from "react";
import { Box, Typography, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { getSiteStatusLabel } from "../utils/siteStatusHelper";

const SiteItem = ({ site, onDelete }) => {
  const navigate = useNavigate();
  const [isAlert, setIsAlert] = useState(false); // 用來存儲警戒狀態

  // 根據 site.id 獲取警戒預測資料
  useEffect(() => {
    const fetchAlertStatus = async () => {
      try {
        const response = await fetch(`http://localhost:8080/sites/${site.id}/alert`);
        if (!response.ok) throw new Error("無法獲取警戒預測資料。");
        const data = await response.json();
        setIsAlert(data); // 設置警戒狀態
      } catch (err) {
        console.error("獲取警戒預測失敗:", err);
      }
    };

    fetchAlertStatus();
  }, [site.id]);

  return (
    <Box
      sx={{
        border: "1px solid #ccc",
        borderRadius: "8px",
        padding: "16px",
        marginBottom: "16px",
        backgroundColor: isAlert ? "#ffcccc" : "transparent", // 根據警戒狀態設置背景顏色
      }}
    >
      <Typography
        variant="h6"
        sx={{
          color: "blue",
          cursor: "pointer",
          textDecoration: "underline",
        }}
        onClick={() => navigate(`/sites/${site.id}/detail`)}
      >
        {site.siteName}
      </Typography>
      <Typography>地址: {site.address}</Typography>
      <Typography>狀態: {getSiteStatusLabel(site.status)}</Typography>
      <Typography>描述: {site.description || "無"}</Typography>
      <Typography>緯度: {site.latitude}, 經度: {site.longitude}</Typography>

      {/* 顯示警戒日期 */}
      {isAlert && (
        <Typography sx={{ color: "red", fontWeight: "bold" }}>
          預計警戒日期: {new Date().toLocaleDateString()}
        </Typography>
      )}

      <Box sx={{ marginTop: "16px", display: "flex", gap: "8px" }}>
        <Button
          variant="contained"
          color="primary"
          onClick={() => navigate(`/sites/${site.id}/edit`)}
        >
          編輯
        </Button>
        <Button
          variant="contained"
          color="error"
          onClick={() => onDelete(site.id)}
        >
          刪除
        </Button>
        <Button
          variant="contained"
          color="success"
          onClick={() => navigate(`/sites/${site.id}/dispatch`)}
        >
          派發
        </Button>
        <Button
          variant="contained"
          color="secondary"
          onClick={() => navigate(`/sites/${site.id}/consume`)}
        >
          消耗
        </Button>
      </Box>
    </Box>
  );
};

export default SiteItem;
