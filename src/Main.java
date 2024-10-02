import java.util.Scanner;

import Base.Expression;
import Base.Num;
import Base.Var;
import BinaryExpressions.Plus;
import BinaryExpressions.Mult;

public class Main {
    public static void printMainMenu() {
        int optionIndex = 0;
        for (MenuOption option : MenuOption.values()) {
            System.out.println((optionIndex++) + ") " + option.getDescription());
        }

        System.out.println("");
    }

    public static void printExpression(Expression expression) {
        System.out.println("The current expression is: \n" + expression + "\n");
    }

    public static Expression enterNewExpression(Scanner reader) {
        try {
            String expression = reader.nextLine();
            return Parser.parse(expression);
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("Encountered an error while parsing a new expression.\nMake sure it is legal!");

            return null; // If the user input doesn't give a valid expression, we just return null.
        }
    }

    public static void evaluateExpression(Expression expression) { //TODO: ask the user for assignments to variables
        try {
            System.out.println(expression + " = " + expression.evaluate() + "\n");
        } catch (Exception e) {
            System.out.println("There was a problem evaluating the given expression.\n");
        }
    }

    public static Expression simplifyExpression(Expression expression) {
        Expression simplifiedExpression = expression.simplify();
        System.out.println(expression + "\nWas simplified to: \n" + simplifiedExpression + "\n");

        return simplifiedExpression;
    }

    public static Expression differentiateExpression(Expression expression, String by) {
        Expression simplifiedExpression = expression.differentiate(by);
        System.out.println(expression + "\nWas differentiated by " + by + " to: \n" + simplifiedExpression + "\n");

        return simplifiedExpression;
    }

    public static void main(String[] args) {
        MenuOption menuOption = MenuOption.PRINT_EXPRESSION;
        Scanner reader = new Scanner(System.in);

        Expression currentExpression = new Plus(new Var("x"), new Mult(new Num(8), new Num(5)));

        while (menuOption != MenuOption.EXIT) {
            printMainMenu();

            try {
                menuOption = MenuOption.fromValue(Integer.parseInt(reader.nextLine()));
                System.out.println("");
            } catch (Exception e) {
                System.out.println("Please enter only a number between 0 and 5.\n");
                continue;
            }

            switch (menuOption) {
                case EXIT:
                    break;
                case PRINT_EXPRESSION:
                    printExpression(currentExpression);
                    break;
                case ENTER_NEW_EXPRESSION:
                    Expression newExpression = enterNewExpression(reader);
                    if (newExpression != null)
                        currentExpression = newExpression;
                    break;
                case EVALUATE_EXPRESSION:
                    evaluateExpression(currentExpression);
                    break;
                case SIMPLIFY_EXPRESSION:
                    currentExpression = simplifyExpression(currentExpression);
                    break;
                case DIFFERENTIATE_EXPRESSION:
                    System.out.println("Input by which variable to differentiate by:");
                    String diffBy = reader.nextLine();
                    currentExpression = differentiateExpression(currentExpression, diffBy);
                    break;
            }
        }

        reader.close();
    }

    // The enum that makes the menu option more readable.
    private enum MenuOption {
        EXIT(0, "Exit"),
        PRINT_EXPRESSION(1, "Print the current expression"),
        ENTER_NEW_EXPRESSION(2, "Enter a new Expression"),
        EVALUATE_EXPRESSION(3, "Try to evaluate the current expression"),
        SIMPLIFY_EXPRESSION(4, "Simplify the current Expression"),
        DIFFERENTIATE_EXPRESSION(5, "Differentiate the current expression");

        private final int value;
        private final String description;

        MenuOption(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return this.value;
        }

        public String getDescription() {
            return this.description;
        }

        public static MenuOption fromValue(int value) throws Exception {
            for (MenuOption option : values()) {
                if (option.getValue() == value) {
                    return option;
                }
            }

            throw new Exception("Unsupported value for type MenuOption");
        }
    }

}
