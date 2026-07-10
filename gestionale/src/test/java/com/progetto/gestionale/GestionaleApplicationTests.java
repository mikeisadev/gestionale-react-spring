package com.progetto.gestionale;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/*
 * Test generato da Spring Initializr: verifica solo che il contesto
 * dell'applicazione si avvii. Attiviamo il profilo "test" così usa
 * H2 in-memory e non dipende dal MariaDB locale (vincolo dei test).
 */
@SpringBootTest
@ActiveProfiles("test")
class GestionaleApplicationTests {

	@Test
	void contextLoads() {
	}

}
