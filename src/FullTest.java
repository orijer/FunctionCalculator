
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedList;

import Base.Expression;
import Base.Num;
import Base.Var;
import BinaryExpressions.Div;
import BinaryExpressions.Log;
import BinaryExpressions.Minus;
import BinaryExpressions.Mult;
import BinaryExpressions.Plus;
import BinaryExpressions.Pow;
import UnaryExpressions.Cos;
import UnaryExpressions.Neg;
import UnaryExpressions.Sin;

public class FullTest {
    public Stats plusTests() {
        Stats plusStats = new Stats("Plus Tests");
        Expression ex;

        // Test 0:
        ex = new Plus(new Num(10), new Num(10));
        plusStats.testDone(0, ex.toString().equals("(10.0 + 10.0)"));

        // Test 1:
        ex = new Plus(new Num(10), new Var("x"));
        plusStats.testDone(1, ex.toString().equals("(10.0 + x)"));

        // Test 2:
        ex = new Plus(new Var("x"), new Num(10));
        plusStats.testDone(2, ex.toString().equals("(x + 10.0)"));

        // Test 3:
        ex = new Plus(new Var("x"), new Var("x"));
        plusStats.testDone(3, ex.toString().equals("(x + x)"));

        // x+0 or 0+x => x

        // Test 4:
        ex = new Plus(new Var("x"), new Num(0));
        plusStats.testDone(4, ex.simplify().toString().equals("x"));

        // Test 5:
        ex = new Plus(new Pow(new Var("x"), new Var("y")), new Num(0));
        plusStats.testDone(5, ex.simplify().toString().equals("(x^y)"));

        // Test 6:
        ex = new Plus(new Num(0), new Pow(new Var("x"), new Var("y")));
        plusStats.testDone(6, ex.simplify().toString().equals("(x^y)"));

        // Test 7:
        ex = new Plus(new Num(0), new Var("x"));
        plusStats.testDone(7, ex.simplify().toString().equals("x"));

        // derivatives

        // Test 8:
        ex = new Plus(new Num(10), new Var("x"));
        plusStats.testDone(8, ex.differentiate("x").toString().equals("(0.0 + 1.0)"));

        return plusStats;
    }

    public Stats minusTests() {
        Stats minusStats = new Stats("Minus Stats");
        Expression ex;

        // Test 0:
        ex = new Minus(new Num(10), new Num(10));
        minusStats.testDone(0, ex.toString().equals("(10.0 - 10.0)"));

        // Test 1:
        ex = new Minus(new Num(10), new Var("x"));
        minusStats.testDone(1, ex.toString().equals("(10.0 - x)"));

        // Test 2:
        ex = new Minus(new Var("x"), new Num(10));
        minusStats.testDone(2, ex.toString().equals("(x - 10.0)"));

        // Test 3:
        ex = new Minus(new Var("x"), new Var("x"));
        minusStats.testDone(3, ex.toString().equals("(x - x)"));

        // x-0 => x

        // Test 4:
        ex = new Minus(new Var("x"), new Num(0));
        minusStats.testDone(4, ex.simplify().toString().equals("x"));

        // Test 5:
        ex = new Minus(new Div(new Num(10), new Var("x")), new Num(0));
        minusStats.testDone(5, ex.simplify().toString().equals("(10.0 / x)"));

        // 0-x => -x

        // Test 6:
        ex = new Minus(new Num(0), new Var("x"));
        minusStats.testDone(6, ex.simplify().toString().equals("(-x)"));

        // Test 7:
        ex = new Minus(new Num(0), new Cos(new Var("y")));
        minusStats.testDone(7, ex.simplify().toString().equals("(-cos(y))"));

        // x-x => 0

        // Test 8:
        ex = new Minus(new Var("x"), new Var("x"));
        minusStats.testDone(8, ex.simplify().toString().equals("0.0"));

        // Test 9:
        ex = new Minus(new Div(new Num(10), new Var("x")), new Div(new Num(10), new Var("x")));
        minusStats.testDone(9, ex.simplify().toString().equals("0.0"));

        // Test 10:
        ex = new Minus(new Plus(new Var("x"), new Var("y")), new Plus(new Var("y"), new Var("x")));
        minusStats.testDone(10, ex.simplify().toString().equals("0.0"));

        // derivatives

        // Test 11:
        ex = new Minus(new Num(10), new Var("x"));
        minusStats.testDone(11, ex.differentiate("x").toString().equals("(0.0 - 1.0)"));

        return minusStats;
    }

