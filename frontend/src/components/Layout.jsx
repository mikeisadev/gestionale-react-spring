import { Outlet, Link } from "react-router";

function Layout() {
  return (
    <div id="main" className="flex">
      {/** SIDEBAR */}
      <div id="sidebar" className="p-4 bg-blue-300 fixed h-full w-[260px]">
        <div className="navigation-bar flex flex-col gap-4">
          <Link to="/prodotti">Prodotti</Link>
          <Link to="/categorie">Categorie</Link>
          <Link to="/dipendenti">Dipendenti</Link>
          <Link to="/progetti">Progetti</Link>
        </div>
      </div>

      {/** CONTENUTO PRINCIPALE */}
      <div id="content" className="w-[calc(100%-260px)] ml-auto p-[24px]">
        <Outlet />
      </div>
    </div>
  );
}

export default Layout;