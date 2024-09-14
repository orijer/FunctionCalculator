package BinaryExpressions;

import java.util.Map;

import Base.Expression;
import Base.Num;
import Base.Var;

/**
 * An expression that represents the exponentiation operation between 2 operands such that:
 * the left expression is the base and the right expression is the exponent.
 */
public class Pow extends BinaryExpression {
    /**
     * Constructor.
     * @param leftExpression - the base.
     * @param rightExpression -the exponent.
     */
    public Pow(Expression leftExpression, Expression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        double result = Math.pow(getLeftExpression().evaluate(assignment), getRightExpression().evaluate(assignment));
        if (!Double.isFinite(result))
            throw new IllegalArgumentException("Tried to raise a negative number to an illegal power");

        return result;
    }

    @Override
    public String getSymbol() {
        return "^";
    }

    @Override
    public Expression assign(String var, Expression expression) {
        //Assign the left:
        Expression assingedLeftExpression = getLeftExpression().assign(var, expression);
        //Assign the right:
        Expression assingedRightExpression = getRightExpression().assign(var, expression);

        //Combine the assigned parts:
        return new Pow(assingedLeftExpression, assingedRightExpression);
    }

    @Override
    public Expression differentiate(String var) {
        // (f^g)' = (f^g) * ((f' * (g / f) + g' * ln(f))
        //Source: https://math.stackexchange.com/questions/1588166/derivative-of-functions-of-the-form-fxgx

        //f^g :
        Expression left = new Pow(getLeftExpression(), getRightExpression());
        //f' :
        Expression right1 = getLeftExpression().differentiate(var);
        //g / f :
        Expression right2 = new Div(getRightExpression(), getLeftExpression());
        //g' :
        Expression right3 = getRightExpression().differentiate(var);
        //ln(f) :
        Expression right4 = new Log(new Var("e"), getLeftExpression());
        //Calculate the entire right expression:
        Expression right = new Plus(new Mult(right1, right2), new Mult(right3, right4));

        //Return the complete expression:
        return new Mult(left, right);
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

        if (rightEvaluated) {
            //If both the left and the right have been evaluated (to a double), we can return their numerical exp:
            if (leftEvaluated) {
                double result = Math.pow(((Num) newLeft).getNum(), ((Num) newRight).getNum());
                if (!Double.isFinite(result))
                    throw new IllegalArgumentException("Tried to raise a negative number to an illegal power");

                return new Num(result);
            }

            //If the right has been evaluated to 1, left^right = left:
            if (newRight.equals(new Num(1.0)))
                return newLeft;
        }

        //Handle expressions of the form: x^y^z => x^(y*z)
        if (newLeft instanceof Pow) {
            Pow powLeft = (Pow) newLeft;
            return new Pow(powLeft.getLeftExpression(), new Mult(powLeft.getRightExpression(), newRight));
        } else if (newRight instanceof Pow) {
            Pow powRight = (Pow) newRight;
            return new Pow(newLeft, new Mult(powRight.getLeftExpression(), powRight.getRightExpression()));
        }

        //If one of them still contains a variable, just return a new Pow expression:
        return new Pow(newLeft, newRight);
    }

    @Override
    public Pow reverse() {
        return new Pow(getRightExpression(), getLeftExpression());
    }
}