    public Stats multTests() {
        Stats multStats = new Stats("Mult Stats");
        Expression ex;

        // Test 0:
        ex = new Mult(new Num(10), new Num(10));
        multStats.testDone(0, ex.toString().equals("(10.0 * 10.0)"));

        // Test 1:
        ex = new Mult(new Num(10), new Var("x"));
        multStats.testDone(1, ex.toString().equals("(10.0 * x)"));

        // Test 2:
        ex = new Mult(new Var("x"), new Num(10));
        multStats.testDone(2, ex.toString().equals("(x * 10.0)"));

        // Test 3:
        ex = new Mult(new Var("x"), new Var("x"));
        multStats.testDone(3, ex.toString().equals("(x * x)"));

        // x*1 or 1*x => x

        // Test 4:
        ex = new Mult(new Var("x"), new Num(1));
        multStats.testDone(4, ex.simplify().toString().equals("x"));

        // Test 5:
        ex = new Mult(new Mult(new Var("x"), new Num(8)), new Num(1));
        multStats.testDone(5,
                (ex.simplify().toString().equals("(x * 8.0)") || ex.simplify().toString().equals("(8.0 * x)")));

        // Test 6:
        ex = new Mult(new Num(1), new Mult(new Var("x"), new Num(8)));
        multStats.testDone(6,
                (ex.simplify().toString().equals("(x * 8.0)") || ex.simplify().toString().equals("(8.0 * x)")));

        // Test 7:
        ex = new Mult(new Num(1), new Var("x"));
        multStats.testDone(7, ex.simplify().toString().equals("x"));

        // x*0 or 0*x => 0

        // Test 8:
        ex = new Mult(new Var("x"), new Num(0));
        multStats.testDone(8, ex.simplify().toString().equals("0.0"));

        // Test 9:
        ex = new Mult(new Pow(new Mult(new Num(3), new Num(2)), new Num(10)), new Num(0));
        multStats.testDone(9, ex.simplify().toString().equals("0.0"));

        // Test 10:
        ex = new Mult(new Num(0), new Var("x"));
        multStats.testDone(10, ex.simplify().toString().equals("0.0"));

        // Test 11:
        ex = new Mult(new Num(0), new Pow(new Mult(new Num(10), new Num(50)), new Num(50)));
        multStats.testDone(11, ex.simplify().toString().equals("0.0"));

        // (x^y)*(x^z) => x^(y+z)

        // Test 12:
        ex = new Mult(new Pow(new Var("x"), new Num(2)), new Pow(new Var("x"), new Num(3)));
        multStats.testDone(12, ex.simplify().toString().equals("(x^5.0)"));

        // (x^z)*(y^z) => (x*y)^z

        // Test 13:
        ex = new Mult(new Pow(new Var("x"), new Num(3)), new Pow(new Var("y"), new Num(3)));
        multStats.testDone(13, ex.simplify().toString().equals("((x * y)^3.0)"));

        // derivatives

        // Test 14:
        ex = new Mult(new Num(10), new Var("x"));
        multStats.testDone(14, ex.differentiate("x").toString().equals("((0.0 * x) + (10.0 * 1.0))"));

        return multStats;
    }

