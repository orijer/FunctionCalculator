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

        //handle expressions of the form: (x*2)+(3*x)=>5*x
        if (newLeft instanceof Mult && newRight instanceof Mult) {
            Mult left = (Mult) newLeft;
            Mult right = (Mult) newRight;

            if (left.getLeftExpression().equals(right.getLeftExpression()) //(x*a)+(x*b)
                || left.getLeftExpression().equals(right.getLeftExpression().reverse())) {
                return (new Mult(new Plus(left.getRightExpression(), right.getRightExpression()), left.getLeftExpression())).simplify();
            }

            if (left.getLeftExpression().equals(right.getRightExpression()) //(x*a)+(b*x)
                    || left.getLeftExpression().equals(right.getRightExpression().reverse())) {
                return (new Mult(new Plus(left.getRightExpression(), right.getLeftExpression()), left.getLeftExpression())).simplify();
            }

            if (left.getRightExpression().equals(right.getLeftExpression()) //(a*x)+(x*b)
                    || left.getRightExpression().equals(right.getLeftExpression().reverse())) {
                return (new Mult(new Plus(left.getLeftExpression(), right.getRightExpression()), left.getRightExpression())).simplify();
            }

            if (left.getRightExpression().equals(right.getRightExpression()) //(a*x)+(b*x)
                    || left.getRightExpression().equals(right.getRightExpression().reverse())) {
                return (new Mult(new Plus(left.getLeftExpression(), right.getLeftExpression()), left.getRightExpression())).simplify();
            }
        }

        // Handle simplification of some repeated addition:
        if (newLeft instanceof Plus) {
            Plus left = (Plus) newLeft;

            if (newRight instanceof Num) {
                if (left.getLeftExpression() instanceof Num) {
                    // (num1 + x) + num2 => (num1 + num2) + x
                    return (new Plus(new Plus(left.getLeftExpression(), newRight), left.getRightExpression()))
                            .simplify();
                } else if (left.getRightExpression() instanceof Num) {
                    // (x + num1) + num2 => (num1 + num2) + x
                    return (new Plus(new Plus(left.getRightExpression(), newRight), left.getLeftExpression()))
                            .simplify();
                }
            }

            if (newRight instanceof Plus) {
                Plus right = (Plus) newRight;

                if (left.getLeftExpression() instanceof Num) {
                    if (right.getLeftExpression() instanceof Num) {
                        // (num1 + x) + (num2 + y) => (num1 + num2) + (x + y)
                        return (new Plus(new Plus(left.getLeftExpression(), right.getLeftExpression()),
                                new Plus(left.getRightExpression(), right.getRightExpression()))).simplify();
                    }

                    if (right.getRightExpression() instanceof Num) {
                        // (num1 + x) + (y + num2) => (num1 + num2) + (x + y)
                        return (new Plus(new Plus(left.getLeftExpression(), right.getRightExpression()),
                                new Plus(left.getRightExpression(), right.getLeftExpression()))).simplify();
                    }
                }

                if (left.getRightExpression() instanceof Num) {
                    if (right.getLeftExpression() instanceof Num) {
                        // (x + num1) + (num2 + y) => (num1 + num2) + (x + y)
                        return (new Plus(new Plus(left.getRightExpression(), right.getLeftExpression()),
                                new Plus(left.getLeftExpression(), right.getRightExpression()))).simplify();
                    }

                    if (right.getRightExpression() instanceof Num) {
                        // (x + num1) + (y + num2) => (num1 + num2) + (x + y)
                        return (new Plus(new Plus(left.getRightExpression(), right.getRightExpression()),
                                new Plus(left.getLeftExpression(), right.getLeftExpression()))).simplify();
                    }
                }
            }
        } else if (newRight instanceof Plus && newLeft instanceof Num) {
            Plus right = (Plus) newRight;

            if (right.getLeftExpression() instanceof Num) {
                // num1 + (num2 + x) => (num1 + num2) + x
                return (new Plus(new Plus(newLeft, right.getLeftExpression()), right.getRightExpression())).simplify();
            }

            if (right.getRightExpression() instanceof Num) {
                // num1 + (x + num2) => (num1 + num2) + x
                return (new Plus(new Plus(newLeft, right.getRightExpression()), right.getLeftExpression())).simplify();
            }
        }

        //If neither is 0, and one of them still contains a variable, just return a new Plus expression:
        return new Plus(newLeft, newRight);
    }

    @Override
    public Plus reverse() {
        return new Plus(getRightExpression(), getLeftExpression());
    }
}
