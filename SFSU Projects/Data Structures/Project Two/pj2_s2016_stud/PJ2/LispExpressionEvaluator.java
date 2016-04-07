/************************************************************************************
 *
 *  		CSC220 Programming Project#2
 *  
 * Specification: 
 *
 * Taken from Project 7, Chapter 5, Page 178
 * I have modified specification and requirements of this project
 *
 * Ref: http://www.gigamonkeys.com/book/        (see chap. 10)
 *      http://joeganley.com/code/jslisp.html   (GUI)
 *
 * In the language Lisp, each of the four basic arithmetic operators appears 
 * before an arbitrary number of operands, which are separated by spaces. 
 * The resulting expression is enclosed in parentheses. The operators behave 
 * as follows:
 *
 * (+ a b c ...) returns the sum of all the operands, and (+ a) returns a.
 *
 * (- a b c ...) returns a - b - c - ..., and (- a) returns -a. 
 *
 * (* a b c ...) returns the product of all the operands, and (* a) returns a.
 *
 * (/ a b c ...) returns a / b / c / ..., and (/ a) returns 1/a. 
 *
 * Note: + * - / must have at least one operand
 *
 * You can form larger arithmetic expressions by combining these basic 
 * expressions using a fully parenthesized prefix notation. 
 * For example, the following is a valid Lisp expression:
 *
 * 	(+ (- 6) (* 2 3 4) (/ (+ 3) (* 1) (- 2 3 1)) (+ 1))
 *
 * This expression is evaluated successively as follows:
 *
 *	(+ (- 6) (* 2 3 4) (/ 3 1 -2) (+ 1))
 *	(+ -6 24 -1.5 1.0)
 *	17.5
 *
 * Requirements:
 *
 * - Design and implement an algorithm that uses LinkedStack class to evaluate a 
 *   valid Lisp expression composed of the four basic operators and integer values. 
 * - Valid tokens in an expression are '(',')','+','-','*','/',and positive integers (>=0)
 * - Display result as floting point number with at 2 decimal places
 * - Negative number is not a valid "input" operand, e.g. (+ -2 3) 
 *   However, you may create a negative number using parentheses, e.g. (+ (-2)3)
 * - There may be any number of blank spaces, >= 0, in between tokens
 *   Thus, the following expressions are valid:
 *   	(+   (-6)3)
 *   	(/(+20 30))
 *
 * - Must use LinkedStack class in this project. (*** DO NOT USE Java API Stack class ***)
 * - Must throw LispExpressionException to indicate errors
 * - Must not add new or modify existing data fields
 * - Must implement these methods in LispExpressionEvaluator class: 
 *
 *   	public LispExpressionEvaluator()
 *   	public LispExpressionEvaluator(String inputExpression) 
 *      public void reset(String inputExpression) 
 *      public double evaluate()
 *      private void evaluateCurrentOperation()
 *
 * - You may add new private methods
 *
 *************************************************************************************/

package PJ2;
import java.util.*;

public class LispExpressionEvaluator
{
    // Current input Lisp expression
    private String currentExpr;

    private double finalResult;

    // Main expression stack, see algorithm in evaluate()
    private LinkedStack<Object> tokensStack;
    private LinkedStack<Double> currentOpStack;


    // default constructor
    public LispExpressionEvaluator()
    {
        currentExpr = "";
        tokensStack = new LinkedStack<>();
        currentOpStack = new LinkedStack<>();
    }

    // constructor with an input expression 
    public LispExpressionEvaluator(String inputExpression) 
    {
        currentExpr = inputExpression;
        tokensStack = new LinkedStack<>();
        currentOpStack = new LinkedStack<>();
    }

    public void reset(String inputExpression) 
    {
        currentExpr = inputExpression;
        tokensStack.clear();
        currentOpStack.clear();
    }

    // This function evaluates current operator with its operands
    // See complete algorithm in evaluate()
    //
    // Main Steps:
    // 		Pop operands from tokensStack and push them onto 
    // 			currentOpStack until you find an operator
    //  	Apply the operator to the operands on currentOpStack
    //          Push the result into tokensStack
    //
    private void evaluateCurrentOperation()
    {
    	//Get first item in tokensStack
        String nextOpInStack = (String) tokensStack.pop();

        //Push only digits to currentOpStack while stack isn't empty
        while(!isOperator(nextOpInStack) && !tokensStack.empty()){

            //Convert to double and push to currentOpStack
            currentOpStack.push(Double.parseDouble(nextOpInStack));

            //Get next item in tokensStack
            nextOpInStack = (String) tokensStack.pop();
        }

        //If the currentOpStack is empty, we have no operands to operate on
        if(!currentOpStack.empty()){
            finalResult = calculateCurrentOpStack(nextOpInStack);
            //Push result back onto tokenStack
            tokensStack.push(String.valueOf(finalResult));
        }
        else{
            throw new LispExpressionException("Operator is missing operands to evaluate!");
        }
    }


