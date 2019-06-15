
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.Arrays;


/**
 * Represents the class having the public method which
 * calculates any infix expression using stack.
 * @author  Ahmed Semih Özmekik
 */
public class InfixCalculator
{

    private MyStack<Float> numberStack; // value stack.
    private MyStack<Character> opStack; // Operations stack. (+,* or log() etc.)
    private String file;
    private Variable[] vars; // Variables of the expression.
    private int index=0; // Index of the string.
    private int sizeVar=0; // Total variable used in the expression.

    /**
     * Defining the constant operation objects.
     */
    private static final char OPEN_BRACKETS = '(';
    private static final char CLOSE_BRACKETS = ')';
    private static final char PLUS = '+';
    private static final char MULTIPLY = '*';
    private static final char DIVIDE = '/';
    private static final char MINUS = '-';
    private static final char ABS = 'A';
    private static final char COS = 'C';
    private static final char SIN = 'S';
    private static final char DOT = '.';




    /**
     * Creates the InfixCalculator with given param. Check the string
     * format for the rules of syntax.
     * @param filename is the input which has the expression in it.
     *                 File format should be like that:
     *                 -The input variables(two or more) and
     *                 -empty line then the expression.
     *                 -
     *                 - var1
     *                 - var2
     *                 - ...
     *                 -- (expression)
     *                 Example: (Ignore the dash(-).)
     *                 - w=5
     *                 - x=6
     *                 -
     *                 - ( w + 4 ) * ( log( x ) - 77.9 )
     */
    public InfixCalculator(String filename) throws IOException
    {
        // Get file to string.
        file = new String(Files.readAllBytes(Paths.get(filename)));
        opStack = new MyStack<>();
        numberStack = new MyStack<>();

        // Initial size of variables defined as 10.
        // Will be increased in future, if needed.
        vars = new Variable[10];

    }


    /**
     * Calculates the infix expression in the given input
     * filename.
     *
     * @return the result of the calculation.
     */
    public float calculate()
    {
        clean(); // Cleaning the string from some characters
        readParam(); // Take the params.

        while(index<file.length()){

            char ch = file.charAt(index);

            if (Character.isDigit(ch)) // is number.
                numberStack.push(getNum());
            else if(containsKey(ch)) // is variable.
                numberStack.push((getVar(ch)));
            else if(ch == OPEN_BRACKETS){ // is '('.
                if (file.charAt(index+1) == MINUS) { // Unary '-' operator.
                    index += 2; // Escape '(' and '-'.
                    numberStack.push(-getNum());
                    ++index; // Escape ')'.
                }else
                    opStack.push(ch);

            }
            else if(ch == CLOSE_BRACKETS) { // is ')'.
                while(opStack.peek() != OPEN_BRACKETS){
                    char op = opStack.pop();
                    // Pop them in reverse order for correct result.
                    float num2 = numberStack.pop();
                    float num1 = numberStack.pop();
                    numberStack.push(handleOp(op, num1, num2));
                }
                // Since the calc. are done for this case, pop the '('.
                opStack.pop();

                // Case: The brackets were belong to the function.
                if (!opStack.isEmpty() && isFunction(opStack.peek())){
                    char op = opStack.pop();
                    float num = numberStack.pop();
                    numberStack.push(handleOp(op, num));
                }
            }
            else if (isFunction(ch))
                opStack.push(ch);
            else if(isElementaryOp(ch)){ // Elementary operation will be processed.
                // Deal with other ops before.
                while(!opStack.isEmpty() && isElementaryOp(opStack.peek()) &&
                        opPre(opStack.peek(), ch)){
                    char op = opStack.pop();
                    float num2 = numberStack.pop();
                    float num1 = numberStack.pop();
                    numberStack.push(handleOp(op, num1, num2));
                }
                opStack.push(ch);
            }

            ++index;
        }

        // Guards for no expression was given case: Example = "()"
        if (numberStack.isEmpty())
            return 0f;

        // Calculation of the final operation.
        while(!opStack.isEmpty()){
            char op = opStack.pop();
            float num2 = numberStack.pop();
            float num1 = numberStack.pop();
            numberStack.push(handleOp(op, num1, num2));
        }

        return numberStack.pop(); // final result is in the top of the stack.
    }

