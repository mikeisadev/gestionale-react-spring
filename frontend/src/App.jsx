import Prodotti from "./pages/Prodotti"
import Categorie from "./pages/Categorie"
import Dipendenti from "./pages/Dipendenti"
import Progetti from "./pages/Progetti"

import { Routes, Route } from 'react-router'
import Layout from "./components/Layout"

function App() {

  return (
    <Routes>
      <Route path="/" element={ <Layout /> }>
        <Route path="prodotti" element={ <Prodotti /> }/>
        <Route path="categorie" element={ <Categorie /> }/>
        <Route path="dipendenti" element={ <Dipendenti /> }/>
        <Route path="progetti" element={ <Progetti /> }/>
      </Route>
    </Routes>
  )
}

export default App
