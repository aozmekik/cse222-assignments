
/**
 * Represents the test code for IteratorSpiral.
 * @see IteratorSpiral
 * @author Ahmed Semih Özmekik
 */
public class Driver
{
    /*
     * Constructs matrix from given x and y. Values starts from
     * 0 and they are sequential
     */
    private static int[][] constructMatrix(int x, int y)
    {
        int[][] matrix = new int[x][y];

        int num=1;
        for (int i=0;i<x;++i){
            for (int j=0;j<y;++j){
                matrix[i][j] = num++;
                System.out.printf("%3d ", matrix[i][j]);
            }
            System.out.print("\n");
        }

        return matrix;
    }

    private static void print(String str)
    {
        System.out.println("[DEBUG] " + str);
    }

    private static void test(int data[][])
    {
        IteratorSpiral it = new IteratorSpiral(data);
        print("Iteration:");
        while(it.hasNext())
            System.out.printf("%d ", it.next());
        System.out.print("\n");
    }

    public static void main(String[] args)
    {
        /*
         * I will be testing for 6 different data.
         */
        int[][] data;

        print("Starting to test...");


        print("Data:");
        data = constructMatrix(4,4);
        test(data);

        print("Data:");
        data = constructMatrix(6,6);
        test(data);

        print("Data:");
        data = constructMatrix(5,6);
        test(data);

        print("Data:");
        data = constructMatrix(8,4);
        test(data);

        print("Data:");
        data = constructMatrix(2,1);
        test(data);

        print("Data:");
        data = constructMatrix(3,10);
        test(data);

        print("Test is over...");

    }
}