    public Stats divTests() {
        Stats divStats = new Stats("Div Stats");
        Expression ex;

        // Test 0:
        ex = new Div(new Num(10), new Num(10));
        divStats.testDone(0, ex.toString().equals("(10.0 / 10.0)"));

        // Test 1:
        ex = new Div(new Num(10), new Var("x"));
        divStats.testDone(1, ex.toString().equals("(10.0 / x)"));

        // Test 2:
        ex = new Div(new Var("x"), new Num(10));
        divStats.testDone(2, ex.toString().equals("(x / 10.0)"));

        // Test 3:
        ex = new Div(new Var("x"), new Var("x"));
        divStats.testDone(3, ex.toString().equals("(x / x)"));

        // x/1 => x

        // Test 4:
        ex = new Div(new Var("x"), new Num(1));
        divStats.testDone(4, ex.simplify().toString().equals("x"));

        // Test 5:
        ex = new Div(new Pow(new Num(10), new Var("y")), new Num(1));
        divStats.testDone(5, ex.simplify().toString().equals("(10.0^y)"));

        // x/x => 1

        // Test 6:
        ex = new Div(new Var("x"), new Var("x"));
        divStats.testDone(6, ex.simplify().toString().equals("1.0"));

        // Test 7:
        ex = new Div(new Mult(new Num(9), new Var("x")), new Mult(new Num(9), new Var("x")));
        divStats.testDone(7, ex.simplify().toString().equals("1.0"));

        // Test 8:
        ex = new Div(new Num(5), new Num(5));
        divStats.testDone(8, ex.simplify().toString().equals("1.0"));

        // Test 9:
        ex = new Div(new Plus(new Var("x"), new Var("y")), new Plus(new Var("y"), new Var("x")));
        divStats.testDone(9, ex.simplify().toString().equals("1.0"));

        // (x^y)/(z^y) => (x/z)^y

        // Test 10:
        ex = new Div(new Pow(new Var("x"), new Num(2)), new Pow(new Var("y"), new Num(2)));
        divStats.testDone(10, ex.simplify().toString().equals("((x / y)^2.0)"));

        // (x^y)/(x^z) => x^(y-z)

        // Test 11:
        ex = new Div(new Pow(new Var("x"), new Num(5)), new Pow(new Var("x"), new Num(3)));
        divStats.testDone(11, ex.simplify().toString().equals("(x^2.0)"));

        // derivatives

        // Test 12:
        ex = new Div(new Num(10), new Var("x"));
        divStats.testDone(4, ex.differentiate("x").toString().equals("(((0.0 * x) - (10.0 * 1.0)) / (x^2.0))"));

        return divStats;
    }

    public Stats powTests() {
        Stats powStats = new Stats("Pow Stats");
        Expression ex;

        // Test 0:
        ex = new Pow(new Num(10), new Num(10));
        powStats.testDone(0, ex.toString().equals("(10.0^10.0)"));

        // Test 1:
        ex = new Pow(new Num(10), new Var("x"));
        powStats.testDone(1, ex.toString().equals("(10.0^x)"));

        // Test 2:
        ex = new Pow(new Var("x"), new Num(10));
        powStats.testDone(2, ex.toString().equals("(x^10.0)"));

        // Test 3:
        ex = new Pow(new Var("x"), new Var("x"));
        powStats.testDone(3, ex.toString().equals("(x^x)"));

        // x^1 => x

        // Test 4:
        ex = new Pow(new Var("x"), new Num(1));
        powStats.testDone(4, ex.simplify().toString().equals("x"));

        // Test 5:
        ex = new Pow(new Pow(new Var("x"), new Var("y")), new Num(1));
        powStats.testDone(5, ex.simplify().toString().equals("(x^y)"));

        // (x^y)^z => x^(y*z)

        // Test 6:
        ex = new Pow(new Pow(new Var("x"), new Var("y")), new Var("z"));
        powStats.testDone(6, ex.simplify().toString().equals("(x^(y * z))"));

        // derivatives

        // Test 7:
        ex = new Pow(new Num(10), new Var("x"));
        powStats.testDone(7,
                ex.differentiate("x").toString().equals("((10.0^x) * ((0.0 * (x / 10.0)) + (1.0 * log(e, 10.0))))"));

        // Test 8:
        ex = new Pow(new Var("x"), new Var("x"));
        powStats.testDone(8,
                ex.differentiate("x").toString().equals("((x^x) * ((1.0 * (x / x)) + (1.0 * log(e, x))))"));

        // Test 9:
        ex = new Pow(new Var("x"), new Num(3));
        powStats.testDone(9,
                ex.differentiate("x").toString().equals("((x^3.0) * ((1.0 * (3.0 / x)) + (0.0 * log(e, x))))"));

        // Test 10:
        ex = new Pow(new Var("x"), new Num(4));
        powStats.testDone(10,
                ex.differentiate("x").toString().equals("((x^4.0) * ((1.0 * (4.0 / x)) + (0.0 * log(e, x))))"));

        // Test 11:
        ex = new Pow(new Var("e"), new Var("x"));
        powStats.testDone(11,
                ex.differentiate("x").toString().equals("((e^x) * ((0.0 * (x / e)) + (1.0 * log(e, e))))"));

        // Test 12:
        powStats.testDone(12, ex.simplify().toString().equals("(e^x)"));

        return powStats;
    }

