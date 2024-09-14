package Base;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represent a variable in the expression. We can later assign the variable a double value.
 */
public class Var implements Expression {
    //The variable name.
    private String var;

    /**
     * Constructor.
     * @param var - the name of the variable.
     */
    public Var(String var) {
        this.var = var;
    }

    /**
     * @return the name of the variable.
     */
    public String getVar() {
        return this.var;
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        //If the assignment doesn't contain this variable, throw an exception:
        if (!assignment.containsKey(this.var)) {
            throw new Exception("Variable not found in the assignment Map.");
        }
        //The assignment contains this variable, return its value:
        return assignment.get(this.var);
    }

    @Override
    public double evaluate() throws Exception {
        throw new Exception("Variable not found in the assignment Map.");
    }

    @Override
    public List<String> getVariables() {
        //A Var contains only one variable: itself.
        List<String> variableList = new LinkedList<>();
        variableList.add(this.var);
        return variableList;
    }

    @Override
    public String toString() {
        return this.var;
    }

    @Override
    public Expression assign(String var, Expression expression) {
        //If this is the variable to be assigned to, return the new expression instead:
        if (this.var.equals(var)) {
            return expression;
        }

        //It should be kept as is:
        return new Var(this.var);
    }

    @Override
    public Expression differentiate(String var) {
        //If we differentiate by this variable, then the derivative of it is 1:
        if (this.var.equals(var)) {
            return new Num(1);
        }

        //We differentiate by a different variable, then this variable is just a number,
        //So it's derivative is 0:
        return new Num(0);
    }

    @Override
    public Expression simplify() {
        return new Var(this.var);
    }

    /**
     * @param other - another Var object.
     * @return true IFF other and this represents the same variable in the expression.
     */
    public boolean equals(Expression other) {
        if (other.getClass() != Var.class) {
            return false;
        }

        return this.var.equals(((Var) other).var);
    }

    @Override
    public Expression reverse() {
        return this;
    }
}
