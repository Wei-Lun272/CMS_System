import React, { useEffect, useState } from "react";
import {
  Box,
  Typography,
  Paper,
  Grid,
  Button,
  IconButton,
  Collapse,
} from "@mui/material";
import {
  ExpandMore,
  ExpandLess,
  Edit,
  Delete,
  WarningAmber, // 警告圖示
} from "@mui/icons-material";
import { useNavigate, useParams } from "react-router-dom";

const SiteDetailPage = () => {
  const { id } = useParams(); // 工地 ID
  const navigate = useNavigate();

  const [siteDetail, setSiteDetail] = useState(null);
  const [error, setError] = useState(null);
  const [expanded, setExpanded] = useState({});
  const [alertPredictions, setAlertPredictions] = useState([]); // 警戒預測資料

  // 1. 取得工地詳細資料
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

  // 2. 取得警戒預測資料
  useEffect(() => {
    const fetchAlertPredictions = async () => {
      try {
        const response = await fetch(`http://localhost:8080/sites/${id}/alert-predictions`);
        if (!response.ok) throw new Error("無法獲取警戒預測資料。");
        const data = await response.json();
        setAlertPredictions(data);
      } catch (err) {
        console.error("獲取警戒預測失敗:", err);
      }
    };

    fetchAlertPredictions();
  }, [id]);

  // 3. 判斷是否為警戒狀態（14 天內）
  const isAlert = (materialId) => {
    const prediction = alertPredictions.find((p) => p.materialId === materialId);
    if (prediction && prediction.alertDate) {
      // 計算距離當前日期的天數
      const alertDate = new Date(prediction.alertDate);
      const currentDate = new Date();
      const diffInDays = (alertDate - currentDate) / (1000 * 3600 * 24);
  
      // 假設我們改成 diffInDays < 14
      return diffInDays < 14;
    }
  
    // 若 alertDate 為 null 或找不到預測，就 return false
    return false;
  };
  

  // 錯誤處理
  if (error) {
    return (
      <Box sx={{ maxWidth: 800, margin: "auto", padding: 3 }}>
        <Typography variant="h6" color="error">
          {error}
        </Typography>
      </Box>
    );
  }

  // 加載中
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
        {/* 頁面標題與操作按鈕 */}
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
          }}
        >
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

        {/* 工地資訊 */}
        <Typography variant="h6">工地名稱: {siteName}</Typography>
        <Typography>地址: {address}</Typography>
        <Typography>狀態: {status}</Typography>
        <Typography>描述: {description || "無"}</Typography>

        {/* 原物料清單 */}
        <Typography variant="h5" sx={{ marginTop: 3 }}>
          原物料清單
        </Typography>
        {materials.length === 0 ? (
          <Typography>目前無原物料資料。</Typography>
        ) : (
          <Grid container spacing={2} sx={{ marginTop: 2 }}>
            {materials.map((material) => {
              // 判斷警戒狀態
              const alertStatus = isAlert(material.materialId);

              // 找到預測資料
              const matchedPrediction = alertPredictions.find(
                (pred) => pred.materialId === material.materialId
              );

              // 顯示日期或提示
              let alertDateContent = (
                <Typography sx={{ color: "green", fontWeight: "bold" }}>
                  最近14天內不會到達警戒值
                </Typography>
              );

              if (matchedPrediction && matchedPrediction.alertDate) {
                const showDate = new Date(matchedPrediction.alertDate).toLocaleDateString();
                alertDateContent = (
                  <Typography sx={{ color: "red", fontWeight: "bold" }}>
                    <strong>警戒日期:</strong> {showDate}
                  </Typography>
                );
              }

              return (
                <Grid item xs={12} key={material.siteMaterialId}>
                  <Paper elevation={1} sx={{ padding: 2 }}>
                    <Box
                      sx={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                        backgroundColor: alertStatus ? "#f59b9b" : "#ffffff",
                        borderRadius: "4px",
                        padding: 1,
                      }}
                    >
                      {/* 左側：原物料資訊 */}
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
                        <Typography sx={{ color: "red", fontWeight: "bold" }}>
                          <strong>警戒值:</strong> {material.alert}
                        </Typography>
                        {/* 顯示警戒日期或「最近14天內不會到達警戒值」 */}
                        {alertDateContent}
                      </Box>

                      {/* 右側：警告圖示 + 展開按鈕 */}
                      <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                        {/* 若為警戒狀態，就顯示圖示 */}
                        {alertStatus && (
                          <WarningAmber sx={{ color: "#ff8400", fontSize: "32px" }} />
                        )}

                        <IconButton
                          onClick={() =>
                            setExpanded((prev) => ({
                              ...prev,
                              [material.siteMaterialId]: !prev[material.siteMaterialId],
                            }))
                          }
                          aria-expanded={expanded[material.siteMaterialId]}
                          aria-label="show more"
                        >
                          {expanded[material.siteMaterialId] ? <ExpandLess /> : <ExpandMore />}
                        </IconButton>
                      </Box>
                    </Box>

                    {/* 下方：消耗紀錄的展開/收合 */}
                    <Collapse in={expanded[material.siteMaterialId]} timeout="auto" unmountOnExit>
                      <Box sx={{ marginTop: 2 }}>
                        <Typography>
                          <strong>消耗紀錄:</strong>
                        </Typography>
                        {material.consumptionHistory.length === 0 ? (
                          <Typography>無消耗紀錄。</Typography>
                        ) : (
                          material.consumptionHistory.map((history) => (
                            <Box
                              key={history.historyId}
                              sx={{
                                display: "flex",
                                justifyContent: "space-between",
                                alignItems: "center",
                                padding: "8px",
                                backgroundColor: history.expired ? "#f0f0f0" : "#fff",
                                borderRadius: "4px",
                                marginBottom: "8px",
                              }}
                            >
                              <Typography sx={{ flex: 1, marginLeft: 2 }}>
                                - {history.consumeType}: {history.amount} (生效日期:{" "}
                                {new Date(history.effectiveDate).toLocaleDateString()})
                              </Typography>
                              {!history.expired && (
                                <Box sx={{ display: "flex", gap: 1 }}>
                                  <IconButton color="primary">
                                    <Edit />
                                  </IconButton>
                                  <IconButton color="error">
                                    <Delete />
                                  </IconButton>
                                </Box>
                              )}
                            </Box>
                          ))
                        )}
                      </Box>
                    </Collapse>
                  </Paper>
                </Grid>
              );
            })}
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
};

export default SiteDetailPage;
