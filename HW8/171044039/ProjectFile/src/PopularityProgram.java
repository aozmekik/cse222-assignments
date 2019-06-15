import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents the middle class between the input data and
 * functional part of the program. Only this class knows about
 * the specialized data structure. Reads the input and controls the
 * data structure (relations) and gives an output.
 * Represents the Controller Class for solution approach.
 * @author Ahmed Semih Özmekik
 * @see PopularityRelation
 */
public class PopularityProgram
{
    private PopularityRelation relations;
    private Scanner scanner;
    private int relationNumber;

    /**
     * Creates the Popularity Program for a given input.
     * @param filename is the input.
     * @throws FileNotFoundException
     */
    public PopularityProgram(String filename) throws FileNotFoundException {
        scanner = new Scanner(new File(filename));

        int peopleNumber = scanner.nextInt();
        relationNumber = scanner.nextInt();
        relations = new PopularityRelation(peopleNumber);

    }

    /**
     * Prints the number of people considered popular by every other person.
     */
    public void output(){
        System.out.println("Number of people considered popular by every other person: ");
        System.out.println(relations.numberOfFamousPeople());
    }

    /**
     * Gets the input from file and constructs the relations.
     */
    public void input(){
        while(scanner.hasNextInt() && relationNumber>0){
            int fanIndex = scanner.nextInt();
            int personIndex = scanner.nextInt();
            /* indexes decremented for left shifting in adjacency matrix */
            relations.addRelation(fanIndex-1, personIndex-1);
            --relationNumber;
        }
    }


}
