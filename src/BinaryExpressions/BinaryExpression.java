package BinaryExpressions;

import java.util.LinkedList;
import java.util.List;

import Base.BaseExpression;
import Base.Expression;

/**
 * An expression between 2 operands.
 * Every Binary expression has 2 children which are also Expressions: a left child and a right child.
 */
public abstract class BinaryExpression extends BaseExpression {
    //The left child expression.
    private Expression leftExpression;
    //The right child expression.
    private Expression rightExpression;

    /**
     * Constructor.
     * @param leftExpression - the left child expression.
     * @param rightExpression - the right child expression.
     */
    public BinaryExpression(Expression leftExpression, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    /**
     * @return the left child expression.
     */
    public Expression getLeftExpression() {
        return this.leftExpression;
    }

    /**
     * @return the right child expression.
     */
    public Expression getRightExpression() {
        return this.rightExpression;
    }

    /**
     * @return A string that symbolizes this operation.
     * For example, for Addition this should return "+" symbol.
     */
    public abstract String getSymbol();

    @Override
    public String toString() {
        //For most binary expression we can simply use:
        return  "(" + getLeftExpression().toString() + getSymbol() + getRightExpression().toString() + ")";
        //DOES NOT WORK FOR ALL BINARY OPERATIONS! FOR EXAMPLE, log.
    }

    @Override
    public List<String> getVariables() {
        List<String> variableList = new LinkedList<>();
        //Add all variables from left expression:
        List<String> variableListLeft = this.leftExpression.getVariables();
        if (variableListLeft != null) {
            variableList.addAll(variableListLeft);
        }
        //Add all new variables from right expression:
        List<String> variableListRight = this.rightExpression.getVariables();
        if (variableListRight != null) {
            for (String variable : variableListRight) {
                //If we didn't already find this variable in the left expression, add it:
                if (!variableList.contains(variable)) {
                    variableList.add(variable);
                }
            }
        }

        //Return the completed list.
        return variableList;
    }
}