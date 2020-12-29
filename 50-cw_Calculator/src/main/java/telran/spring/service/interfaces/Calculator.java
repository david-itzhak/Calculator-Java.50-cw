package telran.spring.service.interfaces;

public interface Calculator {

	int calculate(int op1, int op2, String operation);

	String ADD = "add";
	String SUB = "subtract";
	String MUL = "multiply";
	String DIV = "divide";
}
