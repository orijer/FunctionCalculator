package UnaryExpressions;

import java.util.Map;

import Base.Expression;
import Base.Num;
import BinaryExpressions.Mult;

/**
 * An expression that represents the trigonometric function 'Cosine'.
 */
public class Cos extends UnaryExpression {
    /**
     * Constructor.
     * @param innerExpression - the inner expression. We get: cos(innerExpression).
     */
    public Cos(Expression innerExpression) {
        super(innerExpression);
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        return Math.cos(Math.toRadians(getInnerExpression().evaluate(assignment)));
    }

    @Override
    public String toString() {
        return  "cos(" + getInnerExpression().toString() + ")";
    }

    @Override
    public Expression assign(String var, Expression expression) {
        Expression assingedInnerExpression = getInnerExpression().assign(var, expression);
        return new Cos(assingedInnerExpression);
    }

    @Override
    public Expression differentiate(String var) {
        //(cos(f))' = - sin(f) * f'
        return new Neg(new Mult(new Sin(getInnerExpression()), getInnerExpression().differentiate(var)));
    }

    @Override
    public Expression simplify() {
        try {
            //Try to evaluate the inner expression:
            double innerValue = getInnerExpression().evaluate();

            //If the inner expression can be evaluated, return its cosine:
            return new Num(Math.cos(Math.toRadians(innerValue)));
        } catch (Exception e) {
            //If the inner expression cant be evaluated => it still contains a variable.
            //We can return a new simplified Cos expression:
            return new Cos(getInnerExpression().simplify());
        }
    }
}
