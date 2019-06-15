import java.io.IOException;
import java.util.Scanner;

/**
 * Represents the Concrete Command-line Program which produces
 * and consumes pixel of the given image in 3 different comparison scheme.
 * @see PixelPriority
 * @see PriorityQueue
 * @author Ahmed Semih Özmekik
 */

public class ProgramPixelPriority
{

    public static void main(String[] args) {
        try {
            System.out.println("Enter the file name>");
            String file = new Scanner(System.in).next();
            PixelPriority pixel = PixelPriority.getInstance();
            pixel.setFile(file);
            pixel.start();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
