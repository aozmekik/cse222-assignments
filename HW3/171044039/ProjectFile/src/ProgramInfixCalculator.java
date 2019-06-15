import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Represents the concrete Infix Calculator Program.
 * Program takes the file input as command-line argument and
 * calculates the infix expression in that file. You should be aware
 * of that syntax of the infix expressions.
 * @see InfixCalculator for the syntax of the infix expressions.
 * @author Ahmed Semih Özmekik
 */
public class ProgramInfixCalculator
{
    public static void main(String[] args) {
        if (args.length != 1)
            throw new InvalidParameterException();

        try {
            InfixCalculator incal = new InfixCalculator(args[0]);
            System.out.println("Result: " + incal.calculate());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
