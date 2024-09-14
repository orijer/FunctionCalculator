package UnaryExpressions;

import java.util.Map;

import Base.Expression;
import Base.Num;
import BinaryExpressions.Mult;

/**
 * An expression that represents the trigonometric function 'Sine'.
 */
public class Sin extends UnaryExpression {
    /**
     * Constructor.
     * @param innerExpression - the inner expression. We get: cos(innerExpression).
     */
    public Sin(Expression innerExpression) {
        super(innerExpression);
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        return Math.sin(Math.toRadians(getInnerExpression().evaluate(assignment)));
    }

    @Override
    public String toString() {
        return  "sin(" + getInnerExpression().toString() + ")";
    }

    @Override
    public Expression assign(String var, Expression expression) {
        Expression assingedInnerExpression = getInnerExpression().assign(var, expression);
        return new Sin(assingedInnerExpression);
    }

    @Override
    public Expression differentiate(String var) {
        //(sin(f))' = cos(f) * f'
        return new Mult(new Cos(getInnerExpression()), getInnerExpression().differentiate(var));
    }

    @Override
    public Expression simplify() {
        try {
            //Try to evaluate the inner expression:
            double innerValue = getInnerExpression().evaluate();

            //If the inner expression can be evaluated, return its sine:
            return new Num(Math.sin(Math.toRadians(innerValue)));
        } catch (Exception e) {
            //If the inner expression cant be evaluated => it still contains a variable.
            //We can return a new simplified Sin expression:
            return new Sin(getInnerExpression().simplify());
        }
    }
}