    /**
     * Read the parameters from from file.
     */
    private void readParam()
    {
        do {
            char param_name = file.charAt(index++);
            if(param_name == OPEN_BRACKETS){
                --index;
                break;
            }

            int param_value = 0;
            boolean negative = false;
            if (file.charAt(index) == MINUS){
                negative = true;
                ++index; // go to the number.
            }
            while(Character.isDigit(file.charAt(index))){
                int num = Character.getNumericValue(file.charAt(index++));

                // Increase all the digits of the old number and add the new small digit.
                param_value *= 10;
                param_value += num;
            }

            if (negative)
                param_value=-param_value;

            // Put the name of param as key, and the value of param as value to map.
            putVar(param_name, param_value);
        }while(file.charAt(index) != OPEN_BRACKETS);
    }

    /**
     * Reads the numbers correctly from the expression.
     */
    private float getNum()
    {
        // Fractional and Integer parts of the number.
        float int_part=0, fract_part=0;

        // Integer part.
        char ch = file.charAt(index);
        while(Character.isDigit(ch)){ // Take the part of the number which comes before '.'
            int_part *=10;
            int_part +=Character.getNumericValue(ch);
            ch = file.charAt(++index);
        }

        // Fractional part
        if (ch == DOT){
            ++index; // Ignore the '.'
            ch = file.charAt(index);
            int i=-1; // 10^-1
            while(Character.isDigit(ch)){
                fract_part += Character.getNumericValue(ch)*Math.pow(10,i);
                --i;
                ch = file.charAt(++index);
            }
        }

        --index; // Get the token back for sync.

        return int_part+fract_part;
    }

    /**
     * Calculates the result of the operation from the given
     * parameters and operation type as character.
     * @param op can only be '+', '*', '/'
     * @param num1 first parameter of the operation.
     * @param num2 second parameter of the operation.
     * @return result = (num1*num2), * is the operation.
     */
    private float handleOp(char op, float num1, float num2)
    {
        float result = 0;
        switch(op)
        {
            case MULTIPLY: result = num1*num2; break;
            case PLUS:     result = num1+num2; break;
            case DIVIDE:   result = num1/num2; break;
            case MINUS:    result = num1-num2; break;
            default:       throw new InvalidParameterException("No such operation!");
        }

        return result;
    }

    /**
     * Calculates the result of the operation from to given
     * parameter and operation. For this overload, operation
     * corresponds to a function such as log() or sin() etc.
     * @param op is the operation char literal.
     * @param num is the input of the function.
     * @return result = sin(num) or log(num)
     */
    private float handleOp(char op, float num)
    {
        double result = 0;
        switch(op)
        {
            case ABS:   result = abs(num); break;
            case SIN:   result = sin(num); break;
            case COS:   result = cos(num); break;
            default: throw new InvalidParameterException("No such function!");
        }

        return (float)result;
    }

    /**
     * For the given operation, checks if it is one of the functions(sin, log, etc.).
     * @param op is the operation literal.
     * @return True if the operation is function, False if the operation
     *          is just elementary operation such as +,*,/
     */
    private boolean isFunction(char op)
    {
        return op == COS || op == SIN || op == ABS;
    }

    /**
     * For the given operation, checks if it is one the elementary operations(+, *, /)
     * @param op is the operation literal.
     * @return True if the operation is an elementary op such as +,*,/, otherwise False.
     */
    private boolean isElementaryOp(char op)
    {
        return op == PLUS || op == MULTIPLY || op == DIVIDE || op==MINUS;
    }

    /**
     * For the given operations, determines the precedence of them.
     * @param op1 is the first operator.
     * @param op2 is the second operator.
     * @return If the op1 is has same or greater precedence as op2, returns true.
     */
    private boolean opPre(char op1, char op2)
    {
        boolean result = false;
        if(op1==MULTIPLY || op1==DIVIDE)
            result = true;
        else if(op2==PLUS || op2==MINUS)
            result = true;
        return result;
    }

