import java.util.Scanner;import java.util.Scanner;

public class Novum {
    private static final String EXIT_COMMAND = "exit";
    private static final String INPUT_FIRST_NUMBER = "Please enter the first number.";
    private static final String INPUT_SECOND_NUMBER = "Please enter the second number.";
    private static final String INPUT_OPERATOR = "Please enter the operator (+, -, *, /, %)";
    private static final String NUMBER_FORMAT_ERROR = "Please enter a valid number.";
    private static final String PROGRAM_EXIT = "The program has been terminated!";

    private enum Operator {
        PLUS("+"), MINUS("-"), MULTIPLY("*"), DIVIDE("/"), MODULO("%");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public static Operator fromString(String symbol) {
            for (Operator op : values()) {
                if (op.symbol.equals(symbol)) {
                    return op;
                }
            }
            throw new IllegalArgumentException("Invalid operator: " + symbol);
        }
    }

    private final Scanner scanner;
    private double firstNumber;
    private double secondNumber;

    public Novum() {
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Novum novum = new Novum();
        try {
            novum.start();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            novum.closeScanner();
        }
    }

    private void start() {
        String input = "";
        while (!EXIT_COMMAND.equals(input)) {
            try {
                if (readFirstNumber() && readSecondNumber()) {
                    input = processOperation();
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(PROGRAM_EXIT);
    }

    private boolean readFirstNumber() {
        System.out.println(INPUT_FIRST_NUMBER);
        return readNumber(number -> firstNumber = number);
    }

    private boolean readSecondNumber() {
        System.out.println(INPUT_SECOND_NUMBER);
        return readNumber(number -> secondNumber = number);
    }

    private boolean readNumber(NumberConsumer consumer) {
        String input = scanner.nextLine();
        if (EXIT_COMMAND.equals(input)) {
            return false;
        }
        try {
            consumer.accept(Double.parseDouble(input));
            return true;
        } catch (NumberFormatException e) {
            System.out.println(NUMBER_FORMAT_ERROR);
            return false;
        }
    }

    private String processOperation() {
        System.out.println(INPUT_OPERATOR);
        String input = scanner.nextLine();

        if (!EXIT_COMMAND.equals(input)) {
            try {
                Operator operator = Operator.fromString(input);
                double result = calculateResult(operator);
                System.out.printf("%.2f %s %.2f = %.2f%n",
                        firstNumber, operator.symbol, secondNumber, result);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return input;
    }

    private double calculateResult(Operator operator) {
        return switch (operator) {
            case PLUS -> firstNumber + secondNumber;
            case MINUS -> firstNumber - secondNumber;
            case MULTIPLY -> firstNumber * secondNumber;
            case DIVIDE -> {
                if (secondNumber == 0) {
                    throw new IllegalArgumentException("Division by zero is not allowed!");
                }
                yield firstNumber / secondNumber;
            }
            case MODULO -> {
                if (secondNumber == 0) {
                    throw new IllegalArgumentException("Modulo by zero is not allowed!");
                }
                yield firstNumber % secondNumber;
            }
        };
    }

    private void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }

    @FunctionalInterface
    private interface NumberConsumer {
        void accept(double value);
    }
}