import React from "react";
import { Box, Button, TextField, Typography } from "@mui/material";

const LoginPage = () => {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        height: "100vh",
        padding: 2,
      }}
    >
      <Typography variant="h4" component="h1" gutterBottom>
        登入
      </Typography>
      <Box
        component="form"
        sx={{
          display: "flex",
          flexDirection: "column",
          gap: 2,
          width: "100%",
          maxWidth: 400,
        }}
      >
        <TextField label="帳號" variant="outlined" fullWidth />
        <TextField label="密碼" type="password" variant="outlined" fullWidth />
        <Button variant="contained" color="primary" fullWidth>
          提交
        </Button>
      </Box>
    </Box>
  );
};

export default LoginPage;
