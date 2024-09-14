package BinaryExpressions;

import java.util.Map;

import Base.Expression;
import Base.Num;

/**
 * An expression that represents the Multiplication operation between 2 operands.
 */
public class Mult extends BinaryExpression {
    /**
     * Constructor. The order of the received parameters is not important.
     * @param leftExpression - the multiplier.
     * @param rightExpression - the multiplicand.
     */
    public Mult(Expression leftExpression, Expression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        return getLeftExpression().evaluate(assignment) * getRightExpression().evaluate(assignment);
    }

    @Override
    public String getSymbol() {
        return " * ";
    }

    @Override
    public Expression assign(String var, Expression expression) {
        //Assign the left:
        Expression assingedLeftExpression = getLeftExpression().assign(var, expression);
        //Assign the right:
        Expression assingedRightExpression = getRightExpression().assign(var, expression);

        //Recombine the assigned parts:
        return new Mult(assingedLeftExpression, assingedRightExpression);
    }

    @Override
    public Expression differentiate(String var) {
        //(f * g)' = (f' * g) + (f * g')
        Expression leftDerivative = new Mult(getLeftExpression().differentiate(var), getRightExpression());
        Expression rightDerivative = new Mult(getLeftExpression(), getRightExpression().differentiate(var));
        return new Plus(leftDerivative, rightDerivative);
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

        Num zero = new Num(0);
        //If the left is 0, their product equals 0.
        if (newLeft.equals(zero)) {
            return zero;
        }

        //If the right is 0, their product equals 0.
        if (newRight.equals(zero)) {
            return zero;
        }

        Num one = new Num(1);
        //If the left is 1, their product equals the right expression:
        if (newLeft.equals(one)) {
            return newRight;
        }

        //If the right is 1, their product equals the left expression:
        if (newRight.equals(one)) {
            return newLeft;
        }

        //If both the left and the right have been evaluated (to a double), we can return their numerical product:
        if (leftEvaluated && rightEvaluated) {
            return new Num(((Num) newLeft).getNum() * ((Num) newRight).getNum());
        }

        //If neither is 0, 1, and one of them still contains a variable, just return a new expression for the product:
        return new Mult(newLeft, newRight);
    }

    @Override
    public Mult reverse() {
        return new Mult(getRightExpression(), getLeftExpression());
    }
}
