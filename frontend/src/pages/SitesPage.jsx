import React, { useEffect, useState } from "react";
import { Box, Typography, Button, Paper } from "@mui/material";
import SiteItem from "../components/SiteItem";
import { useNavigate } from "react-router-dom";

export default function SitesPage() {
  const [sites, setSites] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  // 獲取所有工地資料
  useEffect(() => {
    const fetchSites = async () => {
      try {
        const response = await fetch("http://localhost:8080/sites");
        if (!response.ok) throw new Error("無法載入工地資料。");
        const data = await response.json();
        setSites(data);
      } catch (err) {
        setError(err.message);
      }
    };
    fetchSites();
  }, []);

  // 處理刪除
  const handleDelete = async (id) => {
    if (window.confirm("確定要刪除此工地嗎？")) {
      try {
        const response = await fetch(`http://localhost:8080/sites/${id}`, {
          method: "DELETE",
        });
        if (!response.ok) throw new Error("刪除失敗。");
        setSites((prev) => prev.filter((site) => site.id !== id));
      } catch (err) {
        setError(err.message);
      }
    }
  };

  return (
    <Box sx={{ padding: "16px" }}>
      <Typography variant="h4" gutterBottom>
        工地列表
      </Typography>

      <Button
        variant="contained"
        color="primary"
        onClick={() => navigate("/sites/new")}
        sx={{ marginBottom: "16px" }}
      >
        新增工地
      </Button>

      {error && <Typography color="error">{error}</Typography>}

      {sites.length === 0 ? (
        <Typography>目前沒有工地資料。</Typography>
      ) : (
        <Paper elevation={3} sx={{ padding: 2 }}>
          {sites.map((site) => (
            <SiteItem key={site.id} site={site} onDelete={handleDelete} />
          ))}
        </Paper>
      )}
    </Box>
  );
}
