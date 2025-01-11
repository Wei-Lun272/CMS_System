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
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
} from "@mui/material";
import { ExpandMore, ExpandLess, Edit, Delete } from "@mui/icons-material";

export default function SiteDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [siteDetail, setSiteDetail] = useState(null);
  const [error, setError] = useState(null);
  const [expanded, setExpanded] = useState({});
  const [editDialogOpen, setEditDialogOpen] = useState(false);
  const [currentEdit, setCurrentEdit] = useState(null);

  useEffect(() => {
    const fetchSiteDetail = async () => {
      try {
        const response = await fetch(`http://localhost:8080/sites/${id}/detail`);
        if (!response.ok) throw new Error("無法加載工地詳細資料。");
        const data = await response.json();
        const updatedMaterials = data.materials.map((material) => ({
          ...material,
          consumptionHistory: material.consumptionHistory.map((history) => ({
            ...history,
            expired: Boolean(history.expired),
          })),
        }));
        setSiteDetail({ ...data, materials: updatedMaterials });
      } catch (err) {
        setError(err.message);
      }
    };

    fetchSiteDetail();
  }, [id]);

  const toggleExpand = (materialId) => {
    setExpanded((prev) => ({ ...prev, [materialId]: !prev[materialId] }));
  };

  const handleEditOpen = (record) => {
    if (!record.expired) {
      setCurrentEdit(record);
      setEditDialogOpen(true);
    }
  };

  const handleEditClose = () => {
    setEditDialogOpen(false);
    setCurrentEdit(null);
  };

  const handleEditSave = async () => {
    if (!currentEdit) return;

    try {
      const response = await fetch(
        `http://localhost:8080/consumption-histories/${currentEdit.historyId}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            newAmount: currentEdit.amount,
            newEffectiveDate: currentEdit.effectiveDate,
          }),
        }
      );
      if (!response.ok) throw new Error("更新失敗。");

      setSiteDetail((prev) => {
        const updatedMaterials = prev.materials.map((material) => ({
          ...material,
          consumptionHistory: material.consumptionHistory.map((history) =>
            history.historyId === currentEdit.historyId ? currentEdit : history
          ),
        }));
        return { ...prev, materials: updatedMaterials };
      });

      handleEditClose();
      alert("更新成功！");
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async (historyId) => {
    if (!window.confirm("確定要刪除此消耗紀錄嗎？")) return;

    try {
      const response = await fetch(
        `http://localhost:8080/consumption-histories/${historyId}`,
        { method: "DELETE" }
      );
      if (!response.ok) throw new Error("刪除失敗。");

      setSiteDetail((prev) => {
        const updatedMaterials = prev.materials.map((material) => ({
          ...material,
          consumptionHistory: material.consumptionHistory.filter(
            (history) => history.historyId !== historyId
          ),
        }));
        return { ...prev, materials: updatedMaterials };
      });

      alert("刪除成功！");
    } catch (err) {
      setError(err.message);
    }
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
                                <IconButton
                                  onClick={() => handleEditOpen(history)}
                                  color="primary"
                                >
                                  <Edit />
                                </IconButton>
                                <IconButton
                                  onClick={() => handleDelete(history.historyId)}
                                  color="error"
                                >
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

      {currentEdit && (
        <Dialog open={editDialogOpen} onClose={handleEditClose}>
          <DialogTitle>更新紀錄</DialogTitle>
          <DialogContent>
            <TextField
              label="消耗數量"
              type="number"
              value={currentEdit.amount}
              onChange={(e) =>
                setCurrentEdit({ ...currentEdit, amount: parseFloat(e.target.value) })
              }
              fullWidth
              margin="normal"
              />
              <TextField
                label="生效日期"
                type="date"
                value={currentEdit.effectiveDate}
                onChange={(e) =>
                  setCurrentEdit({ ...currentEdit, effectiveDate: e.target.value })
                }
                fullWidth
                margin="normal"
                InputLabelProps={{ shrink: true }}
              />
            </DialogContent>
            <DialogActions>
              <Button onClick={handleEditClose} color="secondary">
                取消
              </Button>
              <Button onClick={handleEditSave} color="primary">
                保存
              </Button>
            </DialogActions>
          </Dialog>
        )}
      </Box>
    );
  }
  