import React from "react";
import { Box, Typography, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

const MaterialItem = ({ material,onDelete }) => {
    const navigate = useNavigate()
  if (!material) {
    return (
      <Typography variant="h6" color="error">
        無法載入原物料資料。
      </Typography>
    );
  }



  return (
    <Box
      sx={{
        border: "1px solid #ccc",
        borderRadius: "8px",
        padding: "16px",
        marginBottom: "16px",
      }}
    >
      <Typography variant="h5" gutterBottom>
        原物料名稱: {material.materialName}
      </Typography>
      <Typography variant="body1">
        <strong>計量單位:</strong> {material.unit}
      </Typography>
      <Typography variant="body1">
        <strong>庫存數量:</strong> {material.stock}
      </Typography>
      <Typography variant="body1">
        <strong>警戒數量:</strong> {material.alertNumber}
      </Typography>

      <Box sx={{ marginTop: "16px", display: "flex", gap: "8px" }}>
        <Button
          variant="contained"
          color="primary"
          onClick={() => navigate(`/materials/${material.id}/edit`)}
        >
          編輯
        </Button>
        <Button
          variant="contained"
          color="error"
          onClick={() => onDelete(material.id)}
        >
          刪除
        </Button>
      </Box>
    </Box>
  );
};

export default MaterialItem;
