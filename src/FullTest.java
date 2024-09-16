
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    public static boolean plusTests() {
        Expression ex;

        ex = new Plus(new Num(10), new Num(10));
        if (!ex.toString().equals("(10.0 + 10.0)"))
            System.out.println("#1:Error in" + ex);
        ex = new Plus(new Num(10), new Var("x"));
        if (!ex.toString().equals("(10.0 + x)"))
            System.out.println("#2:Error in" + ex);
        ex = new Plus(new Var("x"), new Num(10));
        if (!ex.toString().equals("(x + 10.0)"))
            System.out.println("#3:Error in" + ex);
        ex = new Plus(new Var("x"), new Var("x"));
        if (!ex.toString().equals("(x + x)"))
            System.out.println("#4:Error in" + ex);

        //x+0 or 0+x => x
        ex = new Plus(new Var("x"), new Num(0));
        if (!ex.simplify().toString().equals("x"))
            System.out.println("#39:Error in simplify of" + ex.simplify());
        ex = new Plus(new Pow(new Var("x"), new Var("y")), new Num(0));
        if (!ex.simplify().toString().equals("(x^y)"))
            System.out.println("#40:Error in simplify of" + ex.simplify());
        ex = new Plus(new Num(0), new Pow(new Var("x"), new Var("y")));
        if (!ex.simplify().toString().equals("(x^y)"))
            System.out.println("#41:Error in simplify of" + ex.simplify());
        ex = new Plus(new Num(0), new Var("x"));
        if (!ex.simplify().toString().equals("x"))
            System.out.println("#42:Error in simplify of" + ex.simplify());

        //derivatives
        ex = new Plus(new Num(10), new Var("x"));
        if (!ex.differentiate("x").toString().equals("(0.0 + 1.0)"))
            System.out.println("#81:Error in Differentitation of:" + ex);

        return true;
    }

    public static boolean minusTests() {
        Expression ex;

        ex = new Minus(new Num(10), new Num(10));
        if (!ex.toString().equals("(10.0 - 10.0)"))
            System.out.println("#5:Error in" + ex);
        ex = new Minus(new Num(10), new Var("x"));
        if (!ex.toString().equals("(10.0 - x)"))
            System.out.println("#6:Error in" + ex);
        ex = new Minus(new Var("x"), new Num(10));
        if (!ex.toString().equals("(x - 10.0)"))
            System.out.println("#7:Error in" + ex);
        ex = new Minus(new Var("x"), new Var("x"));
        if (!ex.toString().equals("(x - x)"))
            System.out.println("#8:Error in" + ex);

        //x-0 => x
        ex = new Minus(new Var("x"), new Num(0));
        if (!ex.simplify().toString().equals("x"))
            System.out.println("#48:Error in simplify of" + ex.simplify());
        ex = new Minus(new Div(new Num(10), new Var("x")), new Num(0));
        if (!ex.simplify().toString().equals("(10.0 / x)"))
            System.out.println("#49:Error in simplify of" + ex.simplify());

        //0-x => -x
        ex = new Minus(new Num(0), new Var("x"));
        if (!ex.simplify().toString().equals("(-x)"))
            System.out.println("#50:Error in simplify of" + ex.simplify());
        ex = new Minus(new Num(0), new Cos(new Var("y")));
        if (!ex.simplify().toString().equals("(-cos(y))"))
            System.out.println("#51:Error in simplify of" + ex.simplify());

        //x-x => 0
        ex = new Minus(new Var("x"), new Var("x"));
        if (!ex.simplify().toString().equals("0.0"))
            System.out.println("#52:Error in simplify of" + ex.simplify());
        ex = new Minus(new Div(new Num(10), new Var("x")), new Div(new Num(10), new Var("x")));
        if (!ex.simplify().toString().equals("0.0"))
            System.out.println("#53:Error in simplify of" + ex.simplify());
        ex = new Minus(new Plus(new Var("x"), new Var("y")), new Plus(new Var("y"), new Var("x")));
        if (!ex.simplify().toString().equals("0.0")) {
            System.out.println("#97:Error in bonus simplify of:" + ex);
        }

        //derivatives
        ex = new Minus(new Num(10), new Var("x"));
        if (!ex.differentiate("x").toString().equals("(0.0 - 1.0)"))
            System.out.println("#79:Error in Differentitation of:" + ex);

        return true;
    }

    public static boolean multTests() {
        Expression ex;

        ex = new Mult(new Num(10), new Num(10));
        if (!ex.toString().equals("(10.0 * 10.0)"))
            System.out.println("#17:Error in" + ex);
        ex = new Mult(new Num(10), new Var("x"));
        if (!ex.toString().equals("(10.0 * x)"))
            System.out.println("#18:Error in" + ex);
        ex = new Mult(new Var("x"), new Num(10));
        if (!ex.toString().equals("(x * 10.0)"))
            System.out.println("#19:Error in" + ex);
        ex = new Mult(new Var("x"), new Var("x"));
        if (!ex.toString().equals("(x * x)"))
            System.out.println("#20:Error in" + ex);

        //x*1 or 1*x => x
        ex = new Mult(new Var("x"), new Num(1));
        if (!ex.simplify().toString().equals("x"))
            System.out.println("#31 :Error in simplify of" + ex.simplify());
        ex = new Mult(new Mult(new Var("x"), new Num(8)), new Num(1));
        if (!(ex.simplify().toString().equals("(x * 8.0)") || ex.simplify().toString().equals("(8.0 * x)")))
            System.out.println("#32:Error in simplify of" + ex.simplify());
        ex = new Mult(new Num(1), new Mult(new Var("x"), new Num(8)));
        if (!(ex.simplify().toString().equals("(x * 8.0)") || ex.simplify().toString().equals("(8.0 * x)")))
            System.out.println("#33:Error in simplify of" + ex.simplify());
        ex = new Mult(new Num(1), new Var("x"));
        if (!ex.simplify().toString().equals("x"))
            System.out.println("#34:Error in simplify of" + ex.simplify());

        //x*0 or 0*x => 0
        ex = new Mult(new Var("x"), new Num(0));
        if (!ex.simplify().toString().equals("0.0"))
            System.out.println("#35:Error in simplify of" + ex.simplify());
        ex = new Mult(new Pow(new Mult(new Num(3), new Num(2)), new Num(10)), new Num(0));
        if (!ex.simplify().toString().equals("0.0"))
            System.out.println("#36:Error in simplify of" + ex.simplify());
        ex = new Mult(new Num(0), new Var("x"));
        if (!ex.simplify().toString().equals("0.0"))
            System.out.println("#37:Error in simplify of" + ex.simplify());
        ex = new Mult(new Num(0), new Pow(new Mult(new Num(10), new Num(50)), new Num(50)));
        if (!ex.simplify().toString().equals("0.0"))
            System.out.println("#38:Error in simplify of" + ex.simplify());

        //(x^y)*(x^z) => x^(y+z)
        ex = new Mult(new Pow(new Var("x"), new Num(2)), new Pow(new Var("x"), new Num(3)));
        if (!ex.simplify().toString().equals("(x^5.0)")) {
            System.out.println("#105:Error in bonus simplify of:" + ex);
        }

        //(x^z)*(y^z) => (x*y)^z
        ex = new Mult(new Pow(new Var("x"), new Num(3)), new Pow(new Var("y"), new Num(3)));
        if (!ex.simplify().toString().equals("((x * y)^3.0)")) {
            System.out.println("#108:Error in bonus simplify of:" + ex);
        }

        //derivatives
        ex = new Mult(new Num(10), new Var("x"));
        if (!ex.differentiate("x").toString().equals("((0.0 * x) + (10.0 * 1.0))"))
            System.out.println("#80:Error in Differentitation of:" + ex);

        return true;
    }

    public static boolean divTests() {
        Expression ex;

        ex = new Div(new Num(10), new Num(10));
        if (!ex.toString().equals("(10.0 / 10.0)"))
            System.out.println("#13:Error in" + ex);
        ex = new Div(new Num(10), new Var("x"));
        if (!ex.toString().equals("(10.0 / x)"))
            System.out.println("#14:Error in" + ex);
        ex = new Div(new Var("x"), new Num(10));
        if (!ex.toString().equals("(x / 10.0)"))
            System.out.println("#15:Error in" + ex);
        ex = new Div(new Var("x"), new Var("x"));
        if (!ex.toString().equals("(x / x)"))
            System.out.println("#16:Error in" + ex);

        //x/1 => x
        ex = new Div(new Var("x"), new Num(1));
        if (!ex.simplify().toString().equals("x"))
            System.out.println("#46:Error in simplify of" + ex.simplify());
        ex = new Div(new Pow(new Num(10), new Var("y")), new Num(1));
        if (!ex.simplify().toString().equals("(10.0^y)"))
            System.out.println("#47:Error in simplify of" + ex.simplify());

        //x/x => 1
        ex = new Div(new Var("x"), new Var("x"));
        if (!ex.simplify().toString().equals("1.0"))
            System.out.println("#43:Error in simplify of" + ex.simplify());
        ex = new Div(new Mult(new Num(9), new Var("x")), new Mult(new Num(9), new Var("x")));
        if (!ex.simplify().toString().equals("1.0"))
            System.out.println("#44:Error in simplify of" + ex.simplify());
        ex = new Div(new Num(5), new Num(5));
        if (!ex.simplify().toString().equals("1.0"))
            System.out.println("#45:Error in simplify of" + ex.simplify());
        ex = new Div(new Plus(new Var("x"), new Var("y")), new Plus(new Var("y"), new Var("x")));
        if (!ex.simplify().toString().equals("1.0")) {
            System.out.println("#98:Error in bonus simplify of:" + ex);
        }

        //(x^y)/(z^y) => (x/z)^y
        ex = new Div(new Pow(new Var("x"), new Num(2)), new Pow(new Var("y"), new Num(2)));
        if (!ex.simplify().toString().equals("((x / y)^2.0)")) {
            System.out.println("#104:Error in bonus simplify of:" + ex);
        }

        //(x^y)/(x^z) => x^(y-z)
        ex = new Div(new Pow(new Var("x"), new Num(5)), new Pow(new Var("x"), new Num(3)));
        if (!ex.simplify().toString().equals("(x^2.0)")) {
            System.out.println("#106:Error in bonus simplify of:" + ex);
        }

        //derivatives
        ex = new Div(new Num(10), new Var("x"));
        if (!ex.differentiate("x").toString().equals("(((0.0 * x) - (10.0 * 1.0)) / (x^2.0))"))
            System.out.println("#77:Error in Differentitation of:" + ex);
        
        return true;
    }

    public static boolean powTests() {
        Expression ex;

        ex = new Pow(new Num(10), new Num(10));
        if (!ex.toString().equals("(10.0^10.0)"))
            System.out.println("#21:Error in" + ex);
        ex = new Pow(new Num(10), new Var("x"));
        if (!ex.toString().equals("(10.0^x)"))
            System.out.println("#22:Error in" + ex);
        ex = new Pow(new Var("x"), new Num(10));
        if (!ex.toString().equals("(x^10.0)"))
            System.out.println("#23:Error in" + ex);
        ex = new Pow(new Var("x"), new Var("x"));
        if (!ex.toString().equals("(x^x)"))
            System.out.println("#24:Error in" + ex);

        //x^1 => x
        ex = new Pow(new Var("x"), new Num(1));
        if (!ex.simplify().toString().equals("x"))
            System.out.println("#57:Error in simplify of" + ex.simplify());
        ex = new Pow(new Pow(new Var("x"), new Var("y")), new Num(1));
        if (!ex.simplify().toString().equals("(x^y)"))
            System.out.println("#58:Error in simplify of" + ex.simplify());

        //(x^y)^z => x^(y*z)
        ex = new Pow(new Pow(new Var("x"), new Var("y")), new Var("z"));
        if (!ex.simplify().toString().equals("(x^(y * z))")) {
            System.out.println("#103:Error in bonus simplify of:" + ex.simplify());
        }

        //derivatives
        ex = new Pow(new Num(10), new Var("x"));
        if (!ex.differentiate("x").toString().equals("((10.0^x) * ((0.0 * (x / 10.0)) + (1.0 * log(e, 10.0))))"))
            System.out.println("#82:Error in Differentitation of:" + ex);
        ex = new Pow(new Var("x"), new Var("x"));
        if (!ex.differentiate("x").toString().equals("((x^x) * ((1.0 * (x / x)) + (1.0 * log(e, x))))"))
            System.out.println("#83:Error in Differentitation of:" + ex);
        ex = new Pow(new Var("x"), new Num(3));
        if (!ex.differentiate("x").toString().equals("((x^3.0) * ((1.0 * (3.0 / x)) + (0.0 * log(e, x))))"))
            System.out.println("#84:Error in Differentitation of:" + ex);
        ex = new Pow(new Var("x"), new Num(4)).differentiate("x");
        if (!ex.toString().equals("((x^4.0) * ((1.0 * (4.0 / x)) + (0.0 * log(e, x))))")) {
            System.out.println("#92:Error in:" + ex);
        }
        ex = new Pow(new Var("e"), new Var("x"));
        if (!ex.differentiate("x").toString().equals("((e^x) * ((0.0 * (x / e)) + (1.0 * log(e, e))))")) {
            System.out.println("#95:Error in diff of:" + ex);
        }
        ex = ex.simplify();
        if (!ex.toString().equals("(e^x)")) {
            System.out.println("#96:Error in simplify of:" + ex);
        }

        return true;
    }

    public static boolean logTests() {
        Expression ex;

        ex = new Log(new Num(10), new Num(10));
        if (!ex.toString().equals("log(10.0, 10.0)"))
            System.out.println("#9:Error in" + ex);
        ex = new Log(new Num(10), new Var("x"));
        if (!ex.toString().equals("log(10.0, x)"))
            System.out.println("#10:Error in" + ex);
        ex = new Log(new Var("x"), new Num(10));
        if (!ex.toString().equals("log(x, 10.0)"))
            System.out.println("#11:Error in" + ex);

        //log (x,x) => 1
        ex = new Log(new Var("x"), new Var("x"));
        if (!ex.toString().equals("log(x, x)"))
            System.out.println("#12:Error in" + ex);
        ex = new Log(new Var("x"), new Var("x"));
        if (!ex.simplify().toString().equals("1.0"))
            System.out.println("#54:Error in simplify of" + ex.simplify());
        ex = new Log(new Num(105), new Num(105));
        if (!ex.simplify().toString().equals("1.0"))
            System.out.println("#55:Error in simplify of" + ex.simplify());
        ex = new Log(new Sin(new Pow(new Num(10), new Var("x"))), new Sin(new Pow(new Num(10), new Var("x"))));
        if (!ex.simplify().toString().equals("1.0"))
            System.out.println("#56:Error in simplify of" + ex.simplify());
        ex = new Log(new Plus(new Var("x"), new Var("y")), new Plus(new Var("y"), new Var("x")));
        if (!ex.simplify().toString().equals("1.0")) {
            System.out.println("#99:Error in bonus simplify of:" + ex);
        }
        
        //log rules
        ex = new Log(new Num(2), new Num(8));
        if (!ex.simplify().toString().equals("3.0"))
            System.out.println("#67:Error in simplify of" + ex.simplify());
        ex = new Log(new Num(2), new Var("x"));
        if (!ex.simplify().toString().equals("log(2.0, x)"))
            System.out.println("#68:Error in simplify of" + ex.simplify());

        //base > 0
        ex = new Log(new Num(-1), new Num(10));
        try {
            System.out.println("#69:Error in log(0, x)" + ex.evaluate());
        } catch (Exception e2) {
        }

        //base != 1
        ex = new Log(new Num(1), new Num(10));
        try {
            System.out.println("#70:Error in log(1, x)" + ex.evaluate());
        } catch (Exception e2) {
        }
        //log argument > 0
        ex = new Log(new Num(5), new Num(0));
        try {
            System.out.println("#71:Error in log(x, 0)" + ex.evaluate());
        } catch (Exception e2) {
        }

        //derivatives
        ex = new Log(new Num(10), new Var("x"));
        if (!ex.differentiate("x").toString().equals("(1.0 / (x * log(e, 10.0)))"))
            System.out.println("#78:Error in Differentitation of:" + ex);

        return true;
    }

    public static boolean cosTests() throws Exception{
        Expression ex;

        ex = new Cos(new Num(10));
        if (!ex.toString().equals("cos(10.0)"))
            System.out.println("#25:Error in" + ex);
        ex = new Cos(new Var("x"));
        if (!ex.toString().equals("cos(x)"))
            System.out.println("#26:Error in" + ex);
        ex = new Cos(new Num(0));
        if (ex.evaluate() != 1)
            System.out.println("#61:Error in:" + ex.evaluate());
        ex = new Cos(new Var("x"));
        try {
            ex.evaluate();
            System.out.println("#62:Error in:" + ex);
        } catch (Exception e2) {
            //should catch an error here. this is correct!
        }
        ex = new Cos(new Num(180));
        if (ex.evaluate() != -1.0)
            System.out.println("#91:Error in Radians:" + ex);
        ex = new Cos(new Plus(new Var("x"), new Var("y")));
        if (!(ex.differentiate("x").toString().equals("(-(sin((x + y)) * (1.0 + 0.0)))"))
                || (ex.differentiate("x").toString().equals("((-sin((x + y)) * (1.0 + 0.0)))"))) {
            System.out.println("#102:Error in diff of:" + ex);
        }

        return true;
    }

    public static boolean sinTests() throws Exception{
        Expression ex;

        ex = new Sin(new Num(10));
        if (!ex.toString().equals("sin(10.0)"))
            System.out.println("#27:Error in radian:" + ex);
        ex = new Sin(new Var("x"));
        if (!ex.toString().equals("sin(x)"))
            System.out.println("#28:Error in" + ex);
        ex = new Sin(new Num(180));
        if (ex.evaluate() != 1.2246467991473532E-16)
            System.out.println("#90:Error in Radians:" + ex);

        //derivatives
        ex = new Cos(new Num(10));
        if (!(ex.differentiate("x").toString().equals("(-(sin(10.0) * 0.0))")
                || (ex.differentiate("x").toString().equals("((-sin(10.0)) * 0.0)"))))
            System.out.println("#75:Error in Differentitation of:" + ex);
        ex = new Cos(new Var("x"));
        if (!ex.differentiate("x").toString().equals("(-(sin(x) * 1.0))"))
            System.out.println("#76:Error in Differentitation of:" + ex);

        return true;
    }

    public static boolean negTests() {
        Expression ex;

        ex = new Neg(new Num(10));
        if (!ex.toString().equals("(-10.0)"))
            System.out.println("#29:Error in" + ex);
        ex = new Neg(new Var("x"));
        if (!ex.toString().equals("(-x)"))
            System.out.println("#30:Error in" + ex);
        ex = new Neg(new Num(-1));
        if (!ex.toString().equals("(--1.0)"))
            System.out.println("#63:Error in" + ex);
        ex = new Neg(new Num(-1));
        if (!ex.simplify().toString().equals("1.0"))
            System.out.println("#64:Error in" + ex);
        ex = new Neg(new Num(1));
        if (!ex.toString().equals("(-1.0)"))
            System.out.println("#65:Error in" + ex);
        ex = new Neg(new Var("x"));
        if (!ex.toString().equals("(-x)"))
            System.out.println("#66:Error in" + ex);

        return true;
    }
    
    public static boolean numvarTests() {
        Expression ex;

        ex = new Pow(new Num(2),
                new Minus(new Num(5), new Pow(new Num(2), new Plus(new Num(0.5), new Sin(new Cos(new Num(0)))))));
        if (!ex.simplify().toString().equals("11.864461183774617"))
            System.out.println("#60:Error in simplify of" + ex.simplify());
        ex = new Pow(new Num(-2), new Num(0.5));
        if (!ex.toString().equals("(-2.0^0.5)")) {
            System.out.println("#112:Error in:" + ex);
        }
        try {
            System.out.println("#113:Error in evaluate:" + ex.evaluate());
        } catch (Exception e2) {
        }
        try {
            System.out.println("#114:Error in simplify:" + ex.simplify());
        } catch (Exception e2) {
        }
        ex = new Div(new Num(10), new Num(0));
        try {
            System.out.println("#115 Error in:" + ex.evaluate());
        } catch (Exception e) {
        }

        //derivatives
        ex = new Num(10);
        if (!ex.differentiate("x").toString().equals("0.0"))
            System.out.println("#72:Error in Differentitation of:" + ex);
        ex = new Var("x");
        if (!ex.differentiate("x").toString().equals("1.0"))
            System.out.println("#73:Error in Differentitation of:" + ex);
        ex = new Var("y");
        if (!ex.differentiate("x").toString().equals("0.0"))
            System.out.println("#74:Error in Differentitation of:" + ex);

        return true;
    }
    
    public static boolean nestedTests() throws Exception{
        Expression ex;

        ex = new Sin(new Pow(new Mult(new Plus(new Mult(new Num(2), new Var("x")), new Var("y")), new Num(4)),
                new Var("x")));
        if (!ex.toString().equals("sin(((((2.0 * x) + y) * 4.0)^x))"))
            System.out.println("#85:Error in simplify of" + ex);
        List<String> vars = ex.getVariables();
        if (!(vars.get(0).equals("x") && vars.get(1).equals("y"))) {
            System.out.println("#86:Error in getVarList");
        }
        Expression ex2 = ex.assign("x", new Num(10));
        if (!ex2.toString().equals("sin(((((2.0 * 10.0) + y) * 4.0)^10.0))")) {
            System.out.println("#87:Error in:" + ex2);
        }
        ex2 = ex.assign("x", ex);
        if (!ex2.toString().equals(
                "sin(((((2.0 * sin(((((2.0 * x) + y) * 4.0)^x))) + y) * 4.0)^sin(((((2.0 * x) + y) * 4.0)^x))))")) {
            System.out.println("#88:Error in:" + ex2);
        }
        ex = new Pow(new Plus(new Var("x"), new Var("y")), new Num(2));
        Map<String, Double> assignment = new TreeMap<String, Double>();
        assignment.put("x", 2.0);
        assignment.put("y", 4.0);
        double value = ex.evaluate(assignment);
        if (value != 36) {
            System.out.println("#89:Error in map");
        }
        ex = new Pow(new Plus(new Var("x"), new Var("y")), new Num(2));
        if (!ex.differentiate("x").toString()
                .equals("(((x + y)^2.0) * (((1.0 + 0.0) * (2.0 / (x + y))) + (0.0 * log(e, (x + y)))))")) {
            System.out.println("#93:Error in diff:" + ex.differentiate("x"));
        }
        ex = new Pow(new Plus(new Var("x"), new Var("y")), new Num(2));
        if (!ex.differentiate("x").simplify().toString().equals("(((x + y)^2.0) * (2.0 / (x + y)))")) {
            System.out.println("#94:Error in simplify of:" + ex.differentiate("x"));
        }
        ex = new Plus(new Mult(new Plus(new Num(3), new Num(6)), new Var("x")),
                new Mult(new Mult(new Num(4), new Var("x")), new Sin(new Num(0))));
        if (!ex.simplify().toString().equals("(9.0 * x)")) {
            System.out.println("#100:Error in simplify of:" + ex);
        }
        try {
            ex = new Num(10);
        } catch (Exception e2) {
            System.out.println("#101:you have to do int constructor");
        }

        //(a*x)+(x*b) => (a+b)*x
        ex = new Plus(new Mult(new Var("x"), new Num(2)), new Mult(new Var("x"), new Num(4)));
        if (!ex.simplify().toString().equals("(6.0 * x)")) {
            System.out.println("#109:Error in bonus simplify of:" + ex);
        }

        return true;
    }
    public static void main(String[] args) throws Exception {
        System.out.println("Start of checking...");
        Expression ex;
        plusTests();  
        minusTests();
        multTests();
        divTests();
        powTests();
        logTests();
        cosTests();
        sinTests();
        negTests();
        numvarTests();
        nestedTests();      
        
        // bonus checking
        
        ex = new Mult(new Mult(new Num(2), new Var("x")), new Mult(new Var("x"), new Num(3)));
        if (!ex.simplify().toString().equals("(6.0 * (x^2.0))")) {
            System.out.println("#110:Error in bonus simplify of:" + ex);
        }
        ex = new Div(new Div(new Num(2), new Var("x")), new Div(new Var("x"), new Num(3)));
        if (!ex.simplify().toString().equals("(6.0 / (x^2.0))")) {
            System.out.println("#111:Error in bonus simplify of:" + ex);
        }
        

        System.out.println("...End of checking!");
    }
}
