import { createBrowserRouter } from "react-router-dom";
import Layout from "./components/Layout";
import HomePage from "./pages/HomePage";
import ErrorPage from "./pages/ErrorPage"
import MaterialsPage from "./pages/MaterialsPage";
import VendorsPage from "./pages/VendorsPage"
import SitesPage from "./pages/SitesPage";
import LoginPage from "./pages/LoginPage";
// import HistoryPage from "./pages/HistoryPage";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />, // 使用 Layout 作為全局佈局
    errorElement:<ErrorPage />,
    children: [
      { index:true, element: <HomePage /> },         // 首頁
      { path:"login", element: <LoginPage /> },         // 登入
      { path: "materials", element: <MaterialsPage /> }, // 原物料管理
      { path: "vendors", element: <VendorsPage /> }, // 原物料管理
      { path: "sites", element: <SitesPage /> },         // 工地管理
    //   { path: "/history", element: <HistoryPage /> },     // 歷史記錄
    ],
  },
]);

export default router;
