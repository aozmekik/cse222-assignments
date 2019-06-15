import java.security.InvalidParameterException;

/**
 * Represents the simple Graph implementation with Adjacency Matrix.
 * @author Ahmed Semih Özmekik
 */
public class Graph {

    private boolean[][] adjacencyMatrix;
    private int[] numberOfEdgesPointingTo;
    private int size;

    public Graph(int size){
        this.size = size;
        adjacencyMatrix = new boolean[size][size];
        numberOfEdgesPointingTo = new int[size];
    }

    public void addEdge(int source, int dest){
        checkValidityOfIndex(source, dest);

        if (!hasEdge(source, dest)){
            adjacencyMatrix[source][dest] = true;
            numberOfEdgesPointingTo[dest]++;
        }
    }

    public void removeEdge(int source, int dest){
        checkValidityOfIndex(source, dest);

        if (hasEdge(source, dest)){
            adjacencyMatrix[source][dest] = false;
            numberOfEdgesPointingTo[dest]--;
        }
    }

    public boolean hasEdge(int source, int dest){
        return adjacencyMatrix[source][dest];
    }

    public int numberOfEdgesPointingTo(int source){
        return numberOfEdgesPointingTo[source];
    }

    private void checkValidityOfIndex(int ... indexes){
        for (int idx: indexes){
            if (idx > size)
                throw new InvalidParameterException("Index out of bounds!");
        }
    }
}
