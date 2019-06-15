import java.util.Iterator;

/**
 * Represents the iterator class that will traverse
 * a given 2D array spirally clockwise starting at the
 * top left element.
 * @author Ahmed Semih Özmekik
 */
public class IteratorSpiral implements Iterator {

    private final int[][] data;
    private Index[] indices;

    private int x = 0; // xth, row index of the array.
    private int y = 0; // yth, column index of the array.
    private int index = 0;
    
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;


    /**
     * Creates the spiral iterator for given 2D array.
     * @param data 2D integer array.
     */
    public IteratorSpiral(int[][] data)
    {
        this.data = data;
        int row = data.length;  // row length of the array.
        int col = data[0].length; // column length of the array.

        indices = new Index[row*col];
        for (int i=0;i<indices.length;++i)
            indices[i] = new Index();

        int[] boundary = new int[4]; // 4 boundaries exist.
        boundary[RIGHT] = col;
        boundary[DOWN] = row;
        boundary[LEFT] = -1;
        boundary[UP] = 0;

        // Find all of the the indices recursively.
        findIndex(RIGHT, boundary);
        index = 0;
    }

    /**
     * Returns true if the iteration has more elements.
     * @return true if the iteration has more elements.
     */
    @Override
    public boolean hasNext()
    {
        return index != indices.length;
    }

    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration.
     */
    @Override
    public Integer next()
    {
        x = indices[index].x;
        y = indices[index].y;

        ++index;

        return data[x][y];
    }

    /**
     * Finds the indices and stores them in indices array. Those indices array
     * will be used to iterate the array spirally later on.
     * Pre-Condition: parameter way must be RIGHT=0. Boundary[size=4] array should consist
     *                proper limits-boundaries for each 4 positions
     *                regarding to data[][] array.
     * @param way is the starting direction. Should be RIGHT=0 literal in call.
     * @param boundary is boundary, must only have 4 elements which represents the direction.
     */
    private void findIndex(int way, int[] boundary)
    {


        while(index != indices.length) {
            indices[index].set(x, y);
            ++index;

            if (way == RIGHT) {
                if (y + 1 == boundary[way]) { // End of the row.
                    ++x; // Go one row down.
                    way = DOWN;
                } else
                    ++y;
            } else if (way == LEFT) {
                if (y - 1 == boundary[way]) { // Beginning of the row.
                    --x; // Go one row up.
                    way = UP;
                } else
                    --y;
            } else if (way == DOWN) {
                if (x + 1 == boundary[way]) { // End of the column.
                    --y; // Go one column left;
                    way = LEFT;
                } else
                    ++x;
            } else if (way == UP) {
                if (x - 1 == boundary[way]) { // Starting index and the end of the tour.
                    if (boundary[RIGHT] - 1 != 1 && boundary[DOWN] - 1 != 1) { // There is more matrix.

                        --boundary[RIGHT];
                        ++boundary[LEFT];
                        ++boundary[UP];
                        --boundary[DOWN];
                        --index;
                        // Boundaries decremented and limits increased for inner matrices,
                        // and the new tour starts from zero.
                        findIndex(RIGHT, boundary);
                    }
                } else
                    --x;
            }
        }
    }


    /**
     * Represents the index of 2D arrays.
     */
    private static class Index
    {
        // Default invalid index values.
        private int x = -1;
        private int y = -1;

        private void set(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

    }
}
