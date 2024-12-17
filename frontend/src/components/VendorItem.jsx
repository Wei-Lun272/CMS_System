import React from "react";
import { Box, Typography, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

const VendorItem = ({ vendor, onDelete }) => {
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
      <Typography variant="h5" gutterBottom>
        廠商名稱: {vendor.name}
      </Typography>
      <Typography variant="body1">
        <strong>聯絡資訊:</strong> {vendor.contactInfo || "無"}
      </Typography>

      <Box sx={{ marginTop: "16px", display: "flex", gap: "8px" }}>
        <Button
          variant="contained"
          color="primary"
          onClick={() => navigate(`/vendors/${vendor.id}/edit`)}
        >
          編輯
        </Button>
        <Button
          variant="contained"
          color="error"
          onClick={() => onDelete(vendor.id)}
        >
          刪除
        </Button>
      </Box>
    </Box>
  );
};

export default VendorItem;