    /**
     * This funtion evaluates current Lisp expression in currentExpr
     * It return result of the expression 
     *
     * The algorithm:  
     *
     * Step 1   Scan the tokens in the string.
     * Step 2		If you see an operand, push operand object onto the tokensStack
     * Step 3  	    	If you see "(", next token should be an operator
     * Step 4  		If you see an operator, push operator object onto the tokensStack
     * Step 5		If you see ")"  // steps in evaluateCurrentOperation() :
     * Step 6			Pop operands and push them onto currentOpStack 
     * 					until you find an operator
     * Step 7			Apply the operator to the operands on currentOpStack
     * Step 8			Push the result into tokensStack
     * Step 9    If you run out of tokens, the value on the top of tokensStack is
     *           is the result of the expression.
     */
    public double evaluate()
    {
    	//Counter to keep track of parentheses
        int parensCounter = 0;

        // use scanner to tokenize currentExpr
        Scanner currentExprScanner = new Scanner(currentExpr);
        
        // Use zero or more white space as delimiter,
        // which breaks the string into single character tokens
        currentExprScanner = currentExprScanner.useDelimiter("\\s*");

        // Step 1: Scan the tokens in the string.
        while (currentExprScanner.hasNext())
        {
     	    // Step 2: If you see an operand, push operand object onto the tokensStack
            if (currentExprScanner.hasNextInt())
            {
                // This force scanner to grab all of the digits
                // Otherwise, it will just get one char
                String dataString = currentExprScanner.findInLine("\\d+");

                //Push digit to tokensStack
                tokensStack.push(dataString);
            }
            else
            {
                // Get next token, only one char in string token
                String aToken = currentExprScanner.next();

                //System.out.println("Other: " + aToken);
                char item = aToken.charAt(0);
                
                // Step 3: If you see "(", next token should be an operator
                // Step 4: If you see an operator, push operator object onto the tokensStack
                // Step 5: If you see ")"  // steps in evaluateCurrentOperation() :
                switch (item)
                {
                    case '(':   parensCounter++;
                                break;
                    case '+':
                    case '-':
                    case '*':
                    case '/':   tokensStack.push(String.valueOf(item));
                                break;
                    case ')':   evaluateCurrentOperation();
                                parensCounter--;
                                break;
                    default:  // error
                                throw new LispExpressionException(item + " is not a legal expression operator");
                }
            }
        }
        
        //If parensCounter isn't zero, we have too many, or not enough parentheses
        if(parensCounter != 0){
            throw new LispExpressionException("Too many or too few parentheses");
        }

        return finalResult;
    }


    private double calculateCurrentOpStack(String operator){
        int i = 0;
        int stackSize = currentOpStack.size();
        double result = 0.0;

        if(operator.equals("+")){
            result = 0.0;
            for(i = 0; i < stackSize; i++){
                double operand = currentOpStack.pop();
                result += operand;
            }
        }
        else if(operator.equals("-")){
            result = 0.0;
            if(stackSize == 1){
                result -= currentOpStack.pop();
            }
            else{
                result = currentOpStack.pop();
                for(i = 0; i < stackSize-1; i++){
                    double operand = currentOpStack.pop();
                    result -= operand;
                }
            }
        }
        else if(operator.equals("*")){
            result = 1.0;
            for(i = 0; i < stackSize; i++){
                double operand = currentOpStack.pop();
                result *= operand;
            }
        }
        else if(operator.equals("/")){
            result = 1.0;
            if(stackSize == 1){
                result /= currentOpStack.pop();
            }
            else{
                result = currentOpStack.pop();
                for(i = 0; i < stackSize-1; i++){
                    double operand = currentOpStack.pop();
                    result /= operand;
                }
            }
        }

        return result;
    }


    //This method returns true if the input is an operator and false otherwise
    private boolean isOperator(String input){
        return input.equals("+") || input.equals("-") ||
                input.equals("*") || input.equals("/");
    } 
    

    //=====================================================================
    // DO NOT MODIFY ANY STATEMENTS BELOW
    // Quick test is defined in main()
    //=====================================================================

    // This static method is used by main() only
    private static void evaluateExprTest(String s, LispExpressionEvaluator expr, String expect)
    {
        Double result;
        System.out.println("Expression " + s);
        System.out.printf("Expected result : %s\n", expect);
	expr.reset(s);
        try {
           result = expr.evaluate();
           System.out.printf("Evaluated result : %.2f\n", result);
        }
        catch (LispExpressionException e) {
            System.out.println("Evaluated result :"+e);
        }
        
        System.out.println("-----------------------------");
    }

    // define few test cases, exception may happen
    public static void main (String args[])
    {
        LispExpressionEvaluator expr= new LispExpressionEvaluator();
        //expr.setDebug();
        String test1 = "(+ (- 6) (* 2 3 4) (/ (+ 3) (* 1) (- 2 3 1)) (+ 1))";
        String test2 = "(+ (- 632) (* 21 3 4) (/ (+ 32) (* 1) (- 21 3 1)) (+ 0))";
        String test3 = "(+ (/ 2) (* 2) (/ (+ 1) (+ 1) (- 2 1 ))(* 1))";
        String test4 = "(+ (/2)(+ 1))";
        String test5 = "(+ (/2 3 0))";
        String test6 = "(+ (/ 2) (* 2) (/ (+ 1) (+ 3) (- 2 1 ))))";
        String test7 = "(+ (*))";
        String test8 = "(+ (- 6) (* 2 3 4) (/ (+ 3) (* 1) (- 2 3 1)) (+ ))";
	evaluateExprTest(test1, expr, "17.50");
	evaluateExprTest(test2, expr, "-378.12");
	evaluateExprTest(test3, expr, "4.50");
	evaluateExprTest(test4, expr, "1.5");
	evaluateExprTest(test5, expr, "Infinity or LispExpressionException");
	evaluateExprTest(test6, expr, "LispExpressionException");
	evaluateExprTest(test7, expr, "LispExpressionException");
	evaluateExprTest(test8, expr, "LispExpressionException");
    }
}
