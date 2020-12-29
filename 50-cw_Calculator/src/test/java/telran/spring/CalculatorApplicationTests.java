package telran.spring;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import telran.spring.controllers.CalculatorController;
import telran.spring.service.interfaces.Calculator;
import static telran.spring.api.ApiConstants.*;

@SpringBootTest
@AutoConfigureMockMvc
class CalculatorApplicationTests {
	
	@Autowired
	CalculatorController controller;
	@Autowired
	Calculator calculator;
	@Autowired
	MockMvc mock;
	
	// testing of the context ==================================================================
	@Test
	void contextLoads() {
		assertNotNull(controller);
		assertNotNull(calculator);
	}
	
	// helper methods ==================================================================
	private MockHttpServletResponse getMocResponse(String apiTemplate, String calcFunction, String firstParam, int firstParamValue, String secondParam, int secondParamValue) throws Exception {
		return mock.perform(MockMvcRequestBuilders.get(String.format(apiTemplate, calcFunction, firstParam, firstParamValue, secondParam, secondParamValue))).andReturn().getResponse();
	}
	private MockHttpServletResponse getMocResponse(String apiTemplate, String calcFunction, String firstParam, int firstParamValue) throws Exception {
		return mock.perform(MockMvcRequestBuilders.get(String.format(apiTemplate, calcFunction, firstParam, firstParamValue))).andReturn().getResponse();
	}
	private MockHttpServletResponse getMocResponse(String apiTemplate, String calcFunction) throws Exception {
		return mock.perform(MockMvcRequestBuilders.get(String.format(apiTemplate, calcFunction))).andReturn().getResponse();
	}
	private HttpStatus getHttpStatus(MockHttpServletResponse respons) {
		return HttpStatus.valueOf(respons.getStatus());
	}
	
	// testing of "add" functionality ==================================================================
	@Test
	void addNormal() throws Exception {
		String expected = "40";
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d&%s=%d", CALCULATOR_ADD, FIRST_PARAM, 20, SECOND_PARAM, 20);
		assertEquals(OK, getHttpStatus(respons));
		assertEquals(expected, respons.getContentAsString());
	}
	@Test
	void addMissedFirstParam() throws Exception {
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d", CALCULATOR_ADD, SECOND_PARAM, 20);
		assertEquals(BAD_REQUEST, getHttpStatus(respons));
	}
	@Test
	void addMissedSecondParam() throws Exception {
		String expected = "20";
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d", CALCULATOR_ADD, FIRST_PARAM, 20);
		assertEquals(OK, getHttpStatus(respons));
		assertEquals(expected, respons.getContentAsString());
	}
	@Test
	void addMissedParams() throws Exception {
		MockHttpServletResponse respons = getMocResponse("%s", CALCULATOR_ADD);
		assertEquals(BAD_REQUEST, getHttpStatus(respons));
	}
	
	// testing of "subtract" functionality ==================================================================
	@Test
	void subNormal() throws Exception {
		String expected = "40";
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d&%s=%d", CALCULATOR_SUB, FIRST_PARAM, 60, SECOND_PARAM, 20);
		assertEquals(OK, getHttpStatus(respons));
		assertEquals(expected, respons.getContentAsString());
	}
	@Test
	void subMissedFirstParam() throws Exception {
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d", CALCULATOR_SUB, SECOND_PARAM, 20);
		assertEquals(BAD_REQUEST, getHttpStatus(respons));
	}
	@Test
	void subMissedSecondParam() throws Exception {
		String expected = "40";
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d", CALCULATOR_SUB, FIRST_PARAM, 40);
		assertEquals(OK, getHttpStatus(respons));
		assertEquals(expected, respons.getContentAsString());
	}
	@Test
	void subMissedParams() throws Exception {
		MockHttpServletResponse respons = getMocResponse("%s", CALCULATOR_SUB);
		assertEquals(BAD_REQUEST, getHttpStatus(respons));
	}
	
	// testing of "multiply" functionality ==================================================================
	@Test
	void mulNormal() throws Exception {
		String expected = "40";
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d&%s=%d", CALCULATOR_MUL, FIRST_PARAM, 20, SECOND_PARAM, 2);
		assertEquals(OK, getHttpStatus(respons));
		assertEquals(expected, respons.getContentAsString());
	}
	@Test
	void mulMissedFirstParam() throws Exception {
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d", CALCULATOR_MUL, SECOND_PARAM, 20);
		assertEquals(BAD_REQUEST, getHttpStatus(respons));
	}
	@Test
	void mulMissedSecondParam() throws Exception {
		String expected = "40";
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d", CALCULATOR_MUL, FIRST_PARAM, 40);
		assertEquals(OK, getHttpStatus(respons));
		assertEquals(expected, respons.getContentAsString());
	}
	@Test
	void mulMissedParams() throws Exception {
		MockHttpServletResponse respons = getMocResponse("%s", CALCULATOR_MUL);
		assertEquals(BAD_REQUEST, getHttpStatus(respons));
	}
	
	// testing of "divide" functionality ==================================================================
	@Test
	void divNormal() throws Exception {
		String expected = "40";
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d&%s=%d", CALCULATOR_DIV, FIRST_PARAM, 80, SECOND_PARAM, 2);
		assertEquals(OK, getHttpStatus(respons));
		assertEquals(expected, respons.getContentAsString());
	}
	@Test
	void divMissedFirstParam() throws Exception {
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d", CALCULATOR_DIV, SECOND_PARAM, 20);
		assertEquals(BAD_REQUEST, getHttpStatus(respons));
	}
	@Test
	void divMissedSecondParam() throws Exception {
		String expected = "40";
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d", CALCULATOR_DIV, FIRST_PARAM, 40);
		assertEquals(OK, getHttpStatus(respons));
		assertEquals(expected, respons.getContentAsString());
	}
	@Test
	void divMissedParams() throws Exception {
		MockHttpServletResponse respons = getMocResponse("%s", CALCULATOR_DIV);
		assertEquals(BAD_REQUEST, getHttpStatus(respons));
	}
	@Test
	void divideByZero() throws Exception {
		MockHttpServletResponse respons = getMocResponse("%s?%s=%d&%s=%d", CALCULATOR_DIV, FIRST_PARAM, 20, SECOND_PARAM, 0);
		assertEquals(BAD_REQUEST, getHttpStatus(respons));
	}
	
	// testing of API ==================================================================
	@Test
	void wrongApiRequest() throws Exception {
		assertEquals(NOT_FOUND, getHttpStatus(getMocResponse("%s?%s=%d&%s=%d", "/abs", FIRST_PARAM, 20, SECOND_PARAM, 20)));
		assertEquals(NOT_FOUND, getHttpStatus(getMocResponse("%s?%s=%d", "/abs", SECOND_PARAM, 20)));
		assertEquals(NOT_FOUND, getHttpStatus(getMocResponse("%s?%s=%d", "/abs", FIRST_PARAM, 20)));
		assertEquals(NOT_FOUND, getHttpStatus(getMocResponse("%s", "/abs")));
	}
}
