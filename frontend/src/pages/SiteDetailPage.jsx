import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Box,
  Typography,
  Paper,
  Grid,
  Button,
  IconButton,
  Collapse,
} from "@mui/material";
import { ExpandMore, ExpandLess } from "@mui/icons-material";

export default function SiteDetailPage() {
  const { id } = useParams(); // 獲取工地 ID
  const navigate = useNavigate();
  const [siteDetail, setSiteDetail] = useState(null);
  const [error, setError] = useState(null);
  const [expanded, setExpanded] = useState({}); // 控制消耗紀錄展開狀態

  // 載入工地詳細資料
  useEffect(() => {
    const fetchSiteDetail = async () => {
      try {
        const response = await fetch(`http://localhost:8080/sites/${id}/detail`);
        if (!response.ok) throw new Error("無法加載工地詳細資料。");
        const data = await response.json();
        setSiteDetail(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchSiteDetail();
  }, [id]);

  const toggleExpand = (materialId) => {
    setExpanded((prev) => ({ ...prev, [materialId]: !prev[materialId] }));
  };

  if (error) {
    return (
      <Box sx={{ maxWidth: 800, margin: "auto", padding: 3 }}>
        <Typography variant="h6" color="error">
          {error}
        </Typography>
      </Box>
    );
  }

  if (!siteDetail) {
    return (
      <Box sx={{ maxWidth: 800, margin: "auto", padding: 3 }}>
        <Typography variant="h6">正在加載工地詳細資料...</Typography>
      </Box>
    );
  }

  const { siteName, address, status, description, materials } = siteDetail;

  return (
    <Box sx={{ maxWidth: 800, margin: "auto", padding: 3 }}>
      <Paper elevation={3} sx={{ padding: 3 }}>
        <Box sx={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
          <Typography variant="h4" gutterBottom>
            工地詳細資料
          </Typography>
          <Box>
            <Button
              variant="contained"
              color="primary"
              onClick={() => navigate(`/sites/${id}/dispatch`)}
              sx={{ marginRight: 2 }}
            >
              派發
            </Button>
            <Button
              variant="outlined"
              color="secondary"
              onClick={() => navigate(`/sites/${id}/consume`)}
            >
              消耗
            </Button>
          </Box>
        </Box>

        <Typography variant="h6">工地名稱: {siteName}</Typography>
        <Typography>地址: {address}</Typography>
        <Typography>狀態: {status}</Typography>
        <Typography>描述: {description || "無"}</Typography>

        <Typography variant="h5" sx={{ marginTop: 3 }}>
          原物料清單
        </Typography>
        {materials.length === 0 ? (
          <Typography>目前無原物料資料。</Typography>
        ) : (
          <Grid container spacing={2} sx={{ marginTop: 2 }}>
            {materials.map((material) => (
              <Grid item xs={12} key={material.siteMaterialId}>
                <Paper elevation={1} sx={{ padding: 2 }}>
                  <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <Box>
                    <Typography sx={{ flexShrink: 0, width: "200px", fontWeight: "bold" }}>
          {material.materialName}
        </Typography>
        <Typography sx={{ flexShrink: 0, color: "gray" }}>
          單位：{material.materialUnit}
        </Typography>
                      <Typography>
                        <strong>庫存:</strong> {material.stock}
                      </Typography>
                      <Typography
                        sx={{
                          color: "red",
                          fontWeight: "bold",
                        }}
                      >
                        <strong>警戒值:</strong> {material.alert}
                      </Typography>
                    </Box>
                    <IconButton
                      onClick={() => toggleExpand(material.siteMaterialId)}
                      aria-expanded={expanded[material.siteMaterialId]}
                      aria-label="show more"
                    >
                      {expanded[material.siteMaterialId] ? <ExpandLess /> : <ExpandMore />}
                    </IconButton>
                  </Box>
                  <Collapse in={expanded[material.siteMaterialId]} timeout="auto" unmountOnExit>
                    <Box sx={{ marginTop: 2 }}>
                      <Typography>
                        <strong>消耗紀錄:</strong>
                      </Typography>
                      {material.consumptionHistory.length === 0 ? (
                        <Typography>無消耗紀錄。</Typography>
                      ) : (
                        material.consumptionHistory.map((history) => (
                          <Typography key={history.historyId} sx={{ marginLeft: 2 }}>
                            - {history.consumeType}: {history.amount} (生效日期:{" "}
                            {new Date(history.effectiveDate).toLocaleDateString()})
                          </Typography>
                        ))
                      )}
                    </Box>
                  </Collapse>
                </Paper>
              </Grid>
            ))}
          </Grid>
        )}

        <Box sx={{ marginTop: 3, textAlign: "right" }}>
          <Button
            variant="contained"
            color="primary"
            onClick={() => navigate("/sites")}
          >
            返回工地列表
          </Button>
        </Box>
      </Paper>
    </Box>
  );
}
