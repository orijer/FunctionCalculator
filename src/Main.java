import java.util.Scanner;

import Base.Expression;
import Base.Num;
import Base.Var;
import BinaryExpressions.Plus;
import BinaryExpressions.Mult;

public class Main {
    public static void printMainMenu() {
        System.out.println("0) Exit \n"
                + "1) Print the current expression \n"
                + "2) Enter a new Expression \n"
                + "3) Try to evaluate the current expression \n"
                + "4) Simplify the current Expression \n"
                + "5) Differentiate the current expression \n");
    }

    public static void printExpression(Expression expression) {
        System.out.println("The current expression is: \n" + expression + "\n");
    }

    public static void evaluateExpression(Expression expression) {
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
        int menuOption = 1;
        Scanner reader = new Scanner(System.in);

        Expression currentExpression = new Plus(new Var("x"), new Mult(new Num(8), new Num(5)));

        while (menuOption != 0) {
            printMainMenu();
            try {
                menuOption = Integer.parseInt(reader.nextLine());
                System.out.println("");
            } catch (Exception e) {
                System.out.println("Please enter only a number between 0 and 5.\n");
                continue;
            }

            switch (menuOption) {
                case 1:
                    printExpression(currentExpression);
                    break;
                case 2:
                    try {
                        String expression = reader.nextLine();
                        currentExpression = Parser.parse(expression);
                    } catch (Exception e) {
                        if (e.getMessage() != null)
                            System.out.println(e.getMessage());
                        e.printStackTrace();                        
                        System.out.println("Encountered an error while parsing a new expression.\nMake sure it is legal!");
                    }
                    break;
                case 3:
                    evaluateExpression(currentExpression);
                    break;
                case 4:
                    currentExpression = simplifyExpression(currentExpression);
                    break;
                case 5:
                    System.out.println("Input by which variable to differentiate by:");
                    String diffBy = reader.nextLine();
                    currentExpression = differentiateExpression(currentExpression, diffBy);
                    break;
            }
        }

        reader.close();
    }
}
