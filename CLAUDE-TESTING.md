# PROMPT PER CLAUDE CODE — Test didattici per "gestionale-react-spring"

> **Uso:** aprire Claude Code nella root del repo (branch `gestionale-fase-4`) e incollare tutto il blocco sotto la riga. Il prompt è calibrato sulla struttura reale del progetto: backend Spring Boot 4 / Java 25 in `gestionale/`, frontend React 19 + Vite in `frontend/`.

---

Sei un senior software engineer con forte esperienza in testing, e stai preparando **materiale didattico** per un corso sulla generazione di test funzionali. Questo repository è un gestionale usato come esempio in aula: backend Java + Spring Boot in `gestionale/`, frontend React (JSX, no TypeScript) in `frontend/`.

## Missione

Genera esempi di test che illustrino la **piramide del testing**, con questa distribuzione:

- **Backend**: unit test, integration test e test funzionali E2E (a livello API)
- **Frontend**: SOLO unit test

Lo scopo è didattico: ogni test deve *insegnare* il livello della piramide a cui appartiene, non massimizzare la coverage. Qualità da senior (pulito, leggibile, AAA esplicito), ma **niente rocket science**: pochi test, scelti bene, capibili da chi li vede per la prima volta.

## Vincoli globali (non negoziabili)

1. **Non modificare il codice di produzione.** Uniche eccezioni ammesse: aggiungere dipendenze di test in `gestionale/pom.xml`, aggiungere devDependencies/script in `frontend/package.json` e i relativi file di configurazione dei test.
2. I test **non devono dipendere dal database MariaDB locale**: aggiungi H2 in scope test e un `src/test/resources/application-test.properties` (profilo `test`, H2 in-memory, `ddl-auto=create-drop`). La configurazione di produzione non si tocca.
3. Ogni file di test si apre con un **blocco di commento didattico in italiano**: a quale livello della piramide appartiene, cosa carica (niente Spring / slice / applicazione intera), cosa è reale e cosa è finto, e perché questo test vive a questo livello e non a un altro.
4. Naming dei metodi: `metodo_condizione_risultatoAtteso`. Dentro ogni test, commenti `// Arrange`, `// Act`, `// Assert`.
5. Ogni test deve poter fallire: nessun assert generico tipo `assertNotNull` come unica verifica.
6. Commenti e messaggi in italiano; identificatori in inglese o coerenti con lo stile esistente del repo (che usa nomi italiani: rispettalo).
7. Attenzione alle versioni: il progetto usa **Spring Boot 4.0.3 e Java 25** — verifica i nomi corretti degli starter e delle annotazioni di test per questa versione (es. `@MockitoBean` al posto del deprecato `@MockBean`) leggendo il `pom.xml` prima di scrivere codice.

## Backend — 3 livelli in `gestionale/src/test/java/...`

