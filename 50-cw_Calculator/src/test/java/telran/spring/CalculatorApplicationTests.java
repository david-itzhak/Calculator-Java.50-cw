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
	// helper moc methods ==================================================================
	private MockHttpServletResponse getMocResponse(String httpGetRequest) throws Exception {
		return mock.perform(MockMvcRequestBuilders.get(httpGetRequest)).andReturn().getResponse();
	}
	private HttpStatus getHttpStatus(String httpGetRequest) throws Exception {
		return HttpStatus.valueOf(getMocResponse(httpGetRequest).getStatus());
	}
	// common test methods ==================================================================
	private void normal(String calcFunction, int firstParamValue, int secondParamValue, String expectedResult) throws Exception {
		String httpRequest = String.format("%s?%s=%d&%s=%d", calcFunction, FIRST_PARAM, firstParamValue, SECOND_PARAM, secondParamValue);
		assertEquals(OK, getHttpStatus(httpRequest));
		assertEquals(expectedResult, getMocResponse(httpRequest).getContentAsString());
	}
	private void missedFirstParam(String calcFunction, int secondParamValue) throws Exception {
		assertEquals(BAD_REQUEST, getHttpStatus(String.format ("%s?%s=%d", calcFunction, SECOND_PARAM, 20)));
	}
	private void missedSecondParam(String calcFunction, int firstParamValue, String expectedResult) throws Exception {
		String httpRequest = String.format ("%s?%s=%d", calcFunction, FIRST_PARAM, firstParamValue);
		assertEquals(OK, getHttpStatus(httpRequest));
		assertEquals(expectedResult, getMocResponse(httpRequest).getContentAsString());
	}
	private void missedParams(String calcFunction) throws Exception {
		assertEquals(BAD_REQUEST, getHttpStatus(String.format ("%s", calcFunction)));
	}
	private void wrongFirst(String calcFunction, String firstParamValue, int secondParamValue) throws Exception {
		assertEquals(BAD_REQUEST, getHttpStatus(String.format ("%s?%s=%s&%s=%d", calcFunction, FIRST_PARAM, firstParamValue, SECOND_PARAM, secondParamValue)));
	}
	private void wrongSecond(String calcFunction, int firstParamValue, String secondParamValue) throws Exception {
		assertEquals(BAD_REQUEST, getHttpStatus(String.format ("%s?%s=%d&%s=%s", calcFunction, FIRST_PARAM, firstParamValue, SECOND_PARAM, secondParamValue)));
	}
	private void wrongParams(String calcFunction, String firstParamValue, String secondParamValue) throws Exception {
		assertEquals(BAD_REQUEST, getHttpStatus(String.format("%s?%s=%s&%s=%s", calcFunction, FIRST_PARAM, firstParamValue, SECOND_PARAM, secondParamValue))); 
	}
	// testing of "add" functionality ==================================================================
	@Test
	void addNormal() throws Exception { normal(CALCULATOR_ADD, 20, 20, "40"); }
	@Test
	void addMissedFirstParam() throws Exception { missedFirstParam(CALCULATOR_ADD, 20); }
	@Test
	void addMissedSecondParam() throws Exception { missedSecondParam(CALCULATOR_ADD, 20, "20"); }
	@Test
	void addMissedParams() throws Exception { missedParams(CALCULATOR_ADD); }
	@Test
	void addWrongFirst() throws Exception { wrongFirst(CALCULATOR_ADD, "abc", 20); }
	@Test
	void addWrongSecond() throws Exception { wrongSecond(CALCULATOR_ADD, 20, "abc"); }
	@Test
	void addWrongParams() throws Exception { wrongParams(CALCULATOR_ADD, "abc", "abc"); }
	// testing of "subtract" functionality ==================================================================
	@Test
	void subNormal() throws Exception { normal(CALCULATOR_SUB, 60, 20, "40"); }
	@Test
	void subMissedFirstParam() throws Exception { missedFirstParam(CALCULATOR_SUB, 20); }
	@Test
	void subMissedSecondParam() throws Exception { missedSecondParam(CALCULATOR_SUB, 40, "40"); }
	@Test
	void subMissedParams() throws Exception { missedParams(CALCULATOR_SUB); }
	@Test
	void subWrongFirst() throws Exception { wrongFirst(CALCULATOR_SUB, "abc", 20); }
	@Test
	void subWrongSecond() throws Exception { wrongSecond(CALCULATOR_SUB, 20, "abc"); }
	@Test
	void subWrongParams() throws Exception { wrongParams(CALCULATOR_SUB, "abc", "abc"); }
	// testing of "multiply" functionality ==================================================================
	@Test
	void mulNormal() throws Exception { normal(CALCULATOR_MUL, 20, 2, "40"); }
	@Test
	void mulMissedFirstParam() throws Exception { missedFirstParam(CALCULATOR_MUL, 20); }
	@Test
	void mulMissedSecondParam() throws Exception { missedSecondParam(CALCULATOR_MUL, 40, "40"); }
	@Test
	void mulMissedParams() throws Exception { missedParams(CALCULATOR_MUL); }
	@Test
	void mulWrongFirst() throws Exception { wrongFirst(CALCULATOR_MUL, "abc", 20); }
	@Test
	void mulWrongSecond() throws Exception { wrongSecond(CALCULATOR_MUL, 20, "abc"); }
	@Test
	void mulWrongParams() throws Exception { wrongParams(CALCULATOR_MUL, "abc", "abc"); }
	
	// testing of "divide" functionality ==================================================================
	@Test
	void divNormal() throws Exception { normal(CALCULATOR_DIV, 80, 2, "40"); }
	@Test
	void divMissedFirstParam() throws Exception { missedFirstParam(CALCULATOR_DIV, 20); }
	@Test
	void divMissedSecondParam() throws Exception { missedSecondParam(CALCULATOR_DIV, 40, "40"); }
	@Test
	void divMissedParams() throws Exception { missedParams(CALCULATOR_DIV); }
	@Test
	void divideByZero() throws Exception {
		assertEquals(BAD_REQUEST, getHttpStatus(String.format("%s?%s=%d&%s=%d", CALCULATOR_DIV, FIRST_PARAM, 20, SECOND_PARAM, 0)));
	}
	@Test
	void divWrongFirst() throws Exception { wrongFirst(CALCULATOR_DIV, "abc", 20); }
	@Test
	void divWrongSecond() throws Exception { wrongSecond(CALCULATOR_DIV, 20, "abc"); }
	@Test
	void divWrongParams() throws Exception { wrongParams(CALCULATOR_DIV, "abc", "abc"); }
	// testing of API ==================================================================
	@Test
	void wrongApiRequest() throws Exception {
		assertEquals(NOT_FOUND, getHttpStatus(String.format("%s?%s=%d&%s=%d", "/abs", FIRST_PARAM, 20, SECOND_PARAM, 20)));
		assertEquals(NOT_FOUND, getHttpStatus(String.format ("%s?%s=%d", "/abs", SECOND_PARAM, 20)));
		assertEquals(NOT_FOUND, getHttpStatus(String.format ("%s?%s=%d", "/abs", FIRST_PARAM, 20)));
		assertEquals(NOT_FOUND, getHttpStatus(String.format ("%s", "/abs")));
	}
}
