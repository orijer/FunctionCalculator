import java.util.Stack;
import java.util.regex.Pattern;

import Base.*;
import UnaryExpressions.*;
import BinaryExpressions.*;

public class Parser {
    public static String addBracketsAroundUnaryOperands(String input) {
        StringBuilder sb = new StringBuilder();
        int length = input.length();
        int currentBracket = 0;
        Stack<Integer> bracketOpenings = new Stack<>();

        for (int i = 0; i < length; i++) {
            if (input.charAt(i) == '(') {
                currentBracket++;

            } else if (input.charAt(i) == ')') {
                currentBracket--;

                // Check if we now need to close the last bracket we created:
                if (!bracketOpenings.empty() && bracketOpenings.peek() == currentBracket) {
                    sb.append(')');
                    bracketOpenings.pop();
                }

            } else if (i < length - 3 && input.charAt(i + 3) == '(') {
                if (input.charAt(i) == 's' && input.charAt(i + 1) == 'i' && input.charAt(i + 2) == 'n'
                        || input.charAt(i) == 'c' && input.charAt(i + 1) == 'o' && input.charAt(i + 2) == 's'
                        || input.charAt(i) == 'l' && input.charAt(i + 1) == 'o' && input.charAt(i + 2) == 'g') {
                    sb.append('(');
                    bracketOpenings.add(currentBracket);
                }
            }

            sb.append(input.charAt(i));
        }

        return sb.toString();
    }

    /**
     * @return true IFF the input is a string with legal brackets surrounded by another pair of brackets.
     */
    private static boolean isBracketed(String input) {
        int length = input.length();
        if (input.charAt(0) != '(' || input.charAt(length - 1) != ')')
            return false;

        int bracketsCounter = 0;
        for (int i = 1; i < length - 1; i++) {
            if (input.charAt(i) == '(')
                bracketsCounter++;

            if (input.charAt(i) == ')') {
                if ((--bracketsCounter) < 0)
                    return false;
            }
        }

        return (bracketsCounter == 0);
    }

    /**
     * @return true IFF the given char is in the set: {+, -, *, /, ^}
     */
    private static boolean isSplitChar(char ch) {
        return (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^');
    }

    public static int findSplit(String input) throws Exception {
        int bracketCounters = 0;
        int length = input.length();
        for (int i = 0; i < length; i++) {
            if (input.charAt(i) == '(')
                bracketCounters++;
            if (input.charAt(i) == ')') {
                if ((--bracketCounters) < 0)
                    throw new Exception("bad format");
            }

            if (bracketCounters == 0 && isSplitChar(input.charAt(i))) {
                return i;
            }
        }

        throw new Exception("Illegal brackets format in input: " + input);
    }

    private static Expression handleLogs(String input) throws Exception {
        int bracketCounters = 0;
        int length = input.length();
        for (int i = 0; i < length; i++) {
            if (input.charAt(i) == '(')
                bracketCounters++;
            if (input.charAt(i) == ')') {
                if ((--bracketCounters) < 0)
                    throw new Exception("bad format");
            }

            if (bracketCounters == 0 && input.charAt(i) == ',') {
                Expression leftSubExpression = parseRec(input.substring(0, i));
                Expression rightSubExpression = parseRec(input.substring(i + 1));

                return new Log(leftSubExpression, rightSubExpression);
            }
        }

        throw new Exception("Illegal brackets format in input: " + input);
    }

    private static Expression combineLeftAndRightSubExpressions(Expression left, Expression right, char operator)
            throws Exception {
        switch (operator) {
            case '+':
                return new Plus(left, right);
            case '-':
                return new Minus(left, right);
            case '*':
                return new Mult(left, right);
            case '/':
                return new Div(left, right);
            case '^':
                return new Pow(left, right);
        }

        throw new Exception("Operator: " + operator + " not supported");
    }

    private static Expression parseRec(String input) throws Exception {
        input = input.trim();
        int length = input.length();

        // If we just have: (string), parse the string without the brackets:
        if (isBracketed(input))
            return parseRec(input.substring(1, length - 1));

        // If the input doesnt contains any operators, it is either a number or a variable:
        String regex = "[+\\-*/^]"; // It turns out '-' is used for range, so we need a '\' before it, which can only be achieved using \\. 
        if (!Pattern.compile(regex).matcher(input).find() && !input.contains("sin(")
                && !input.contains("cos(") && !input.contains("log(")) {
            try {
                return new Num(Double.parseDouble(input));
            } catch (Exception e) {
                return new Var(input);
            }
        }

        // If the input is of the form: sin(something), parse the something and put it inside a sin object:
        if (input.startsWith("sin(") && input.charAt(length - 1) == ')'
                && isBracketed("(" + input.substring(4, length - 1).trim() + ")")) {
            return new Sin(parseRec(input.substring(4, length - 1)));
        } else if (input.startsWith("cos(") && input.charAt(length - 1) == ')'
                && isBracketed("(" + input.substring(4, length - 1).trim() + ")")) { //same as above but for cos:
            return new Cos(parseRec(input.substring(4, length - 1)));
        }
        if (input.startsWith("log(") && input.charAt(length - 1) == ')') {
            return handleLogs(input.substring(4, length - 1).trim());
        }

        int splitIndex = findSplit(input);
        Expression leftSubExpression = parseRec(input.substring(0, splitIndex));
        Expression rightSubExpression = parseRec(input.substring(splitIndex + 1));

        return combineLeftAndRightSubExpressions(leftSubExpression, rightSubExpression, input.charAt(splitIndex));
    }

    public static Expression parse(String input) throws Exception {
        return parseRec(input);
    }
}
