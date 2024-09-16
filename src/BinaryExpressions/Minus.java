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
        if (newLeft.equals(newRight) || newLeft.equals(newRight.reverse())) {
            return zero;
        }

        //If both the left and the right have been evaluated (to a double), we can return their numerical difference:
        if (leftEvaluated && rightEvaluated) {
            return new Num(((Num) newLeft).getNum() - ((Num) newRight).getNum());
        }

        //handle expressions of the form: (x*4)-(2*x)=>2*x
        if (newLeft instanceof Mult && newRight instanceof Mult) {
            Mult left = (Mult) newLeft;
            Mult right = (Mult) newRight;

            if (left.getLeftExpression().equals(right.getLeftExpression()) //(x*a)+(x*b)
                    || left.getLeftExpression().equals(right.getLeftExpression().reverse())) {
                return (new Mult(new Minus(left.getRightExpression(), right.getRightExpression()),
                        left.getLeftExpression())).simplify();
            }

            if (left.getLeftExpression().equals(right.getRightExpression()) //(x*a)+(b*x)
                    || left.getLeftExpression().equals(right.getRightExpression().reverse())) {
                return (new Mult(new Minus(left.getRightExpression(), right.getLeftExpression()),
                        left.getLeftExpression())).simplify();
            }

            if (left.getRightExpression().equals(right.getLeftExpression()) //(a*x)+(x*b)
                    || left.getRightExpression().equals(right.getLeftExpression().reverse())) {
                return (new Mult(new Minus(left.getLeftExpression(), right.getRightExpression()),
                        left.getRightExpression())).simplify();
            }

            if (left.getRightExpression().equals(right.getRightExpression()) //(a*x)+(b*x)
                    || left.getRightExpression().equals(right.getRightExpression().reverse())) {
                return (new Mult(new Minus(left.getLeftExpression(), right.getLeftExpression()),
                        left.getRightExpression())).simplify();
            }
        }

        //If neither is 0, they are different, and one of them still contains a variable,
        //just return a new Minus expression:
        return new Minus(newLeft, newRight);
    }

    @Override
    public Minus reverse() {
        return new Minus(getRightExpression(), getLeftExpression());
    }
}
