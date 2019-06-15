import java.io.FileNotFoundException;

public class Driver
{
    public static void main(String[] args) {
        try {
            PopularityProgram pp = new PopularityProgram("input.txt");
            pp.input();
            pp.output();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
