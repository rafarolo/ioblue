package br.com.ioblue.test;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	private static final String API_V1_ACCOUNTS = "/api/v1/accounts";

	@Test
	void controllerTest() throws Exception {
		String mockUrlAllAccounts = new URL("http://localhost:" + port + API_V1_ACCOUNTS).toString();
		ResponseEntity<String> response = testRestTemplate.getForEntity(mockUrlAllAccounts, String.class);
		assertEquals(200, response.getStatusCodeValue());
		assertNotNull(response.getBody());
	}

}
