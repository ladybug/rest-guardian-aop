package com.vrto;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.get;
import static com.vrto.Ids.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlGuardiansApplicationTests {

	@Value("${local.server.port}")
	int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void shouldListCompanyTickets() {
	    listTickets(KNOWN_COMPANY, KNOWN_USER).then()
			.statusCode(OK.value())
			.body("$.size", equalTo(3))
			.body("[0].subject", equalTo("First ticket"));
	}

	@Test
	public void shouldRejectAccessForUnknownCompany() {
	    listTickets(UNKNOWN_COMPANY, KNOWN_USER).then()
			.statusCode(NOT_FOUND.value())
			.body("message", equalTo("Unknown company: " + UNKNOWN_COMPANY));
	}

	@Test
	public void shouldRejectAccessForUnknownUser() {
		listTickets(KNOWN_COMPANY, UNKNOWN_USER).then()
			.statusCode(NOT_FOUND.value())
			.body("message", equalTo("Unknown user: " + UNKNOWN_USER));
	}

	@Test
	public void shouldRejectAccess_WhenUserDoesNotBelongToCompany() {
		listTickets(KNOWN_COMPANY, KNOWN_USER_DIFFERENT_COMPANY).then()
			.statusCode(NOT_FOUND.value())
			.body("message", equalTo("Unknown user: " + KNOWN_USER_DIFFERENT_COMPANY));
	}

	private Response listTickets(long companyId, long userId) {
		return get(String.format("/%d/%d/tickets", companyId, userId));
	}

}
