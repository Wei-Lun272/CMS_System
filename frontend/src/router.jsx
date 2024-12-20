import { createBrowserRouter, Navigate } from "react-router-dom";import Layout from "./components/Layout";
import HomePage from "./pages/HomePage";
import ErrorPage from "./pages/ErrorPage"
import MaterialsPage from "./pages/MaterialsPage";
import VendorsPage from "./pages/VendorsPage"
import SitesPage from "./pages/SitesPage";
import LoginPage from "./pages/LoginPage";
import EditMaterialPage from "./components/EditMaterialPage";
import EditVendorPage from "./components/EditVendorPage";
import EditSitePage from "./components/EditSitePage";
// import HistoryPage from "./pages/HistoryPage";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />, // 使用 Layout 作為全局佈局
    errorElement:<ErrorPage />,
    children: [
      { index:true, element: <HomePage /> },         // 首頁
      { path:"login", element: <LoginPage />       },         // 登入
      { path: "materials",
        children:[ 
            {index:true,element:<MaterialsPage />},
            {path:":id/edit",element:<EditMaterialPage/>},
            {path:"new",element:<EditMaterialPage/>}
        ]
        }, // 原物料管理
      { path: "vendors", 
        children:[ 
            {index:true,element: <VendorsPage />},
            {path:":id/edit",element:<EditVendorPage/>},
            {path:"new",element:<EditVendorPage/>}
        ] }, // 原物料管理
      { path: "sites", 
        children:[ 
            {index:true,element: <SitesPage />},
            {path:":id/edit",element:<EditSitePage/>},
            {path:"new",element:<EditSitePage/>}
        ] },  
      { path: "*", element: <Navigate to="/" replace /> },       // 工地管理
    //   { path: "/history", element: <HistoryPage /> },     // 歷史記錄
    ],
  },
]);

export default router;
