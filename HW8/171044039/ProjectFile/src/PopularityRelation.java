/**
 * Represents the data structure specialized for a group of people
 * in which an ordered relation with transitivity is defined between person pairs.
 * Graph Data Structure, for instance Adjacency Matrix implementation
 * used for to satisfy requirements of this structure.
 * Represents the Model Class for solution approach.
 * @author Ahmed Semih Özmekik
 */
public class PopularityRelation
{
    private Graph peopleGraph;
    private int peopleNumber;

    /**
     * Creates a Popularity Relation Structure with given initials.
     * @param peopleNumber number of peopleGraph in the data.
     */
    public PopularityRelation(int peopleNumber){
        this.peopleNumber = peopleNumber;
        peopleGraph = new Graph(peopleNumber);
    }

    /**
     * Inserts a relation to the structure.
     * Example:  P1 P2
     * @param fan is the index of P1 who thinks P2 is popular.
     *                 P1 is the fan of P2.
     * @param person is the index of P2 who P1 considers as popular.
     */
    public void addRelation(int fan, int person){
        if (fan == person)
            return;

        peopleGraph.addEdge(fan, person);
        applyTransitivity(fan, person);
    }

    /**
     * From a completed structure, gets the number of peopleGraph who are
     * considered popular by every other person. Keep that in mind, transitivity
     * on the relation will have considerable effects on the result.
     * @return the number of peopleGraph considered popular by every other person.
     */
    public int numberOfFamousPeople(){
        int famousNumber = 0;

        for (int i=0;i<peopleNumber;++i){
            if (isKnownByAll(i))
                ++famousNumber;
        }
        return famousNumber;
    }

    /* Adds new popular persons to the fan's table regarding the transitivity rule */
    private void applyTransitivity(int fan, int person){
        for (int i=0;i<peopleNumber;++i){
            if (peopleGraph.hasEdge(i, fan) && !peopleGraph.hasEdge(i, person))
                addRelation(i, person);
            if (peopleGraph.hasEdge(person, i) && !peopleGraph.hasEdge(fan, i))
                addRelation(fan, i);
        }
    }

    private boolean isKnownByAll(int person){
        return peopleGraph.numberOfEdgesPointingTo(person) == peopleNumber - 1;
    }

}
