import { Outlet } from "react-router";
import Sidebar from "./Sidebar";

const Layout = () => {

    return (
        <div className="flex h-full">
            <Sidebar />
            <Outlet />
        </div>
    )
}

export default Layout;