package com.ace.calculator;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Calculate extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Vector<String> operands = new Vector<String>();
		Vector<String> operators = new Vector<String>();
		boolean isCheckingNumber = true;
		String input = (String)req.getParameter("val");
		String temp = "";
		//separate operators and operands
		for (int i = 0; i < input.length(); i++) {
			if (!isCheckingNumber)
				temp = "";
			if (Character.isDigit(input.charAt(i))) {
				temp += input.charAt(i);
				isCheckingNumber = true;
			} else {
				if (!temp.equals(""))
					operands.add(temp);
				isCheckingNumber = false;
				if (Character.isAlphabetic(input.charAt(i))) {
					if (input.charAt(i) == 'S' && input.charAt(i + 1) == 'i') {
						operators.add("Sin");
						i += 2;
					} else if (input.charAt(i) == 'S' && input.charAt(i + 1) == 'q') {
						operators.add("Sqrt");
						i += 3;
					} else if (input.charAt(i) == 'C') {
						operators.add("Cos");
						i += 2;
					} else if (input.charAt(i) == 'T') {
						operators.add("Tan");
						i += 2;
					}
				} else {
					operators.add(input.charAt(i) + "");
				}
			}
		}
		operands.add(temp);

		int countBinaryOpBefore = 0;
		int idxStart, idxEnd;
		double tempTotal;
		while (operators.size() > 0) {
			countBinaryOpBefore = 0;
			tempTotal = 0;
			//check for single operands - operator
			if (operators.contains("Cos") || operators.contains("Tan") || operators.contains("Sin") || operators.contains("Sqrt")) {
				idxStart = getNearestSingleOperator(operators);
				idxEnd = getFurthestSingleOperator(operators, idxStart);
				countBinaryOpBefore = getBinaryOperatorCountBefore(operators,
						idxStart);
				for (int i = idxEnd; i >= idxStart; i--) {
					tempTotal = Double.parseDouble(operands
							.get(countBinaryOpBefore));
					tempTotal = calcSingleOperator(operators.get(i), tempTotal);
					operands.set(countBinaryOpBefore, tempTotal + "");
				}
				for (int i = idxStart; i <= idxEnd; i++) {
					operators.remove(idxStart);
				}
			} else if (operators.contains("^")) {
				//check for ^ first
				idxStart = getOperatorIndex(operators, "^");
				idxEnd = idxStart + 1;
				tempTotal = calculateBinaryOperator(operators.get(idxStart),
						Double.parseDouble(operands.get(idxStart)),
						Double.parseDouble(operands.get(idxEnd)));
				operands.set(idxStart, tempTotal + "");
				operands.remove(idxEnd);
				operators.remove(idxStart);
			} else if (operators.contains("*") || operators.contains("/")) {
				//then * and /
				idxStart = getNearestBinaryOperator(operators, "*/");
				idxEnd = idxStart + 1;
				tempTotal = calculateBinaryOperator(operators.get(idxStart),
						Double.parseDouble(operands.get(idxStart)),
						Double.parseDouble(operands.get(idxEnd)));
				operands.set(idxStart, tempTotal + "");
				operands.remove(idxEnd);
				operators.remove(idxStart);
			} else if (operators.contains("+") || operators.contains("-")) {
				//lastly + and -
				idxStart = getNearestBinaryOperator(operators, "+-");
				idxEnd = idxStart + 1;
				tempTotal = calculateBinaryOperator(operators.get(idxStart),
						Double.parseDouble(operands.get(idxStart)),
						Double.parseDouble(operands.get(idxEnd)));
				operands.set(idxStart, tempTotal + "");
				operands.remove(idxEnd);
				operators.remove(idxStart);
			}
		}
		req.setAttribute("val", Float.parseFloat(operands.get(0)));
		//resp.sendRedirect("/CalculatorServlet/Calculator");
		req.getRequestDispatcher("/Calculator").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	// comparator must be */ or +-
	private int getNearestBinaryOperator(Vector<String> operators, String comparator) {
		for (int i = 0; i < operators.size(); i++) {
			if (operators.get(i).equals(comparator.charAt(0) + "")
					|| operators.get(i).equals(comparator.charAt(1) + ""))
				return i;
		}
		return -1;
	}

	private double calculateBinaryOperator(String operator, double operand1, double operand2) {
		double temp = 0;
		switch (operator.charAt(0)) {
			case '^':
				temp = Math.pow(operand1, operand2);
				break;
			case '+':
				temp = operand1 + operand2;
				break;
			case '-':
				temp = operand1 - operand2;
				break;
			case '/':
				temp = operand1 / operand2;
				break;
			case '*':
				temp = operand1 * operand2;
				break;
		}
		return temp;
	}

	private int getOperatorIndex(Vector<String> operators, String operator) {
		for (int i = 0; i < operators.size(); i++) {
			if (operators.get(i).equals(operator))
				return i;
		}
		return -1;
	}

	private double calcSingleOperator(String operator, double operand) {
		double temp = 0;
		if (operator.equals("Sin")) {
			temp = Math.sin(Math.toRadians(operand));
		} else if (operator.equals("Cos")) {
			temp = Math.cos(Math.toRadians(operand));
		} else if (operator.equals("Tan")) {
			temp = Math.tan(Math.toRadians(operand));
		} else if (operator.equals("Sqrt")) {
			temp = Math.pow(operand, 0.5);
		}
		return temp;
	}

	private int getBinaryOperatorCountBefore(Vector<String> operators, int endIdx) {
		int j = 0;
		for (int i = 0; i < endIdx; i++) {
			if (!operators.get(i).equals("Cos")
					&& !operators.get(i).equals("Tan")
					&& !operators.get(i).equals("Sin")
					&& !operators.get(i).equals("Sqrt")) {
				j++;
			} else
				continue;
		}
		return j;
	}

	private int getNearestSingleOperator(Vector<String> operators) {
		for (int i = 0; i < operators.size(); i++) {
			if (operators.get(i).equals("Cos")
					|| operators.get(i).equals("Tan")
					|| operators.get(i).equals("Sin")
					|| operators.get(i).equals("Sqrt")) {
				return i;
			}
		}
		return -1;
	}

	private int getFurthestSingleOperator(Vector<String> operators, int startIdx) {
		int j = startIdx;
		for (int i = startIdx + 1; i < operators.size(); i++) {
			if (operators.get(i).equals("Cos")
					|| operators.get(i).equals("Tan")
					|| operators.get(i).equals("Sin")
					|| operators.get(i).equals("Sqrt")) {
				j = i;
			} else
				break;
		}
		return j;
	}
}