    public Stats logTests() {
        Stats logStats = new Stats("Log Stats");
        Expression ex;

        // Test 0:
        ex = new Log(new Num(10), new Num(10));
        logStats.testDone(0, ex.toString().equals("log(10.0, 10.0)"));

        // Test 1:
        ex = new Log(new Num(10), new Var("x"));
        logStats.testDone(1, ex.toString().equals("log(10.0, x)"));

        // Test 2:
        ex = new Log(new Var("x"), new Num(10));
        logStats.testDone(2, ex.toString().equals("log(x, 10.0)"));

        // Test 3:
        ex = new Log(new Var("x"), new Var("x"));
        logStats.testDone(3, ex.toString().equals("log(x, x)"));

        // log (x,x) => 1

        // Test 4:
        ex = new Log(new Var("x"), new Var("x"));
        logStats.testDone(4, ex.simplify().toString().equals("1.0"));

        // Test 5:
        ex = new Log(new Num(105), new Num(105));
        logStats.testDone(5, ex.simplify().toString().equals("1.0"));

        // Test 6:
        ex = new Log(new Sin(new Pow(new Num(10), new Var("x"))), new Sin(new Pow(new Num(10), new Var("x"))));
        logStats.testDone(6, ex.simplify().toString().equals("1.0"));

        // Test 7:
        ex = new Log(new Plus(new Var("x"), new Var("y")), new Plus(new Var("y"), new Var("x")));
        logStats.testDone(7, ex.simplify().toString().equals("1.0"));

        // log rules

        // Test 8:
        ex = new Log(new Num(2), new Num(8));
        logStats.testDone(8, ex.simplify().toString().equals("3.0"));

        // Test 9:
        ex = new Log(new Num(2), new Var("x"));
        logStats.testDone(9, ex.simplify().toString().equals("log(2.0, x)"));

        //base > 0

        // Test 10:
        ex = new Log(new Num(-1), new Num(10));
        try {
            ex.evaluate();
            logStats.testDone(10, false);
        } catch (Exception e) {
            logStats.testDone(10, true);
        }

        // base != 1

        // Test 11:
        ex = new Log(new Num(1), new Num(10));
        try {
            ex.evaluate();
            logStats.testDone(11, false);
        } catch (Exception e) {
            logStats.testDone(11, true);
        }

        // log argument > 0

        // Test 12:
        ex = new Log(new Num(5), new Num(0));
        try {
            ex.evaluate();
            logStats.testDone(12, false);
        } catch (Exception e) {
            logStats.testDone(12, true);
        }

        // derivatives

        // Test 13:
        ex = new Log(new Num(10), new Var("x"));
        logStats.testDone(13, ex.differentiate("x").toString().equals("(1.0 / (x * log(e, 10.0)))"));

        return logStats;
    }

    public Stats cosTests() {
        Stats cosStats = new Stats("Cos Stats");
        Expression ex;

        // Test 0:
        ex = new Cos(new Num(10));
        cosStats.testDone(0, ex.toString().equals("cos(10.0)"));

        // Test 1:
        ex = new Cos(new Var("x"));
        cosStats.testDone(1, ex.toString().equals("cos(x)"));

        // Test 2:
        ex = new Cos(new Num(0));
        try {
            cosStats.testDone(2, (ex.evaluate() == 1));
        } catch (Exception e) {
            cosStats.testDone(2, false);
        }

        // Test 3:
        ex = new Cos(new Var("x"));
        try {
            ex.evaluate();
            cosStats.testDone(3, false);
        } catch (Exception e2) {
            cosStats.testDone(3, true);
        }

        // Test 4:
        ex = new Cos(new Num(180));
        try {
            cosStats.testDone(4, (ex.evaluate() == -1.0));
        } catch (Exception e) {
            cosStats.testDone(4, false);
        }

        // derivatives

        // Test 5:
        ex = new Cos(new Plus(new Var("x"), new Var("y")));
        cosStats.testDone(5, (ex.differentiate("x").toString().equals("(-(sin((x + y)) * (1.0 + 0.0)))"))
                || (ex.differentiate("x").toString().equals("((-sin((x + y)) * (1.0 + 0.0)))")));

        return cosStats;
    }

