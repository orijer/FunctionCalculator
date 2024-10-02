import java.util.regex.Pattern;

import Base.*;
import UnaryExpressions.*;
import BinaryExpressions.*;

/**
 * The class the contains the code that transforms a string of an expression to the correct expression objects tree.
 */
public class Parser {
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

    /**
     * @param input - An expression in string form, whose outermost operator is binary.
     * @return the index of the outermost binary operator, the one that splits the input into left and right subexpressions.
     * @throws Exception if the input has incorrect bracket format.
     */
    private static int findSplit(String input) throws Exception {
        int bracketCounters = 0;
        int length = input.length();
        for (int i = 0; i < length; i++) {
            if (input.charAt(i) == '(')
                bracketCounters++;
            if (input.charAt(i) == ')') {
                if ((--bracketCounters) < 0)
                    throw new Exception("bad brackets format in input: " + input);
            }

            if (bracketCounters == 0 && isSplitChar(input.charAt(i))) {
                return i;
            }
        }

        throw new Exception("Illegal brackets format in input: " + input);
    }

    /**
     * 
     * @param input - An expression in string form, whose outermost operator is a log.
     * @return the parsed expression including the log.
     * @throws Exception if the input has incorrect bracket format.
     */
    private static Expression handleLogs(String input) throws Exception {
        int bracketCounters = 0;
        int length = input.length();
        for (int i = 0; i < length; i++) {
            if (input.charAt(i) == '(')
                bracketCounters++;
            if (input.charAt(i) == ')') {
                if ((--bracketCounters) < 0)
                    throw new Exception("bad brackets format in input: " + input);
            }

            if (bracketCounters == 0 && input.charAt(i) == ',') { // ',' is the character that splits the left and right subexpressions in a log.
                Expression leftSubExpression = parseRec(input.substring(0, i));
                Expression rightSubExpression = parseRec(input.substring(i + 1));

                return new Log(leftSubExpression, rightSubExpression);
            }
        }

        throw new Exception("Illegal log format in input: " + input);
    }

    /**
     * @return an expression object of: left operator right
     * @throws Exception if the operator isn't supported: {+, -, *, /, ^}
     */
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

    /**
     * The recursive function that parses the input step by step.
     * @throws Exception if the input's brackets are not valid.
     */
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

        } 
        
        // Same as above but for cos:
        if (input.startsWith("cos(") && input.charAt(length - 1) == ')'
                && isBracketed("(" + input.substring(4, length - 1).trim() + ")")) { 
            return new Cos(parseRec(input.substring(4, length - 1)));

        } 
        
        // We need to handle logs a little differently:
        if (input.startsWith("log(") && input.charAt(length - 1) == ')') {
            return handleLogs(input.substring(4, length - 1).trim());
        }

        // If we reached here, we have left operator right, but we dont know yet where exactly is the outermost operator:
        int splitIndex = findSplit(input);
        Expression leftSubExpression = parseRec(input.substring(0, splitIndex));
        Expression rightSubExpression = parseRec(input.substring(splitIndex + 1));

        return combineLeftAndRightSubExpressions(leftSubExpression, rightSubExpression, input.charAt(splitIndex));
    }

    /**
     * Parses the given string and returns the corresponding expression object.
     * @param input - The string to parse.
     * @return a new expression object the corresponds to the string input.
     * @throws Exception if the input's brackets are not valid.
     */
    public static Expression parse(String input) throws Exception {
        return parseRec(input);
    }
}
