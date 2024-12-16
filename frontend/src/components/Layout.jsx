import React from "react";
import { Outlet } from "react-router-dom";
import Header from "./Header";
import Navbar from "./Navbar";
import Footer from "./Footer";
import { Box } from "@mui/material";

const Layout = () => {
  return (
    <Box sx={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      <Header />
      <Box sx={{ display: "flex", flexGrow: 1 }}>
        <Navbar />
        <Box component="main" sx={{ flexGrow: 1, padding: 2 }}>
          <Outlet /> {/* 子路由內容將在此渲染 */}
        </Box>
      </Box>
      <Footer />
    </Box>
  );
};

export default Layout;
