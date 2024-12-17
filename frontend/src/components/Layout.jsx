import React, { useState } from "react";
import { Outlet } from "react-router-dom";
import Header from "./Header";
import Navbar from "./Navbar";
import Footer from "./Footer";
import { Box, Drawer, Toolbar, IconButton } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";

const drawerWidth = 240;

const Layout = () => {
  const [open, setOpen] = useState(false); // 控制 Drawer 展開/收起狀態

  const toggleDrawer = () => {
    setOpen((prev) => !prev);
  };

  return (
    <Box sx={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      {/* Header 區域 */}
      <Header onMenuClick={toggleDrawer} />

      {/* 左側 Drawer 側邊欄 */}
      <Drawer
        variant="temporary"
        open={open}
        onClose={toggleDrawer}
        ModalProps={{
          keepMounted: true,
        }}
        sx={{
          "& .MuiDrawer-paper": {
            width: drawerWidth,
            boxSizing: "border-box",
          },
        }}
      >
        <Toolbar>
          <IconButton onClick={toggleDrawer}>
            <ChevronLeftIcon />
          </IconButton>
        </Toolbar>
        <Navbar />
      </Drawer>

      {/* 主要內容區域 */}
      <Box component="main" sx={{ flexGrow: 1, padding: 3 }}>
        <Toolbar /> {/* 這裡用來佔位，避免內容被 Header 覆蓋 */}
        <Outlet /> {/* 子路由內容 */}
      </Box>

      {/* Footer 區域 */}
      <Footer />
    </Box>
  );
};

export default Layout;
