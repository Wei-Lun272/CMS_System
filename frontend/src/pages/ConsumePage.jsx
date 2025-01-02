import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Box,
  Typography,
  Button,
  Paper,
  TextField,
  MenuItem,
  Grid,
} from "@mui/material";

export default function ConsumePage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [siteName, setSiteName] = useState(""); // 工地名稱
  const [materials, setMaterials] = useState([]); // 工地派發的原物料
  const [consumeList, setConsumeList] = useState([]); // 消耗的清單
  const [error, setError] = useState(null);

  useEffect(() => {
    // 獲取工地詳細資料
    const fetchSiteDetails = async () => {
      try {
        const response = await fetch(`http://localhost:8080/sites/${id}/detail`);
        if (!response.ok) throw new Error("無法加載工地資料。");
        const data = await response.json();
        setSiteName(data.siteName); // 設置工地名稱
        setMaterials(data.materials); // 僅使用已派發的原物料
      } catch (err) {
        setError(err.message);
      }
    };

    fetchSiteDetails();
  }, [id]);

  const handleAddConsume = () => {
    setConsumeList([
      ...consumeList,
      { materialId: "", materialName: "", unit: "", amount: "", consumeType: "", effectiveDate: "" },
    ]);
  };

  const handleChangeConsume = (index, field, value) => {
    const updatedList = [...consumeList];
    if (field === "materialId") {
      const selectedMaterial = materials.find((m) => m.materialId === value);
      updatedList[index] = {
        ...updatedList[index],
        materialId: value,
        materialName: selectedMaterial?.materialName || "",
        unit: selectedMaterial?.materialUnit || "",
      };
    } else if (field === "amount") {
      updatedList[index][field] = value ? parseFloat(value) : "";
    } else {
      updatedList[index][field] = value;
    }
    setConsumeList(updatedList);
  };

  const isValidConsumeList = () => {
    return consumeList.every(
      (item) =>
        item.materialId &&
        parseFloat(item.amount) > 0 &&
        item.consumeType &&
        item.effectiveDate
    );
  };

  const handleSubmit = async () => {
    if (!isValidConsumeList()) {
      alert("請確認所有欄位均已填寫且格式正確！");
      return;
    }

    const formattedConsumeList = consumeList.map((item) => ({
      materialId: item.materialId,
      consumeAmount: parseFloat(item.amount),
      consumeType: item.consumeType,
      effectiveDate: `${item.effectiveDate}T00:00:00.000Z`,
    }));

    try {
      const response = await fetch(`http://localhost:8080/sites/${id}/consume`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formattedConsumeList),
      });
      if (!response.ok) throw new Error("消耗提交失敗。");
      alert("消耗成功！");
      navigate(`/sites/${id}/detail`);
    } catch (err) {
      setError(err.message);
    }
  };

  const isMaterialsEmpty = materials.length === 0;

  return (
    <Box sx={{ maxWidth: 800, margin: "auto", padding: 3 }}>
      <Paper elevation={3} sx={{ padding: 3 }}>
        <Typography variant="h4" gutterBottom>
          消耗原物料 - 工地: {siteName}
        </Typography>
        {error && <Typography color="error">{error}</Typography>}

        {isMaterialsEmpty ? (
          <Typography variant="h6" color="text.secondary" sx={{ marginTop: 2 }}>
            沒有可消耗的原物料。
          </Typography>
        ) : (
          <>
            <Grid container spacing={2}>
              {consumeList.map((consume, index) => (
                <Grid item xs={12} key={index}>
                  <Paper elevation={1} sx={{ padding: 2 }}>
                    <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                      <Typography sx={{ flexShrink: 0, fontWeight: "bold" }}>
                        原物料: {consume.materialName || "未選擇"} ({consume.unit || "無單位"})
                      </Typography>
                      <Button
                        variant="outlined"
                        onClick={() => handleChangeConsume(index, "materialId", null)}
                      >
                        重新選擇
                      </Button>
                    </Box>

                    <TextField
                      select
                      label="選擇原物料"
                      value={consume.materialId}
                      onChange={(e) => handleChangeConsume(index, "materialId", e.target.value)}
                      fullWidth
                      margin="normal"
                    >
                      {materials
                        .filter(
                          (m) =>
                            !consumeList.some(
                              (c) => c.materialId === m.materialId && c.materialId !== consume.materialId
                            )
                        )
                        .map((material) => (
                          <MenuItem key={material.materialId} value={material.materialId}>
                            {material.materialName} ({material.materialUnit})
                          </MenuItem>
                        ))}
                    </TextField>
                    <TextField
                      label="消耗數量"
                      value={consume.amount}
                      onChange={(e) => handleChangeConsume(index, "amount", e.target.value)}
                      type="number"
                      fullWidth
                      margin="normal"
                    />
                    <TextField
                      select
                      label="消耗類型"
                      value={consume.consumeType}
                      onChange={(e) => handleChangeConsume(index, "consumeType", e.target.value)}
                      fullWidth
                      margin="normal"
                    >
                      <MenuItem value="ONCE">一次性</MenuItem>
                      <MenuItem value="DAILY">每日</MenuItem>
                    </TextField>
                    <TextField
                      label="生效日期"
                      type="date"
                      value={consume.effectiveDate}
                      onChange={(e) => handleChangeConsume(index, "effectiveDate", e.target.value)}
                      fullWidth
                      margin="normal"
                      InputLabelProps={{ shrink: true }}
                    />
                  </Paper>
                </Grid>
              ))}
            </Grid>

            <Button
              variant="outlined"
              onClick={handleAddConsume}
              sx={{ marginTop: 2 }}
              disabled={isMaterialsEmpty}
            >
              新增消耗項目
            </Button>
            <Button
              variant="contained"
              color="primary"
              onClick={handleSubmit}
              sx={{ marginTop: 2, marginLeft: 2 }}
              disabled={isMaterialsEmpty || consumeList.length === 0}
            >
              提交
            </Button>
          </>
        )}
      </Paper>
    </Box>
  );
}
