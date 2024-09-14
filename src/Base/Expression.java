package Base;

import java.util.List;
import java.util.Map;

/**
 * An interface to represent a mathematical expression.
 * We can use variables, evaluate and differentiate an expression.
 */
public interface Expression {
    /**
     * @param assignment - the assignment of the variables to a double value.
     * @return return the value of the expression after assigning all the variables from assignment.
     * If the expression contains a variable which is not in the assignment, an exception is thrown.
     */
    double evaluate(Map<String, Double> assignment) throws Exception;

    /**
     * A convenience method. Like the evaluate(assignment) method above, but uses an empty assignment.
     * @return the value of the expression if it contains no variables, and throws an exception otherwise.
     */
    double evaluate() throws Exception;

    /**
     * @return a list of the variables in the expression.
     */
    List<String> getVariables();

    /**
     * @return  a nice string representation of the expression.
     */
    String toString();

    /**
     * Switches all occurrences of a given variable to a given expression.
     * @param var - the variable to be assigned to.
     * @param expression - the expression to be replaced with.
     * @return a new expression in which all occurrences of the variable var are replaced with
     * the provided expression (Does not modify the current expression).
     */
    Expression assign(String var, Expression expression);

    /**
     * @param var - the variable to be differentiated by.
     * @return the expression tree resulting from differentiating the current expression relative to variable 'var'.
     */
    Expression differentiate(String var);

    /**
     * @return a simplified version of the current expression.
     */
    Expression simplify();

    /**
     * @param other - another Expression object.
     * @return - true IFF this and other represent the exact same expression.
     */
    boolean equals(Expression other);
}
