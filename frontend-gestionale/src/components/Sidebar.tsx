import { Link } from "react-router";

const Sidebar = () => {

    return (
        <div id="sidebar">
            {/* Logo */}
            <div className="logo">
                <h1>Logo</h1>
            </div>

            {/* Menu sidebar */}
            <div className="menu">
                <ul>
                    <li><Link to="/">Dashboard</Link></li>
                    <li><Link to="/articoli">Articoli</Link></li>
                    <li><Link to="/pagine">Pagine</Link></li>
                    <li><Link to="/prodotti">Prodotti</Link></li>
                    <li><Link to="/categorie">Categorie</Link></li>
                </ul>

                {/* Esci */}
                <div className="bottom-menu">
                    <a>Esci</a>
                </div>
            </div>
        </div>
    );
}

export default Sidebar;