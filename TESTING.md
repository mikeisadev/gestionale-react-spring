# TESTING.md — La piramide del testing in questo repository

Questo documento accompagna gli esempi di test didattici del gestionale.
Ogni test è stato scelto per **insegnare il livello della piramide a cui
appartiene**, non per massimizzare la coverage.

## Mappa dei file

| File | Livello della piramide | Cosa insegna | Comando |
|---|---|---|---|
| `gestionale/src/test/java/.../service/DipendenteServiceImplTest.java` | 1 — Unit (backend) | La regola di business "email univoca" testata con repository mockato: il database non esiste, il test gira in millisecondi | `cd gestionale && ./mvnw test -Dtest=DipendenteServiceImplTest` |
| `gestionale/src/test/java/.../service/ProdottoServiceImplTest.java` | 1 — Unit (backend) | Le decisioni del service (eccezione per id inesistente, aggiornamento del titolo) verificate in isolamento totale | `cd gestionale && ./mvnw test -Dtest=ProdottoServiceImplTest` |
| `gestionale/src/test/java/.../repository/DipendenteRepositoryIT.java` | 2 — Integrazione (slice JPA) | La query derivata `findByEmail` eseguita DAVVERO contro un database (H2): ciò che un mock non può dirti | `cd gestionale && ./mvnw test -Dtest=DipendenteRepositoryIT` |
| `gestionale/src/test/java/.../controller/DipendenteControllerIT.java` | 2 — Integrazione (app intera + MockMvc) | Il contratto HTTP: l'eccezione del service diventa un 400 con `ApiResponse` e messaggio "email già esistente" | `cd gestionale && ./mvnw test -Dtest=DipendenteControllerIT` |
| `gestionale/src/test/java/.../e2e/GestionaleE2ETest.java` | 3 — Funzionale E2E (API) | Un viaggio utente completo via HTTP reale (porta random): crea Anna → la ritrova → il duplicato viene rifiutato | `cd gestionale && ./mvnw test -Dtest=GestionaleE2ETest` |
| `frontend/src/pages/__tests__/Prodotti.test.jsx` | 1 — Unit (frontend) | Il service mockato con `vi.mock` è l'equivalente frontend del repository mockato: stesso principio, altra metà dell'app | `cd frontend && npm run test -- Prodotti` |
| `frontend/src/components/__tests__/Layout.test.jsx` | 1 — Unit (frontend) | Un'unità senza I/O è il test più economico in assoluto: nessun mock, nessuna attesa, solo render e query | `cd frontend && npm run test -- Layout` |

**Tutti i test backend:** `cd gestionale && ./mvnw test` (10 test, nessun database locale richiesto: profilo `test` + H2 in-memory)
**Tutti i test frontend:** `cd frontend && npm run test` (3 test)

## Come leggere questi esempi in aula (dal basso verso l'alto)

**Si parte dalla base: gli unit test.** Aprite `DipendenteServiceImplTest`
e `Prodotti.test.jsx` fianco a fianco: sono lo stesso identico gesto su due
metà dell'applicazione. Nel backend il mock è il repository, nel frontend è
il service HTTP — in entrambi i casi *il pezzo lento e imprevedibile viene
sostituito da uno che risponde a comando*. Per questo i 7 test di base
girano in meno di un secondo: sono i test che si scrivono a decine e si
eseguono a ogni salvataggio.

**Si sale al centro: i test di integrazione.** Qui la domanda cambia: non
più "il mio codice decide bene?" ma "i pezzi veri collaborano davvero?".
`DipendenteRepositoryIT` mostra il sapore piccolo (una slice: solo JPA + H2),
`DipendenteControllerIT` il sapore grande (l'applicazione intera, ma senza
rete). Il caso chiave è l'email duplicata: al livello 1 avevamo verificato
che il service *lancia* l'eccezione; qui verifichiamo che quell'eccezione
*arriva al client* come 400 con il messaggio giusto. Due test, due domande
diverse, nessuna ridondanza.

**Si arriva al vertice: il test funzionale E2E.** `GestionaleE2ETest` non sa
nulla di classi, service o repository: parla con l'applicazione solo via
HTTP, come farebbe il frontend React o un utente con Postman. È un racconto
Given/When/Then: creo Anna, la ritrovo, il duplicato viene rifiutato e Anna
resta una sola. È il test più costoso — server vero, porta vera — e per
questo al vertice ce ne sono *pochi*. Nota per il corso: questo stesso
scenario, scritto in Gherkin, diventa un test funzionale Cucumber: il
passaggio è solo di forma, la sostanza l'avete già vista qui.

La morale della piramide: **più si sale, più il test è realistico ma lento e
fragile; più si scende, più è veloce e preciso ma cieco sulle collaborazioni.**
Servono tutti e tre i livelli, in proporzioni decrescenti.

## Note tecniche

- I test **non toccano il MariaDB locale**: il profilo `test`
  (`gestionale/src/test/resources/application-test.properties`) usa H2
  in-memory con `ddl-auto=create-drop`.
- Il progetto usa **Spring Boot 4**: le annotazioni di test vivono in
  package nuovi (`@DataJpaTest` in `org.springframework.boot.data.jpa.test.autoconfigure`,
  `@AutoConfigureMockMvc` in `org.springframework.boot.webmvc.test.autoconfigure`,
  `TestRestTemplate` in `org.springframework.boot.resttestclient` e va attivato
  con `@AutoConfigureTestRestTemplate` + modulo `spring-boot-restclient`).
- Surefire è configurato per eseguire anche i file `*IT.java` con `mvn test`
  (di default eseguirebbe solo `*Test`): scelta didattica per lanciare
  l'intera piramide con un solo comando.
