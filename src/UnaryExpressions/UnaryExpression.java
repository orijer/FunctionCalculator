package UnaryExpressions;

import java.util.LinkedList;
import java.util.List;

import Base.BaseExpression;
import Base.Expression;

/**
 * Does an operation on a single operand.
 * Every Unary Expression has 1 child called the inner expression.
 */
public abstract class UnaryExpression extends BaseExpression {
    //The inner expression of this.
    private Expression innerExpression;

    /**
     * Constructor.
     * @param innerExpression - the inner expression.
     */
    public UnaryExpression(Expression innerExpression) {
        this.innerExpression = innerExpression;
    }

    /**
     * @return the inner expression.
     */
    public Expression getInnerExpression() {
        return innerExpression;
    }

    @Override
    public List<String> getVariables() {
        List<String> variableList = new LinkedList<>();
        //Add all variables from inner expression:
        List<String> innerVariableList = this.innerExpression.getVariables();
        if (innerVariableList != null) {
            variableList.addAll(innerVariableList);
        }

        //Return the completed list:
        return variableList;
    }
}