    public Stats sinTests() {
        Stats sinStats = new Stats("Sin Stats");
        Expression ex;

        // Test 0:
        ex = new Sin(new Num(10));
        sinStats.testDone(0, ex.toString().equals("sin(10.0)"));

        // Test 1:
        ex = new Sin(new Var("x"));
        sinStats.testDone(1, ex.toString().equals("sin(x)"));

        // Test 2:
        ex = new Sin(new Num(180));
        try {
            sinStats.testDone(2, (ex.evaluate() == 1.2246467991473532E-16));
        } catch (Exception e) {
            sinStats.testDone(2, false);
        }

        // derivatives

        // Test 3:
        ex = new Cos(new Num(10));
        sinStats.testDone(3, (ex.differentiate("x").toString().equals("(-(sin(10.0) * 0.0))")
                || (ex.differentiate("x").toString().equals("((-sin(10.0)) * 0.0)"))));

        // Test 4:
        ex = new Cos(new Var("x"));
        sinStats.testDone(4, (ex.differentiate("x").toString().equals("(-(sin(x) * 1.0))")));

        return sinStats;
    }

    public Stats negTests() {
        Stats negStats = new Stats("Neg Stats");
        Expression ex;

        // Test 0:
        ex = new Neg(new Num(10));
        negStats.testDone(0, ex.toString().equals("(-10.0)"));

        // Test 1:
        ex = new Neg(new Var("x"));
        negStats.testDone(1, ex.toString().equals("(-x)"));

        // Test 2:
        ex = new Neg(new Num(-1));
        negStats.testDone(2, ex.toString().equals("(--1.0)"));

        // Test 3:
        ex = new Neg(new Num(-1));
        negStats.testDone(3, ex.simplify().toString().equals("1.0"));

        // Test 4:
        ex = new Neg(new Num(1));
        negStats.testDone(4, ex.simplify().toString().equals("-1.0"));

        // Test 5:
        ex = new Neg(new Var("x"));
        negStats.testDone(5, ex.toString().equals("(-x)"));

        return negStats;
    }

    public Stats numvarTests() {
        Stats nvStats = new Stats("Num & Var Stats");
        Expression ex;

        // Test 0:
        ex = new Pow(new Num(2),
                new Minus(new Num(5), new Pow(new Num(2), new Plus(new Num(0.5), new Sin(new Cos(new Num(0)))))));
        nvStats.testDone(0, ex.simplify().toString().equals("11.864461183774617"));

        // Test 1:
        ex = new Pow(new Num(-2), new Num(0.5));
        nvStats.testDone(1, ex.toString().equals("(-2.0^0.5)"));

        // Test 2:
        try {
            ex.evaluate();
            nvStats.testDone(2, false);
        } catch (Exception e2) {
            nvStats.testDone(2, true);
        }

        // Test 3:
        try {
            ex.simplify();
            nvStats.testDone(3, false);
        } catch (Exception e2) {
            nvStats.testDone(3, true);
        }

        // Test 4:
        ex = new Div(new Num(10), new Num(0));
        try {
            ex.evaluate();
            nvStats.testDone(4, false);
        } catch (Exception e) {
            nvStats.testDone(4, true);
        }

        // derivatives

        // Test 5:
        ex = new Num(10);
        nvStats.testDone(5, ex.differentiate("x").toString().equals("0.0"));

        // Test 6:
        ex = new Var("x");
        nvStats.testDone(6, ex.differentiate("x").toString().equals("1.0"));

        // Test 7:
        ex = new Var("y");
        nvStats.testDone(7, ex.differentiate("x").toString().equals("0.0"));

        return nvStats;
    }

