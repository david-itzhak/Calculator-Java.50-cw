package telran.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import telran.spring.service.interfaces.Calculator;
import static telran.spring.api.ApiConstants.*;
import static telran.spring.service.interfaces.Calculator.*;

@RestController
public class CalculatorController {

	@Autowired
	Calculator calculator;

	// http://localhost:8080/calculator/add?op1=10&op2=20
	@GetMapping(value = CALCULATOR_ADD)
	ResponseEntity<?> add(@RequestParam(name = FIRST_PARAM, required = true) int op1,
			@RequestParam(name = SECOND_PARAM, defaultValue = "0") int op2) {
		return getRespons(op1, op2, ADD);
	}

	// http://localhost:8080/calculator/subtract?op1=10&op2=20
	@GetMapping(value = CALCULATOR_SUB)
	ResponseEntity<?> sub(@RequestParam(name = FIRST_PARAM, required = true) int op1,
			@RequestParam(name = SECOND_PARAM, defaultValue = "0") int op2) {
		return getRespons(op1, op2, SUB);
	}

	// http://localhost:8080/calculator/multiply?op1=10&op2=20
	@GetMapping(value = CALCULATOR_MUL)
	ResponseEntity<?> mul(@RequestParam(name = FIRST_PARAM, required = true) int op1,
			@RequestParam(name = SECOND_PARAM, defaultValue = "1") int op2) {
		return getRespons(op1, op2, MUL);
	}

	// http://localhost:8080/calculator/divide?op1=30&op2=0
	@GetMapping(value = CALCULATOR_DIV)
	ResponseEntity<?> div(@RequestParam(name = FIRST_PARAM, required = true) int op1,
			@RequestParam(name = SECOND_PARAM, defaultValue = "1") int op2) {
		return getRespons(op1, op2, DIV);
	}

	private ResponseEntity<?> getRespons(int op1, int op2, String operation) {
		try {
			return ResponseEntity.ok(calculator.calculate(op1, op2, operation));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
