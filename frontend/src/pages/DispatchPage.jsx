import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Box,
  Button,
  Typography,
  Paper,
  Grid,
  Menu,
  MenuItem,
  TextField,
} from "@mui/material";

export default function DispatchPage() {
  const { id } = useParams(); // 獲取工地 ID
  const navigate = useNavigate();
  const [materials, setMaterials] = useState([]); // 所有原物料
  const [dispatchList, setDispatchList] = useState([]); // 動態生成的派發列表
  const [error, setError] = useState(null);
  const [anchorEl, setAnchorEl] = useState(null); // 控制菜單的狀態
  const [activeIndex, setActiveIndex] = useState(null); // 當前操作的行索引
  const [siteName, setSiteName] = useState(""); // 工地名稱

  // 加載工地名稱
  useEffect(() => {
    const fetchSiteName = async () => {
      try {
        const response = await fetch(`http://localhost:8080/sites/${id}`);
        if (!response.ok) throw new Error("無法加載工地名稱");
        const data = await response.json();
        setSiteName(data.siteName || `ID: ${id}`);
      } catch (err) {
        setError(err.message);
      }
    };
    fetchSiteName();
  }, [id]);

  // 加載所有原物料
  useEffect(() => {
    const fetchMaterials = async () => {
      try {
        const response = await fetch("http://localhost:8080/materials");
        if (!response.ok) throw new Error("無法加載原物料列表");
        const data = await response.json();
        setMaterials(data);
      } catch (err) {
        setError(err.message);
      }
    };
    fetchMaterials();
  }, []);

  // 添加新行（選擇原物料）
  const handleAddRow = () => {
    setDispatchList((prev) => [
      ...prev,
      {
        materialId: "",
        materialName: "",
        dispatchAmount: 0,
        alertAmount: 0,
        dispatchDate: new Date().toISOString().split("T")[0],
        effectiveDate: new Date().toISOString().split("T")[0],
      },
    ]);
  };

  // 更新選擇的原物料
  const handleSelectMaterial = (index, material) => {
    setDispatchList((prev) =>
      prev.map((item, i) =>
        i === index
          ? { ...item, materialId: material.id, materialName: material.materialName }
          : item
      )
    );
    setAnchorEl(null); // 關閉菜單
  };

  // 刪除行
  const handleDeleteRow = (index) => {
    setDispatchList((prev) => prev.filter((_, i) => i !== index));
  };

  // 提交派發數據並跳轉
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`http://localhost:8080/sites/${id}/dispatch`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dispatchList.map(({ materialName, ...rest }) => rest)), // 過濾掉 materialName
      });
      if (!response.ok) throw new Error("派發失敗");
      const siteId = await response.json(); // 獲取返回的工地 ID
      alert("派發成功！");
      navigate(`/sites/${siteId}/detail`); // 跳轉到詳細頁面
    } catch (err) {
      setError(err.message);
    }
  };

  // 計算剩餘可選原物料
  const getAvailableMaterials = (selectedIds) =>
    materials.filter((material) => !selectedIds.includes(material.id));

  return (
    <Box sx={{ maxWidth: 800, margin: "auto", padding: 3 }}>
      <Paper elevation={3} sx={{ padding: 3 }}>
        <Typography
          variant="h4"
          gutterBottom
          sx={{ fontWeight: "bold", textAlign: "center" }}
        >
          派發原物料到工地: {siteName}
        </Typography>

        {error && <Typography color="error">{error}</Typography>}

        <form onSubmit={handleSubmit}>
          {dispatchList.map((dispatch, index) => {
            const selectedIds = dispatchList.map((d) => d.materialId); // 已選的原物料 ID
            const availableMaterials = getAvailableMaterials(selectedIds);

            return (
              <Grid container spacing={2} key={index} sx={{ marginBottom: 2 }}>
                <Grid item xs={12} sx={{ display: "flex", alignItems: "center" }}>
                  <Typography variant="h6" sx={{ flexGrow: 1 }}>
                    {dispatch.materialName || "未選擇原物料"}
                  </Typography>
                  <Button
                    variant="outlined"
                    color="primary"
                    onClick={(e) => {
                      setAnchorEl(e.currentTarget);
                      setActiveIndex(index);
                    }}
                  >
                    選擇原物料
                  </Button>
                </Grid>

                <Menu
                  anchorEl={anchorEl}
                  open={Boolean(anchorEl) && activeIndex === index}
                  onClose={() => setAnchorEl(null)}
                >
                  {availableMaterials.map((material) => (
                    <MenuItem
                      key={material.id}
                      onClick={() => handleSelectMaterial(index, material)}
                    >
                      {material.materialName}
                    </MenuItem>
                  ))}
                </Menu>

                <Grid item xs={6}>
                  <TextField
                    label="派發數量"
                    type="number"
                    value={dispatch.dispatchAmount}
                    onChange={(e) =>
                      setDispatchList((prev) =>
                        prev.map((item, i) =>
                          i === index
                            ? { ...item, dispatchAmount: parseFloat(e.target.value) }
                            : item
                        )
                      )
                    }
                    fullWidth
                    required
                  />
                </Grid>

                <Grid item xs={6}>
                  <TextField
                    label="警戒值"
                    type="number"
                    value={dispatch.alertAmount}
                    onChange={(e) =>
                      setDispatchList((prev) =>
                        prev.map((item, i) =>
                          i === index
                            ? { ...item, alertAmount: parseFloat(e.target.value) }
                            : item
                        )
                      )
                    }
                    fullWidth
                    required
                  />
                </Grid>

                <Grid item xs={6}>
                  <TextField
                    label="派發日期"
                    type="date"
                    value={dispatch.dispatchDate}
                    onChange={(e) =>
                      setDispatchList((prev) =>
                        prev.map((item, i) =>
                          i === index ? { ...item, dispatchDate: e.target.value } : item
                        )
                      )
                    }
                    fullWidth
                    required
                  />
                </Grid>

                <Grid item xs={6}>
                  <TextField
                    label="生效日期"
                    type="date"
                    value={dispatch.effectiveDate}
                    onChange={(e) =>
                      setDispatchList((prev) =>
                        prev.map((item, i) =>
                          i === index ? { ...item, effectiveDate: e.target.value } : item
                        )
                      )
                    }
                    fullWidth
                    required
                  />
                </Grid>

                <Grid item xs={12} sx={{ textAlign: "right" }}>
                  <Button
                    variant="outlined"
                    color="error"
                    onClick={() => handleDeleteRow(index)}
                  >
                    刪除
                  </Button>
                </Grid>
              </Grid>
            );
          })}

          <Box sx={{ marginTop: 3, display: "flex", justifyContent: "space-between" }}>
            <Button
              type="button"
              variant="contained"
              color="primary"
              onClick={handleAddRow}
            >
              新增原物料
            </Button>
            <Button type="submit" variant="contained" color="success">
              提交派發
            </Button>
          </Box>
        </form>
      </Paper>
    </Box>
  );
}
