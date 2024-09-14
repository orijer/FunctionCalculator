package BinaryExpressions;

import java.util.Map;

import Base.Expression;
import Base.Num;

/**
 * An expression that represents the addition operation between 2 operands.
 */
public class Plus extends BinaryExpression {
    /**
     * Constructor. The order of the received parameters is not important.
     * @param leftExpression - the augend.
     * @param rightExpression - the addend.
     */
     public Plus(Expression leftExpression, Expression rightExpression) {
         super(leftExpression, rightExpression);
     }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        return getLeftExpression().evaluate(assignment) + getRightExpression().evaluate(assignment);
    }

    @Override
    public String getSymbol() {
        return " + ";
    }

    @Override
    public Expression assign(String var, Expression expression) {
        //Assign the left:
        Expression assingedLeftExpression = getLeftExpression().assign(var, expression);
        //Assign the right:
        Expression assingedRightExpression = getRightExpression().assign(var, expression);

        //Combine the assigned parts:
        return new Plus(assingedLeftExpression, assingedRightExpression);
    }

    @Override
    public Expression differentiate(String var) {
        //(f + g)' = f' + g'
        return new Plus(getLeftExpression().differentiate(var), getRightExpression().differentiate(var));
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
        } catch (Exception e) {
            //We can't evaluate the left expression, so just simplify it:
            newLeft = getLeftExpression().simplify();
            leftEvaluated = false;
        }

        //Try to evaluate the right expression:
        try {
            newRight = new Num(getRightExpression().evaluate());
            rightEvaluated = true;
        } catch (Exception e) {
            //We can't evaluate the right expression, so just simplify it:
            newRight = getRightExpression().simplify();
            rightEvaluated = false;
        }

        Num zero = new Num(0);
        //If the left is 0, their sum equals the right expression.
        if (newLeft.equals(zero)) {
            return newRight;
        }

        //If the right is 0, their sum equals the left expression.
        if (newRight.equals(zero)) {
            return newLeft;
        }

        //If both the left and the right have been evaluated (to a double), we can return their numerical sum:
        if (leftEvaluated && rightEvaluated) {
            return new Num(((Num) newLeft).getNum() + ((Num) newRight).getNum());
        }

        //If neither is 0, and one of them still contains a variable, just return a new Plus expression:
        return new Plus(newLeft, newRight);
    }
}
