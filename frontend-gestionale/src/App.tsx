import { type ReactNode } from 'react'
import { Routes, Route } from 'react-router';

import Layout from './components/Layout';

import Login from './pages/auth/Login';

function App(): ReactNode {

  return (
    <>
      <Routes>
        {/** Isoliamo le rotte con autenticazione  */}
        <Route path='/' element={ <Layout /> }>
          <Route index element={<div className="main-content"><h1>Dashboard gestionale</h1></div>}/>

          <Route path="/articoli" element={<div className="main-content"><h1>Articoli</h1></div>}/>
          <Route path="/pagine" element={<div className="main-content"><h1>Pagine</h1></div>}/>
          <Route path="/prodotti" element={<div className="main-content"><h1>Prodotti</h1></div>}/>
          <Route path="/categorie" element={<div className="main-content"><h1>Categorie</h1></div>}/>
        </Route>

        {/** Gestire autenticazione e registrazione */}
        <Route path="/login" element={<Login />} />
        <Route path="/registrazione" element={<h1>Registrazione</h1>} />
        <Route path="/password-dimenticata" element={<h1>Password dimenticata</h1>} />
      </Routes>
    </>
  )
}

export default App
