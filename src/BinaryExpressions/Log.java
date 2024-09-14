package BinaryExpressions;

import java.util.Map;

import Base.Expression;
import Base.Num;
import Base.Var;

/**
 * An expression that represents the logarithm operation between 2 operands such that:
 * the left expression is the base and the right expression is the argument.
 */
public class Log extends BinaryExpression {
    /**
     * Constructor.
     * @param leftExpression - the base of the logarithm.
     * @param rightExpression - the argument of the logarithm.
     */
    public Log(Expression leftExpression, Expression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        //for any given c: log(a, b) = log(c, b) / log(c, a) :
        return Math.log(getRightExpression().evaluate(assignment)) / Math.log(getLeftExpression().evaluate(assignment));
    }

    @Override
    public String getSymbol() {
        return "";
    }

   @Override
   public String toString() {
        return "log(" + getLeftExpression().toString() + ", " + getRightExpression().toString() + ")";
   }

    @Override
    public Expression assign(String var, Expression expression) {
        return null;
    }

    @Override
    public Expression differentiate(String var) {
        // (log(b, f))' = f' / (f * ln(b))
        Expression nominator = getRightExpression().differentiate(var);
        Expression denominator = new Mult(getRightExpression(), new Log(new Var("e"), getLeftExpression()));
        return new Div(nominator, denominator);
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

        //If the left and right expressions are equal, their log is 1:
        if (newLeft.equals(newRight)) {
            return new Num(1);
        }

        //If both the left and the right have been evaluated (to a double), we can return their log:
        if (leftEvaluated && rightEvaluated) {
            return new Num(Math.log(((Num) newRight).getNum()) / Math.log(((Num) newLeft).getNum()));
        }

        //If they are different, and one of them still contains a variable, just return a new log expression:
        return new Log(newLeft, newRight);
    }
}