    /**
     * Cleans the file string from empty spaces and makes it a useful
     * string.
     */
    private void clean()
    {
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<file.length();++i){
            char ch = file.charAt(i);

            if (ch=='\n'){ // Put one extra brackets surround the whole expression.
                 if(i+1!=file.length() && file.charAt(i+1) == '\n')
                    builder.append(OPEN_BRACKETS);
            }

            // Turn functions to only one letter.
            if (ch =='a' || ch=='s' || ch=='c'){
                if (i+1 != file.length() && Character.isLetter(file.charAt(i+1))){
                    ch = Character.toUpperCase(ch);
                    builder.append(ch);
                    i+=2;
                }
                else
                    builder.append(ch);
            }

            else if(ch != ' ' && ch != '\n' && ch != '=') // ignore the EOF, space etc.
                builder.append(ch);
        }

        builder.append(CLOSE_BRACKETS); // Close the expression.

        file = new String(builder);
    }

    /**
     * Sets the file which has the infix expression in it.
     * @param filename infix expression input. See the constructor
     *                 for syntax.
     */
    public void setFile(String filename) throws IOException
    {
        file = new String(Files.readAllBytes(Paths.get(filename)));
    }


    /**
     * Puts a variable for that key inside the Variable[] array.
     * @param key is the the letter representation of the variable.
     * @param value is the value of the variable.
     */
    private void putVar(char key, float value)
    {
        if (sizeVar == vars.length) // vars[] is full.
            vars = Arrays.copyOf(vars, vars.length*2); // double the capacity.

        vars[sizeVar++] = new Variable(key, value);
    }

    /**
     * Gets the variable from the Variable[] array.
     * @param key is the letter representation of the variable.
     * @return is the value of the variable for given key.
     * @throws  InvalidParameterException if the key doesn't exist in the array.
     */
    private float getVar(char key)
    {
        for(int i = 0; i<sizeVar;++i){
            if (vars[i].key == key)
                return vars[i].value;
        }
        throw new InvalidParameterException("Key is not found!");
    }

    /**
     * Checks if the Variable[] array contains the given key.
     * @param key is the letter representation of the variable.
     * @return true if the Variable[] array contains the key.
     */
    private boolean containsKey(char key)
    {
        boolean contains=false;
        for (int i=0;i<sizeVar && !contains;++i){
            if (vars[i].key == key)
                contains = true;
        }

        return contains;
    }

    /**
     * Sin(x) method implementation with taylor series.
     * @param x rads.
     * @return cos(x).
     */
    private static double cos(float x)
    {
        double result = 0;
        final int max_term = 10;

        x=x*3.141592f/180f;

        for(int i=0;i<=max_term;i++)
            result += pow(-1, i)*pow(x,2*i) /fact(2*i);

        return result;
    }

    /**
     * Sin(x) method implementation with taylor series.
     * @param x rads.
     * @return sin(x).
     */
    private static double sin(float x)
    {
        double result=0;
        final int max_term = 10;

        x *= 3.1415/180f;

        for(int i=0;i<=max_term;i++)
            result += pow(-1, i)*pow(x,2*i+1) /fact(2*i+1);

        return result;
    }

    private static double abs(float x)
    {
        if (x<0)
            x = -x;
        return x;
    }

    /**
     * Finds base^power(2^3).
     * @param base base.
     * @param power power.
     * @return x^y.
     */
    private static double pow(double base, int power)
    {
        double result=1;
        for (int i=0;i<power;++i)
            result *=base;
        return result;
    }

    /**
     * Finds n! for given n.
     * @param n n.
     * @return n!.
     */
    private static double fact(double n)
    {
        int result = 1;
        for (int i=2; i<=n; i++)
            result *= i;
        return result;
    }

    /**
     * Represents the variable which will take a part in the expression.
     * Every variable has a letter representation and a value.
     */
    private static class Variable
    {
        private final char key; // letter of the var.
        private final float value;

        public Variable(char key, float value)
        {
            if (!Character.isLetter(key))
                throw new InvalidParameterException("Key is not a letter!");

            this.key = key;
            this.value = value;
        }
    }

}
