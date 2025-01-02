import React from "react";
import { Box, Typography, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { getSiteStatusLabel } from "../utils/siteStatusHelper";

const SiteItem = ({ site, onDelete }) => {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        border: "1px solid #ccc",
        borderRadius: "8px",
        padding: "16px",
        marginBottom: "16px",
      }}
    >
      {/* 工地名稱可點擊 */}
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

      <Box sx={{ marginTop: "16px", display: "flex", gap: "8px" }}>
        {/* 編輯按鈕 */}
        <Button
          variant="contained"
          color="primary"
          onClick={() => navigate(`/sites/${site.id}/edit`)}
        >
          編輯
        </Button>
        {/* 刪除按鈕 */}
        <Button
          variant="contained"
          color="error"
          onClick={() => onDelete(site.id)}
        >
          刪除
        </Button>
        {/* 派發按鈕 */}
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
