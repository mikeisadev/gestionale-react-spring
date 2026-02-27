import { Link, Outlet } from 'react-router';

const Layout = () => {

    return (
        <div id="main" className='flex'>
            <div id="sidebar" className='p-4 bg-blue-300 fixed h-full w-[320px]'>
                <div className="navigation-bar flex flex-col gap-4">
                    <Link to="/prodotti">Prodotti</Link>
                    <Link to="/categorie">Categorie</Link>
                    <Link to="/dipendenti">Dipendenti</Link>
                    <Link to="/progetti">Progetti</Link>
                </div>
            </div>
            <div id="content" className=''>
                <Outlet />
            </div>
        </div>
    )
}

export default Layout;