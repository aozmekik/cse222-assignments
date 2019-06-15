import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Represent the concrete program which takes a file
 * from command-line argument then calculates the number
 * of "white components" and print that number on screen.
 * @author Ahmed Semih Özmekik
 * @see BinaryImage
 */
public class ProgramBinaryImage
{
    public static void main(String[] args) {
        if (args.length !=1)
            throw new InvalidParameterException();

        try {
            BinaryImage img = new BinaryImage(args[0]);
            System.out.println("White Component: " + img.component());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
