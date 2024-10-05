# My Own Function Calculator

This repository holds the java code I wrote for a function calculator during my first year, and all of its improvements.

It is packaged in a friendly command line UI which allows the user to enter any expression comprised of the elemntary operations: addition, subtraction, multiplication, division, exponentiation, logarithms, sine and cosine.

It also supports variables in the expressions, and allows assigning values to variables, simplifying an expression, and even taking its derivative according to any variable the user wants.

Lastly, this repository holds all the tests I wrote, so that future development will be easier.

# How To Run

In order to run the program you must make sure java is installed on your machine.

Then, in order to compile the code enter in the command line: 

`javac -d bin src/Base/*.java src/BinaryExpressions/*.java src/UnaryExpressions/*.java src/FullTest.java src/Parser.java src/Main.java`

In order to run the tests and make sure it all downloaded correctly enter:

`java -cp bin FullTest`

In order to run the program itself enter:

`java -cp bin Main`

# How To Use

After running the program, a menu will be displayed. It contains 6 options:

0) Pressing 0 will end the program and go back to the command line.

1) Pressing 1 will print the current expression.

2) Pressing 2 will allow the user to enter a new expression. That expression can be as complex as you want, but make sure the brackets are valid.
   x+5 is valid, but x+5+2 is not valid, you will need to write it as  (x+5)+2, or x+(5+2). Spaces do not affect the end result.

3) Pressing 3 will attempt to evaluate the current expression. If it doesn't contains any variables, it will display the value of te current expression,
   and if it does it will show the user an error and do nothing.

4) Pressing 4 will simplify the current expression. The program cant simplify everything, but a lot of work as been put in this feature, and it can do some nice things like:
   * (x+6)/(6+x) => 1
   * (x*2)*x => 2 * (x^2)
   * 0 * x => 0
   * x + 0 => x
   * 1 * x => x
     
   and many more.

5) Pressing 5 will first read from the user another variable, and then take the derivative of the current expression by the newly inputed variable.

6) Pressing 6 will allow the user to assign a value to a variable that is currently in the expression. <br>
   It doesn't have to be a number, it can also assign into a variable another expression like so:
   x+5 assigning x to be a+8 results in: (a+8)+5
