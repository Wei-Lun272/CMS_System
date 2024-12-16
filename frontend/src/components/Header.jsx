import React from "react";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import { NavLink } from "react-router-dom";

const Header = () => {
  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          工程管理系統
        </Typography>
        <NavLink to="/login" style={({ isActive }) => ({
            color: isActive ? "yellow" : "white", // 樣式可動態改變
            textDecoration: "none",
            marginLeft: "16px",
          })}>登入</NavLink>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