    public Stats nestedTests() {
        Stats nestedStats = new Stats("Nested Stats");
        Expression ex;

        // Test 0:
        ex = new Sin(new Pow(new Mult(new Plus(new Mult(new Num(2), new Var("x")), new Var("y")), new Num(4)),
                new Var("x")));
        nestedStats.testDone(0, ex.toString().equals("sin(((((2.0 * x) + y) * 4.0)^x))"));

        // Test 1:
        List<String> vars = ex.getVariables();
        nestedStats.testDone(1, (vars.get(0).equals("x") && vars.get(1).equals("y")));

        // Test 2:
        Expression ex2 = ex.assign("x", new Num(10));
        nestedStats.testDone(2, ex2.toString().equals("sin(((((2.0 * 10.0) + y) * 4.0)^10.0))"));

        // Test 3:
        ex2 = ex.assign("x", ex);
        nestedStats.testDone(3, ex2.toString().equals(
                "sin(((((2.0 * sin(((((2.0 * x) + y) * 4.0)^x))) + y) * 4.0)^sin(((((2.0 * x) + y) * 4.0)^x))))"));

        // Test 4:
        ex = new Pow(new Plus(new Var("x"), new Var("y")), new Num(2));
        Map<String, Double> assignment = new TreeMap<String, Double>();
        assignment.put("x", 2.0);
        assignment.put("y", 4.0);
        try {
            nestedStats.testDone(4, (ex.evaluate(assignment) == 36));
        } catch (Exception e) {
            nestedStats.testDone(4, false);
        }

        // Test 5:
        ex = new Pow(new Plus(new Var("x"), new Var("y")), new Num(2));
        nestedStats.testDone(5, ex.differentiate("x").toString()
                .equals("(((x + y)^2.0) * (((1.0 + 0.0) * (2.0 / (x + y))) + (0.0 * log(e, (x + y)))))"));

        // Test 6:
        ex = new Pow(new Plus(new Var("x"), new Var("y")), new Num(2));
        nestedStats.testDone(6,
                ex.differentiate("x").simplify().toString().equals("(2.0 * (x + y))"));

        // Test 7:
        ex = new Plus(new Mult(new Plus(new Num(3), new Num(6)), new Var("x")),
                new Mult(new Mult(new Num(4), new Var("x")), new Sin(new Num(0))));
        nestedStats.testDone(7, ex.simplify().toString().equals("(9.0 * x)"));

        // (a*x)+(x*b) => (a+b)*x

        // Test 8:
        ex = new Plus(new Mult(new Var("x"), new Num(2)), new Mult(new Var("x"), new Num(4)));
        nestedStats.testDone(8, ex.simplify().toString().equals("(6.0 * x)"));

        return nestedStats;
    }

    public void printFinalMessage(List<Stats> allStats) {
        System.out.println("\nFinished!\n");

        for (Stats stats : allStats) {
            if (!stats.result()) {
                System.out.println("Not all tests passed successfully!");
                return;
            }
        }

        System.out.println("All tests passed successfully!\n");
    }

    public void runTests() {
        System.out.println("Starting tests:\n");

        List<Stats> allStats = new LinkedList<>();

        allStats.add(plusTests());
        allStats.add(minusTests());
        allStats.add(multTests());
        allStats.add(divTests());
        allStats.add(powTests());
        allStats.add(logTests());
        allStats.add(cosTests());
        allStats.add(sinTests());
        allStats.add(negTests());
        allStats.add(numvarTests());
        allStats.add(nestedTests());

        for (Stats stats : allStats) {
            stats.print();
        }

        printFinalMessage(allStats);
    }

    public static void main(String[] args) {
        FullTest tests = new FullTest();
        tests.runTests();
    }

    /**
     * A class that contains all the information about a signle test group.
     */
    private class Stats {
        private String testName;
        private int successes;
        private int total;
        private List<Integer> failures;

        public Stats(String testName) {
            this.testName = testName;
            this.successes = 0;
            this.total = 0;
            this.failures = new LinkedList<>();
        }

        public void testDone(int testID, boolean result) {
            this.total++;

            if (result)
                this.successes++;
            else
                this.failures.add(testID);
        }

        public void print() {
            System.out.println(this.testName);
            System.out.println("tests passed: " + this.successes + "/" + this.total);

            if (this.successes < this.total) {
                System.out.print("failed in tests:\n");
                for (int testID : this.failures) {
                    System.out.print(":  " + testID + "  :");
                }
            }

            System.out.println();
        }

        public boolean result() {
            return (this.successes == this.total);
        }
    }
}
