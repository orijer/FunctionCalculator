package Base;

import java.util.List;
import java.util.Map;

/**
 * Represents a Double in the expression.
 */
public class Num implements Expression {
    //The value of the number in the expression.
    private double num;

    /**
     * Constructor.
     * @param num - the value (type: double) of the number in the expression.
     */
    public Num(double num) {
        this.num = num;
    }

    /**
     * @return the value of the number in the expression.
     */
    public double getNum() {
        return num;
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        //The value of a number is itself:
        return this.num;
    }

    @Override
    public double evaluate() throws Exception {
        return evaluate(null);
    }

    @Override
    public List<String> getVariables() {
        //A number contains no variables:
        return null;
    }

    @Override
    public String toString() {
        return Double.toString(this.num);
    }

    @Override
    public Expression assign(String var, Expression expression) {
        //Assigning a number should just return the number:
        return new Num(this.num);
    }

    @Override
    public Expression differentiate(String var) {
        //the derivative of any number is 0:
        return new Num(0);
    }

    @Override
    public Expression simplify() {
        return new Num(this.num);
    }

    /**
     * @param other - another Num object.
     * @return true IFF other and this represents the same number in the expression.
     */
    public boolean equals(Expression other) {
        if (other.getClass() != Num.class) {
            return false;
        }

        return this.num == ((Num) other).num;
    }
}
