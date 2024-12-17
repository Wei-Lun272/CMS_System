import React, { useEffect, useState } from "react";
import MaterialItem from "../components/MaterialItem";
import { Box, Button, Typography } from "@mui/material";
import { NavLink } from "react-router-dom";

export default function MaterialsPage() {
  const [materials, setMaterials] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchMaterials = async () => {
      try {
        const response = await fetch("http://localhost:8080/materials");
        if (!response.ok) {
          throw new Error("無法載入原物料資料。");
        }
        const data = await response.json();
        setMaterials(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchMaterials();
  }, []);

  // const handleEdit = (material) => {
  //   alert(`編輯功能尚未實現: ${material.materialName}`);
  //   // 編輯操作的處理邏輯
  // };

// 刪除原物料
const handleDelete = async (id) => {
  if (window.confirm("確定要刪除此原物料嗎？")) {
    try {
      const response = await fetch(`http://localhost:8080/materials/${id}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error("刪除失敗");
      }

      // 從前端列表中移除刪除的項目
      setMaterials((prevMaterials) =>
        prevMaterials.filter((material) => material.id !== id)
      );

      alert("刪除成功！");
    } catch (err) {
      setError(err.message);
    }
  }
};

  return (
    <Box sx={{ padding: "16px" }}>
      <Typography variant="h4" gutterBottom>
        原物料列表
      </Typography>
      <Button
      component={NavLink}
      to="/materials/new"
      variant="contained"
      color="primary"
      sx={{
        marginBottom: "16px",
        '&:hover': { backgroundColor: 'darkblue' }, // hover 顏色
      }}
    >
      新增
    </Button>
      {error && (
        <Typography variant="h6" color="error">
          {error}
        </Typography>
      )}

      {materials.map((material) => (
        <MaterialItem
          key={material.id}
          material={material}
          // onEdit={handleEdit}
          onDelete={handleDelete}
        />
      ))}
    </Box>
  );
}
