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

        if (newLeft instanceof Pow) {
            Pow left = (Pow) newLeft;

            //Handle expressions of the form: (x^y)*x => x^(y+1)
            if (left.getLeftExpression().equals(newRight) || left.getLeftExpression().equals(newRight.reverse())) {
                newRight = new Pow(newRight, new Num(1.0)); //we then treat it as (x^y)*(x^z)
            }

            if (newRight instanceof Pow) {
                Pow right = (Pow) newRight;

                //Handle expressions of the form: (x^y)*(x^z) => x^(y+z)
                if (left.getLeftExpression().equals(right.getLeftExpression())
                        || left.getLeftExpression().equals(right.getLeftExpression().reverse())) {
                    Plus newPower = new Plus(left.getRightExpression(), right.getRightExpression());
                    return new Pow(left.getLeftExpression(), newPower.simplify());
                }

                //Handle expressions of the form: (x^z)*(y^z) => (x*y)^z
                if (left.getRightExpression().equals(right.getRightExpression())
                        || left.getRightExpression().equals(right.getRightExpression().reverse())) {
                    return new Pow(new Mult(left.getLeftExpression(), right.getLeftExpression()),
                            left.getRightExpression());
                }
            }

            if (newRight instanceof Div) {
                Div right = (Div) newRight;
                if (left.getLeftExpression().equals(right.getRightExpression())
                        || left.getLeftExpression().equals(right.getRightExpression().reverse())) {
                    // (x ^ y) * (z / x) => z * (x ^ (y - 1))
                    return (new Mult(right.getLeftExpression(),
                            new Pow(left.getLeftExpression(), new Minus(left.getRightExpression(), new Num(1)))))
                            .simplify();
                }
            }
        } else if (newRight instanceof Pow) { //Handle x*(x^y) => x^(1+y)
            Pow right = (Pow) newRight;
            if (newLeft.equals(right.getLeftExpression()) || newLeft.equals(right.getLeftExpression().reverse())) {
                Plus newPower = new Plus(new Num(1.0), right.getRightExpression());
                return new Pow(newLeft, newPower);
            }
        }

        // Handle simplification of some repeated multiplications:
        if (newLeft instanceof Mult) {
            Mult left = (Mult) newLeft;

            if (newRight instanceof Num) {
                if (left.getLeftExpression() instanceof Num) {
                    // (num1 * x) * num2 => (num1 * num2) * x
                    return (new Mult(new Mult(left.getLeftExpression(), newRight), left.getRightExpression()))
                            .simplify();
                } else if (left.getRightExpression() instanceof Num) {
                    // (x * num1) * num2 => (num1 * num2) * x
                    return (new Mult(new Mult(left.getRightExpression(), newRight), left.getLeftExpression()))
                            .simplify();
                }
            }

            if (newRight instanceof Mult) {
                Mult right = (Mult) newRight;

                if (left.getLeftExpression() instanceof Num) {
                    if (right.getLeftExpression() instanceof Num) {
                        // (num1 * x) * (num2 * y) => (num1 * num2) * (x * y)
                        return (new Mult(new Mult(left.getLeftExpression(), right.getLeftExpression()),
                                new Mult(left.getRightExpression(), right.getRightExpression()))).simplify();
                    }

                    if (right.getRightExpression() instanceof Num) {
                        // (num1 * x) * (y * num2) => (num1 * num2) * (x * y)
                        return (new Mult(new Mult(left.getLeftExpression(), right.getRightExpression()),
                                new Mult(left.getRightExpression(), right.getLeftExpression()))).simplify();
                    }
                }

                if (left.getRightExpression() instanceof Num) {
                    if (right.getLeftExpression() instanceof Num) {
                        // (x * num1) * (num2 * y) => (num1 * num2) * (x * y)
                        return (new Mult(new Mult(left.getRightExpression(), right.getLeftExpression()),
                                new Mult(left.getLeftExpression(), right.getRightExpression()))).simplify();
                    }

                    if (right.getRightExpression() instanceof Num) {
                        // (x * num1) * (y * num2) => (num1 * num2) * (x * y)
                        return (new Mult(new Mult(left.getRightExpression(), right.getRightExpression()),
                                new Mult(left.getLeftExpression(), right.getLeftExpression()))).simplify();
                    }
                }
            }
        } else if (newRight instanceof Mult && newLeft instanceof Num) {
            Mult right = (Mult) newRight;

            if (right.getLeftExpression() instanceof Num) {
                // num1 * (num2 * x) => (num1 * num2) * x
                return (new Mult(new Mult(newLeft, right.getLeftExpression()), right.getRightExpression())).simplify();
            }

            if (right.getRightExpression() instanceof Num) {
                // num1 * (x * num2) => (num1 * num2) * x
                return (new Mult(new Mult(newLeft, right.getRightExpression()), right.getLeftExpression())).simplify();
            }
        }

        // If we found no further simplification:
        return new Mult(newLeft, newRight);
    }

    @Override
    public Mult reverse() {
        return new Mult(getRightExpression(), getLeftExpression());
    }
}
