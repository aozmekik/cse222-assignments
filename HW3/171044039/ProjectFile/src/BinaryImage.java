import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

/**
 * Represents a binary digital image represented
 * through a matrix, each element of which is either 1(white)
 * or 0(black). Has a method which finds number of components.
 */
public class BinaryImage
{
    private MyStack<Matrix> stack;
    private Matrix[][] bitmap;
    private String file; // filename


    // Dimensions of the bitmap.
    private int row;
    private int col;


    // Delimiters used in bitmap file.
    private static final char EOF = '\n';

    /**
     * Creates a binary image from the given file.
     * After creating the binary image, you may be want to
     * use component() for to get the number of components
     * in that bitmap.
     * @param filename input file to read for to create bitmap.
     * @throws IOException any input errors can occur i.e.
     *                     FileNotFound except.
     */
    public BinaryImage(String filename) throws IOException
    {
        file = filename;
        stack = new MyStack<>();

        file = new String(Files.readAllBytes(Paths.get(filename)))
                .replaceAll(" ","");



        // Get the dimensions and stretch them from each corner by 1 point.
        col = file.indexOf(EOF) + 1;
        if (file.indexOf(file.length()-1) != EOF)
            row = (file.length()+1)/col;
        else
            row = file.length()/col;

        col +=1;
        row +=2;

        bitmap = new Matrix[row][col];

        file = file.replaceAll("\n", "");

    }

    /**
     * Finds number of white components. A white component
     * is a set of white matrix locations, such that, between any
     * two of them, there is a path of white matrix locations, where every
     * consecutive pair are adjacent.
     * @return the number of white component.
     */
    public int component()
    {
        int number=0;
        initMap();

        for(int i=0;i<bitmap.length;++i)
        {
            for (Matrix bit: bitmap[i]){
                if (bit.color == Matrix.WHITE){ // Found '1' bit.
                    bit.color = Matrix.BLACK;
                    stack.push(bit);
                }

                while(!stack.isEmpty()){
                    boolean found = false;
                    Matrix compo = stack.peek(); // Matrix belongs to one component.

                    // Check each 4 position.
                    for (Position pos:  Position.values()){
                        Matrix m = getMatrix(compo.x, compo.y, pos);
                        if (m.color == Matrix.WHITE){
                            m.color = Matrix.BLACK;
                            found = true;
                            stack.push(m);
                        }
                    }
                    if (!found)
                        stack.pop();
                    if (stack.isEmpty()) // Component is completed.
                        ++number;

                }

            }
        }

        return number;
    }

    /**
     * Initializes the bitmap from then given file. Adds all bits
     * to the bitmap List with their positions.
     */
    private void initMap()
    {
        int x=0, y=0; // Positions of matrices.

        // Stretch to top.
        for (int i=0;i<col;++i)
            bitmap[x][i] = new Matrix(x, i, Matrix.BLACK);

        ++x;
        int index = 0;
        while(x<row-1){

            if (y == col-1){
                bitmap[x][y] = new Matrix(x, y, Matrix.BLACK); // Stretch to right.
                y = 0;
                ++x;
            }
            else{
                if (y==0){
                    bitmap[x][y] = new Matrix(x,y, Matrix.BLACK); // Stretch to left.
                    ++y;
                }
                else{
                    char ch = file.charAt(index++);
                    bitmap[x][y] = new Matrix(x, y, Character.getNumericValue(ch));
                    ++y;
                }
            }
        }

        // Stretch to down.
        for (int i=0;i<col;++i)
            bitmap[x][i] = new Matrix(x, i, Matrix.BLACK);


    }

    /**
     * Gets the matrix from the bitmap list.
     * @param x x position of the matrix.
     * @param y y position of the matrix.
     */
    private Matrix getMatrix(int x, int y)
    {
        return bitmap[x][y];
    }


    private Matrix getMatrix(int x, int y, Position pos)
    {
        Matrix matrix;
        switch(pos)
        {
            case LEFT:  matrix = getMatrix(x,y-1);  break;
            case TOP:   matrix = getMatrix(x-1,y);  break;
            case RIGHT: matrix = getMatrix(x,y+1);  break;
            case DOWN:  matrix = getMatrix(x+1, y); break;
            default: throw new InvalidParameterException();
        }

        return matrix;
    }

    /**
     * Represents the positions.
     */
    enum Position
    {
        LEFT, TOP, RIGHT, DOWN
    }

    /**
     * Represents the Matrix in the bitmap.
     */
    private static class Matrix
    {
        // Color constants in the map.
        private static final int WHITE = 1;
        private static final int BLACK = 0;

        private int x;
        private int y;
        private int color;

        /**
         * Returns a Matrix with given indices
         * @param row row index of the matrix in the bitmap.
         * @param col column index of the matrix in the bitmap.
         * @param color is either 1(WHITE) or 0(BLACK)
         * @throws InvalidParameterException if color is not 1 nor 0.
         */
        public Matrix(int row, int col, int color)
        {
            if (color != 1 && color != 0)
                throw new InvalidParameterException();

            this.x = row;
            this.y = col;
            this.color = color;
        }

        @Override
        public String toString() {
            return String.format("[%d](%d,%d)", color, x,y);
        }

    }

}
