package telran.spring.service.impl;

import org.springframework.stereotype.Service;
import telran.spring.service.interfaces.Calculator;

@Service
public class CalculatorSimpleImpl implements Calculator {

	@Override
	public int calculate(int op1, int op2, String operation) {
		switch (operation) {
		case ADD: return op1 + op2;
		case SUB: return op1 - op2;
		case MUL: return op1 * op2;
		case DIV: return op1 / op2;
		default: throw new RuntimeException("Unknow operation");
		}
	}
}
