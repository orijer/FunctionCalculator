package Base;

/**
 * The most basic Math expression we can get.
 */
public abstract class BaseExpression implements Expression {
    @Override
    public double evaluate() throws Exception {
        //We use the evaluate(Map<> assignment) which we have from the Expression interface, using an empty map:
        return evaluate(null);
    }

    @Override
    public boolean equals(Expression other) {
        return this.toString().equals(other.toString());
    }
}
