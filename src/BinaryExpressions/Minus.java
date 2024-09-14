package BinaryExpressions;

import java.util.Map;

import Base.Expression;
import Base.Num;
import UnaryExpressions.Neg;

/**
 * An expression that represents the subtraction operation between 2 operands such that:
 * the left expression is the minuend (subtracted from) and the right expression is the subtrahend (subtracting by).
 */
public class Minus extends BinaryExpression {
    /**
     * Constructor.
     * @param leftExpression - the minuend (subtracted from) of the expression.
     * @param rightExpression - the subtrahend (subtracting by) of the expression.
     */
    public Minus(Expression leftExpression, Expression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        return getLeftExpression().evaluate(assignment) - getRightExpression().evaluate(assignment);
    }

    @Override
    public String getSymbol() {
        return " - ";
    }

    @Override
    public Expression assign(String var, Expression expression) {
        //Assign the left:
        Expression assingedLeftExpression = getLeftExpression().assign(var, expression);
        //Assign the right:
        Expression assingedRightExpression = getRightExpression().assign(var, expression);

        //Recombine the assigned parts:
        return new Minus(assingedLeftExpression, assingedRightExpression);
    }

    @Override
    public Expression differentiate(String var) {
        //(f - g)' = f' - g'
        return new Minus(getLeftExpression().differentiate(var), getRightExpression().differentiate(var));
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
        //If the left is 0, their difference equals minus the new right expression.
        if (newLeft.equals(zero)) {
            return new Neg(newRight);
        }

        //If the right is 0, their difference equals the new left expression.
        if (newRight.equals(zero)) {
            return newLeft;
        }

        //If the left and right expressions are equal, their difference is 0:
        if (newLeft.equals(newRight)) {
            return zero;
        }

        //If both the left and the right have been evaluated (to a double), we can return their numerical difference:
        if (leftEvaluated && rightEvaluated) {
            return new Num(((Num) newLeft).getNum() - ((Num) newRight).getNum());
        }

        //If neither is 0, they are different, and one of them still contains a variable,
        //just return a new Minus expression:
        return new Minus(newLeft, newRight);
    }
}
