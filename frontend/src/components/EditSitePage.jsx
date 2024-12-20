import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Box, TextField, Button, Typography, Paper, MenuItem } from "@mui/material";
import { SITE_STATUS } from "../utils/siteStatusHelper";

export default function EditSitePage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [site, setSite] = useState({
    siteName: "",
    address: "",
    status: "",
    description: "",
    latitude: "",
    longitude: "",
  });
  const [error, setError] = useState(null);

  useEffect(() => {
    if (id) {
      const fetchSite = async () => {
        try {
          const response = await fetch(`http://localhost:8080/sites/${id}`);
          if (!response.ok) throw new Error("無法加載工地資料。");
          const data = await response.json();
          setSite(data);
        } catch (err) {
          setError(err.message);
        }
      };
      fetchSite();
    }
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const method = id ? "PUT" : "POST";
      const url = id
        ? `http://localhost:8080/sites/${id}`
        : "http://localhost:8080/sites";

      const response = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(site),
      });

      if (!response.ok) throw new Error(id ? "更新失敗" : "創建失敗");

      alert(id ? "工地更新成功！" : "工地創建成功！");
      navigate("/sites");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <Box sx={{ maxWidth: 600, margin: "auto", padding: 3 }}>
      <Paper elevation={3} sx={{ padding: 3 }}>
        <Typography variant="h4" gutterBottom>
          {id ? "編輯工地" : "新增工地"}
        </Typography>

        {error && <Typography color="error">{error}</Typography>}

        <form onSubmit={handleSubmit}>
          <TextField
            label="工地名稱"
            name="siteName"
            value={site.siteName}
            onChange={(e) => setSite({ ...site, siteName: e.target.value })}
            fullWidth
            required
            margin="normal"
          />
          <TextField
            label="地址"
            name="address"
            value={site.address}
            onChange={(e) => setSite({ ...site, address: e.target.value })}
            fullWidth
            required
            margin="normal"
          />
          <TextField
            label="狀態"
            name="status"
            select
            value={site.status}
            onChange={(e) => setSite({ ...site, status: e.target.value })}
            fullWidth
            required
            margin="normal"
          >
            {SITE_STATUS.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </TextField>
          <TextField
            label="描述"
            name="description"
            value={site.description}
            onChange={(e) => setSite({ ...site, description: e.target.value })}
            fullWidth
            margin="normal"
          />
          <TextField
            label="緯度"
            name="latitude"
            value={site.latitude}
            onChange={(e) => setSite({ ...site, latitude: e.target.value })}
            fullWidth
            margin="normal"
          />
          <TextField
            label="經度"
            name="longitude"
            value={site.longitude}
            onChange={(e) => setSite({ ...site, longitude: e.target.value })}
            fullWidth
            margin="normal"
          />

          <Box sx={{ marginTop: 3 }}>
            <Button type="submit" variant="contained" color="primary">
              {id ? "更新" : "創建"}
            </Button>
            <Button
              variant="outlined"
              color="secondary"
              onClick={() => navigate("/sites")}
              sx={{ marginLeft: 2 }}
            >
              取消
            </Button>
          </Box>
        </form>
      </Paper>
    </Box>
  );
}
