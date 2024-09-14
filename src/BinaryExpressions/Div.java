package BinaryExpressions;

import java.util.Map;

import Base.Expression;
import Base.Num;

/**
 * An expression that represents division between 2 operands - .
 */
public class Div extends BinaryExpression {
    /**
     * Constructor. We get leftExpression / rightExpression.
     * @param leftExpression - the left child expression.
     * @param rightExpression - the right child expression.
     */
    public Div(Expression leftExpression, Expression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        double result = getLeftExpression().evaluate(assignment) / getRightExpression().evaluate(assignment);
        if (!Double.isFinite(result))
            throw new IllegalArgumentException("Problem evaluating a div");

        return result;
    }

    @Override
    public String getSymbol() {
        return " / ";
    }

    @Override
    public Expression assign(String var, Expression expression) {
        //Assign the left:
        Expression assingedLeftExpression = getLeftExpression().assign(var, expression);
        //Assign the right:
        Expression assingedRightExpression = getRightExpression().assign(var, expression);

        //Recombine the assigned parts:
        return new Div(assingedLeftExpression, assingedRightExpression);
    }

    @Override
    public Expression differentiate(String var) {
        //(f / g)' = ((f' * g) - (f * g')) / (g^2)
        Expression leftNumerator = new Mult(getLeftExpression().differentiate(var), getRightExpression());
        Expression rightNumerator = new Mult(getLeftExpression(), getRightExpression().differentiate(var));
        Expression numerator = new Minus(leftNumerator, rightNumerator);
        Expression denominator = new Pow(getRightExpression(), new Num(2));
        return new Div(numerator, denominator);
    }

    @Override
    public Expression simplify() {
        Expression newLeft;
        Expression newRight;
        boolean leftEvaluated, rightEvaluated;

        //Try to evaluate the left expression:
        try {
            newLeft = new Num(getLeftExpression().evaluate());
            leftEvaluated = true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            //We can't evaluate the left expression, so just simplify it:
            newLeft = getLeftExpression().simplify();
            leftEvaluated = false;
        }

        //Try to evaluate the right expression:
        try {
            newRight = new Num(getRightExpression().evaluate());
            rightEvaluated = true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            //We can't evaluate the right expression, so just simplify it:
            newRight = getRightExpression().simplify();
            rightEvaluated = false;
        }

        //If the right is 1, their quotient equals the left expression.
        Num one = new Num(1);
        if (newRight.equals(one)) {
            return newLeft;
        }

        //If the left and right expressions are equal, their quotient is 1:
        if (newLeft.equals(newRight) || newLeft.equals(newRight.reverse())) {
            return one;
        }

        //If both the left and the right have been evaluated (to a double), we can return their numerical quotient:
        if (leftEvaluated && rightEvaluated) {
            double result = ((Num) newLeft).getNum() / ((Num) newRight).getNum();
            if (!Double.isFinite(result))
                throw new IllegalArgumentException("Problem simplifying a div");
                
            return new Num(result);
        }

        //If the right isn't 1, they are different, and one of them still contains a variable,
        //just return a new Div expression:
        return new Div(newLeft, newRight);
    }

    @Override
    public Div reverse() {
        return new Div(getRightExpression(), getLeftExpression());
    }
}
