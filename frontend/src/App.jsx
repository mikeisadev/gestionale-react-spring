function App() {
  return (
    <div id="main" className="flex">
      {/** SIDEBAR */}
      <div id="sidebar" className="p-4 bg-blue-300 fixed h-full w-[320px]">
        <div className="navigation-bar flex flex-col gap-4">
          <a href="/prodotti">Prodotti</a>
          <a href="/categorie">Categorie</a>
          <a hred="/dipendenti">Dipendenti</a>
          <a href="/progetti">Progetti</a>
        </div>
      </div>

      {/** CONTENUTO PRINCIPALE */}
      <div id="content" className="">
        <p>Qui deve apparire il contenuto dell'elemento selezionato</p>
      </div>
    </div>
  );
}

export default App;