### Livello 1 — Unit test (base della piramide)
File: `service/DipendenteServiceImplTest.java` e `service/ProdottoServiceImplTest.java`.
- Solo JUnit 5 + Mockito, **nessun contesto Spring**: repository mockato, si testa la logica pura del service.
- Casi da coprire (già presenti nel codice, non inventarne):
  - `creaDipendente` con email già esistente → lancia `IllegalArgumentException` (la regola di business dell'email univoca in `DipendenteServiceImpl`)
  - `creaDipendente` con email nuova → salva e restituisce il dipendente
  - `trovaProdottoPerId` con id inesistente → lancia l'eccezione con il messaggio "Prodotto non trovato"
  - `aggiornaProdotto` → aggiorna il titolo del prodotto esistente e salva
- Nel commento didattico evidenzia: "il database qui NON esiste: il mock risponde ciò che decidiamo noi — per questo il test gira in millisecondi".

### Livello 2 — Integration test (centro della piramide)
Due sapori, per mostrare che "integrazione" significa "più pezzi veri che collaborano":
1. `repository/DipendenteRepositoryIT.java` con `@DataJpaTest` + H2: verifica che la query derivata `findByEmail` funzioni davvero contro un database reale (salva → trova; email inesistente → Optional vuoto).
2. `controller/DipendenteControllerIT.java` con `@SpringBootTest` + `MockMvc` (profilo `test`, H2): attraversa controller → service → repository → H2 senza server HTTP reale. Caso chiave: **POST di un dipendente con email duplicata → status 400 e body `ApiResponse` con il messaggio "email già esistente"** — dimostra che `GlobalExceptionHandler` e la regola di business collaborano. Più un caso felice (POST → 200/201 → GET lo ritrova).
- Nel commento didattico evidenzia la differenza col livello 1: "qui Spring, la serializzazione JSON, l'exception handler e il database sono VERI; è più lento ma verifica il contratto tra i pezzi".

### Livello 3 — Test funzionale E2E a livello API (vertice della piramide)
File: `e2e/GestionaleE2ETest.java` con `@SpringBootTest(webEnvironment = RANDOM_PORT)` + chiamate HTTP reali (usa `TestRestTemplate`, oppure REST Assured se preferisci aggiungere la dipendenza).
- Un **unico scenario-viaggio** in stile utente, con commenti che raccontano la storia in linguaggio Given/When/Then:
  1. Given: l'applicazione è avviata su una porta reale con database pulito
  2. When: creo il dipendente "Anna" via `POST /api/dipendenti`
  3. Then: `GET /api/dipendenti` la include nella lista
  4. When: provo a ricreare "Anna" con la stessa email
  5. Then: ricevo 400 con il messaggio di errore, e la lista contiene ancora UNA sola Anna
- (Adatta i path reali leggendo `DipendenteController`: non inventare endpoint.)
- Nel commento didattico evidenzia: "qui il test parla con l'applicazione SOLO via HTTP, come farebbe il frontend: non sa nulla di classi e service. È il livello del corso: gli stessi scenari, scritti in Gherkin, diventano i test funzionali di Cucumber".
- **Bonus opzionale** (solo se tutto il resto è verde): riscrivi lo stesso scenario come `dipendenti.feature` in Gherkin (italiano) con relative step definitions Cucumber, in un package separato `bdd/`, come anteprima del focus del corso. Se le dipendenze Cucumber creano attriti con Spring Boot 4, salta il bonus e segnalamelo invece di forzare.

## Frontend — solo unit test in `frontend/src/**/__tests__/`

Setup: aggiungi Vitest + @testing-library/react + @testing-library/jest-dom + jsdom, script `"test": "vitest run"` e la config necessaria (il progetto è Vite 7 + React 19, JSX). Nessun test E2E o di integrazione qui: è una scelta didattica per far vedere che il frontend ha la SUA base della piramide.

1. `pages/__tests__/Prodotti.test.jsx` — testa la pagina `Prodotti` mockando il layer `services/prodottiService` con `vi.mock`:
   - al mount, la pagina mostra i prodotti restituiti dal service mockato (2 prodotti finti → 2 titoli visibili)
   - compilando il form e inviandolo, `prodottiService.creaProdotto` viene chiamato con i dati inseriti
   - Nel commento didattico: "il backend qui NON esiste: il mock del service è l'equivalente frontend del mock del repository — stesso principio, altra metà dell'applicazione".
2. Un secondo test a scelta su un'unità pura già presente (es. lo schema/utility in `src/data/forms` o un altro componente semplice come `Layout`): scegli quello che produce l'esempio più leggibile, spiegando nel commento perché un'unità senza I/O è il test più economico in assoluto.

Se i componenti non hanno attributi aggredibili dai test, preferisci query per ruolo/label/testo (Testing Library way); NON aggiungere `data-testid` al codice di produzione (vincolo 1).

## Ordine di lavoro e checkpoint (fermati e chiedimi conferma dopo ogni fase)

1. **Ricognizione:** leggi pom.xml, application.properties, controller/service/entity di Dipendente e Prodotto, `frontend/package.json`, `frontend/src/pages/Prodotti.jsx` e `frontend/src/services/`. Riportami: endpoint reali, versioni, e il piano dei file che creerai. Attendi il mio OK.
2. **Backend livello 1** → esegui `mvn -pl gestionale test` (o dalla cartella `gestionale`: `mvn test`) e mostra l'esito.
3. **Backend livelli 2 e 3** → riesegui e mostra l'esito. Se un test fallisce per un comportamento reale del codice di produzione, NON modificare il codice: segnalamelo come "possibile bug trovato dal test" (è oro didattico).
4. **Frontend** → `npm install` e `npm run test` in `frontend/`, mostra l'esito.
5. **Chiusura:** genera `TESTING.md` nella root con: tabella file → livello della piramide → cosa insegna → comando per eseguirlo, più un breve paragrafo "come leggere questi esempi in aula" che percorre la piramide dal basso verso l'alto.

Alla fine dammi il riepilogo: file creati, dipendenze aggiunte, esito delle esecuzioni, eventuali bug o comportamenti sospetti scoperti dai test.