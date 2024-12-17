import React from "react";
import { AppBar, Toolbar, Typography, IconButton } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import { NavLink } from "react-router-dom";

const Header = ({ onMenuClick }) => {
  return (
    <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
      <Toolbar>
        {/* Drawer 開關按鈕 */}
        <IconButton
          color="inherit"
          edge="start"
          onClick={onMenuClick} // 點擊開關 Drawer
          sx={{ marginRight: 2 }}
        >
          <MenuIcon />
        </IconButton>

        {/* 標題 */}
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          <NavLink
            to="/"
            style={{
              textDecoration: "none",
              color: "inherit",
              fontWeight: "bold",
            }}
          >
            工程管理系統
          </NavLink>
        </Typography>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
