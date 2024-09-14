package UnaryExpressions;

import java.util.Map;

import Base.Expression;
import Base.Num;

/**
 * An expression that represents the negative of an inner expression.
 */
public class Neg extends UnaryExpression {
    /**
     * Constructor.
     * @param innerExpression - the inner expression we want to represent the negative of.
     */
    public Neg(Expression innerExpression) {
        super(innerExpression);
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        return -getInnerExpression().evaluate(assignment);
    }

    @Override
    public String toString() {
        return  "(-" + getInnerExpression().toString() + ")";
    }

    @Override
    public Expression assign(String var, Expression expression) {
        Expression assingedInnerExpression = getInnerExpression().assign(var, expression);
        return new Neg(assingedInnerExpression);
    }

    @Override
    public Expression differentiate(String var) {
        //(-f)' = - (f)'
        return new Neg(getInnerExpression().differentiate(var));
    }

    @Override
    public Expression simplify() {
        try {
            //Try to evaluate the inner expression:
            double innerValue = getInnerExpression().evaluate();

            //If the inner expression can be evaluated, return its negative:
            return new Num(-innerValue);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            //If the inner expression cant be evaluated => it still contains a variable.

            
            //We can return a new simplified Neg expression:
            return new Neg(getInnerExpression().simplify());
        }
    }
}
