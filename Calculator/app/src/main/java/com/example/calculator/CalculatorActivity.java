package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Stack;

public class CalculatorActivity extends AppCompatActivity {

    private TextView display;
    private String currentExpression = "";
    private boolean lastInputIsOperator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        display = findViewById(R.id.display);

        // Number buttons
        int[] numberButtonIds = {R.id.button_0, R.id.button_00, R.id.button_1, R.id.button_2, R.id.button_3,
                R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9};
        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(v -> {
                Button button = (Button) v;
                currentExpression += button.getText().toString();
                lastInputIsOperator = false;
                updateDisplay();
            });
        }

        // Operator buttons
        int[] operatorButtonIds = {R.id.button_add, R.id.button_subtract, R.id.button_multiply, R.id.button_divide};
        for (int id : operatorButtonIds) {
            findViewById(id).setOnClickListener(v -> {
                Button button = (Button) v;
                if (!lastInputIsOperator && !currentExpression.isEmpty()) {
                    currentExpression += button.getText().toString();
                    lastInputIsOperator = true;
                    updateDisplay();
                }
            });
        }

        // AC button
        findViewById(R.id.button_ac).setOnClickListener(v -> {
            currentExpression = "";
            lastInputIsOperator = false;
            updateDisplay();
        });

        // Backspace button
        findViewById(R.id.button_backspace).setOnClickListener(v -> {
            if (!currentExpression.isEmpty()) {
                currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
                lastInputIsOperator = currentExpression.endsWith("+") || currentExpression.endsWith("-") ||
                        currentExpression.endsWith("×") || currentExpression.endsWith("÷");
                updateDisplay();
            }
        });

        // Percentage button
        findViewById(R.id.button_percent).setOnClickListener(v -> {
            if (!currentExpression.isEmpty() && !lastInputIsOperator) {
                try {
                    double result = evaluateExpression(currentExpression) / 100;
                    currentExpression = String.valueOf(result);
                    lastInputIsOperator = false;
                    updateDisplay();
                } catch (Exception e) {
                    display.setText("Error");
                }
            }
        });

        // Decimal button
        findViewById(R.id.button_decimal).setOnClickListener(v -> {
            if (!lastInputIsOperator && !currentExpression.endsWith(".")) {
                currentExpression += ".";
                updateDisplay();
            }
        });

        // Equals button
        findViewById(R.id.button_equals).setOnClickListener(v -> {
            if (!currentExpression.isEmpty() && !lastInputIsOperator) {
                try {
                    double result = evaluateExpression(currentExpression);
                    currentExpression = String.valueOf(result);
                    lastInputIsOperator = false;
                    updateDisplay();
                } catch (Exception e) {
                    display.setText("Error");
                }
            }
        });
    }

    private void updateDisplay() {
        if (currentExpression.isEmpty()) {
            display.setText("0");
        } else {
            display.setText(currentExpression);
        }
    }

    private double evaluateExpression(String expression) {
        expression = expression.replace("×", "*").replace("÷", "/");
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        StringBuilder number = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                number.append(c);
            } else {
                if (number.length() > 0) {
                    numbers.push(Double.parseDouble(number.toString()));
                    number.setLength(0);
                }
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    applyOperator(numbers, operators.pop());
                }
                operators.push(c);
            }
        }
        if (number.length() > 0) {
            numbers.push(Double.parseDouble(number.toString()));
        }

        while (!operators.isEmpty()) {
            applyOperator(numbers, operators.pop());
        }

        return numbers.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if ((op1 == '+' || op1 == '-') && (op2 == '*' || op2 == '/')) {
            return false;
        }
        return true;
    }

    private void applyOperator(Stack<Double> numbers, char operator) {
        double b = numbers.pop();
        double a = numbers.pop();
        switch (operator) {
            case '+':
                numbers.push(a + b);
                break;
            case '-':
                numbers.push(a - b);
                break;
            case '*':
                numbers.push(a * b);
                break;
            case '/':
                if (b == 0) throw new ArithmeticException("Divide by zero");
                numbers.push(a / b);
                break;
        }
    }
}