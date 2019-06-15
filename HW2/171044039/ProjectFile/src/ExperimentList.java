import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class ExperimentList implements Iterable<Experiment>
{
    private ExpNode headExp; // head of the experiments.
    private DayNode headDay; // head of the days.
    private int size = 0;

    /**
     * Creates the Experiment List with no initial pointer nor data.
     */
    public ExperimentList()
    {
        headDay = null;
        headExp = null;
    }

    /**
     * Creates the Experiment List with given experiment.
     * @param exp is the first element of the list.
     */
    public ExperimentList(Experiment exp)
    {
        headExp = new ExpNode(exp);
        headDay = new DayNode(headExp);
        ++size;
    }

    /**
     * Inserts the experiment to the end of the day.
     * @param exp is the experiment to be inserted into the end of the day.
     */
    public void addExp(Experiment exp)
    {
        ++size;
        if (headExp == null) {
            headExp = new ExpNode(exp);
            headDay = new DayNode(headExp);
        }
        else // DayNode handles the details.
            headDay.add(exp);
    }

    /**
     * Gets the experiment with the given day and position.
     * @param day is the the desired experiment's day.
     * @param index is the desired experiment's index in the day.
     * @return Desired experiment with the given day and position.
     */
    public Experiment getExp(int day, int index)
    {
        return headDay.getDay(day).getExp(index).data;
    }

    /**
     * Sets the experiment with the given day and position.
     * @param day is the the desired experiment's day.
     * @param index is the desired experiment's index in the day.
     * @param newExp is the experiment to be set to the given position.
     *               Given new Experiment's must be same with the given
     *               day index.
     */
    public void setExp(int day, int index, Experiment newExp)
    {
        ExpNode exp = headDay.getDay(day).getExp(index);
        if (day != newExp.getDay())
            throw new IllegalArgumentException("Experiment's day conflicts!");
        exp.data = newExp;
    }

    /**
     * Removes the experiment specified as index from given day.
     * @param day is the day of experiment to remove.
     * @param index is the index of experiment to remove.
     * @return the removed experiment from list.
     */
    public Experiment removeExp(int day, int index)
    {
        ExpNode temp = headDay.getDay(day).getExp(index); // Get the element we want to remove.

        if (headDay.getDay(day).size == 1) { // Day will be removed.
            if (headDay.day == day){ // Head will be changed.
                headDay = headDay.next;
                headExp = headExp.next;
            }
            else { // Find the previous DayNode and ExpNode.
                DayNode prevDay = headDay.getDay(day-1);
                ExpNode prev = prevDay.getExp(prevDay.size);

                prevDay.next = prevDay.next.next; // Remove the day.
                prev.next = prev.next.next; // Remove the experiment.
            }
        }
        else if(index == 1) { // Day won't be removed, but the head exp will change.
            if (headDay.day == day){ // Change the heads.
                headDay.head = headDay.head.next;
                headExp = headExp.next;
                --headDay.size;
            }
            else { // Find the previous DayNode and ExpNode.
                --headDay.getDay(day).size;

                DayNode prevDay = headDay.getDay(day-1);
                ExpNode prev = prevDay.getExp(prevDay.size);
                // Remove the exp.
                prevDay.lastExp().next = prevDay.head.next;
                prev.next = prev.next.next;
            }
        }
        else{ // Again, find the previous ExpNode.
            ExpNode prev = headDay.getDay(day).getExp(index-1);

            --headDay.getDay(day).size;
            prev.next = prev.next.next;  // Remove the experiment.
        }

        --size;
        return temp.data;
    }

    /**
     * List all completed experiments in a given day.
     * @param day is the day of experiments to be listed.
     */
    public void listExp(int day)
    {
        DayNode theDay = headDay.getDay(day);
        ExpNode exp = theDay.head;

        for (int i=0;i<theDay.size;++i){
            if (exp.data.isCompleted())
                System.out.println(exp.data.toString());
            exp = exp.next;
        }

    }

    /**
     * Remove all experiments in a given day.
     * @param day is the day of experiments to be removed.
     */
    public void removeDay(int day)
    {
        if (headDay.day == day){ // Head day will be removed.
            headExp = headDay.lastExp().next;
            headDay = headDay.next;
        }

        else{
            // Get the previous day and the Day.
            DayNode prev = headDay.getDay(day-1);
            DayNode theDay = headDay.getDay(day);

            // Connect nodes and delete the Day.
            prev.lastExp().next =  theDay.lastExp().next;
            prev.next = theDay.next;

        }
    }

    /**
     * Sorts the experiments in a given day according to the accuracy, the
     * changes will be done on the list.
     * @param day is the day to be sorted.
     */
    public void orderDay(int day)
    {
        DayNode theDay = headDay.getDay(day);
        Experiment[] expData = new Experiment[theDay.size];

        for (int i=0;i<expData.length;++i) // Get the experiments to array.
            expData[i] = theDay.getDay(day).getExp(i+1).data;

        Arrays.sort(expData);

        for (int i=0;i<expData.length;++i) // Set the experiment from array.
            setExp(day,i+1,expData[i]);

    }

    /**
     * Sorts all the experiments in the list according to the accuracy, the
     * original list should not be changed since the day list may be damage.
     * @return New ordered experiment list.
     */
    public ExperimentList orderExperiments()
    {
        ExperimentList orderedList = new ExperimentList();
        Experiment[] expData = new Experiment[size];


        int i=0; // Iterate the list and get the experiments.
        for (Experiment e : this)
            expData[i++] = e;

        Arrays.sort(expData);

        orderedList.headDay = null; // Since the day order is corrupted, no need.
        orderedList.size = expData.length;

        ExpNode expNode = new ExpNode(expData[0]);
        orderedList.headExp = expNode; // Connect to head.
        for (i=1;i<expData.length;++i){ // Set the experiments to connected exp nodes.
            expNode.next = new ExpNode(expData[i]);
            expNode = expNode.next;
        }

        return orderedList;
    }

    @NotNull
    @Override
    public Iterator<Experiment> iterator() {
        return new ExpIterator(0);
    }


    /**
     * Represents the Custom Iterator for ExperimentList.
     */
    class ExpIterator implements Iterator<Experiment> {
        private ExpNode nextExp; // next experiment.
        private Experiment lastExpReturned;
        private int index = 0;

        /**
         * Returns the experiment iterator over the experiment list.
         * @param i is the index of iterator.
         * @exception IndexOutOfBoundsException if the given index(i)
         *            is invalid for that list.
         */
        public ExpIterator(int i) {
            if (i < 0 || i > size) // Out of Boundaries.
                throw new IndexOutOfBoundsException("Invalid index: " + i);
            lastExpReturned = null;
            if (i == size) {
                index = size;
                nextExp = null;
            } else {
                nextExp = headExp;
                for (index = 0; index < i; ++index)
                    nextExp = nextExp.next;
            }
        }

        /**
         * Informs the iterator has next element or not.
         * @return true if there exist next element, false otherwise.
         */
        @Override
        public boolean hasNext() {
            return nextExp != null;
        }

        /**
         * Returns the next experiment and increments the iterator.
         * @return the next experiment.
         */
        @Override
        public Experiment next() {
            lastExpReturned = nextExp.data;
            nextExp = nextExp.next;
            return lastExpReturned;
        }


    }

    /**
     * Represents the Experiment Node.
     * Implemented as inner class, to keep it in the context.
     * Experiment Node has next experiment pointer beside of the data itself.
     * @see Experiment
     */
    private static class ExpNode
    {
        private ExpNode next; // pointer to next experiment.
        private Experiment data; // data of the node.

        /**
         * Creates the Experiment node with given Experiment.
         * @param exp is the data field of experiment node.
         *            Should not be null.
         */
        private ExpNode(Experiment exp)
        {
            next = null;
            data = exp;
        }
    }

    /**
     * Represents the Day Node. DayNode has experiments and points to
     * next DayNode.
     * @see Experiment
     */
    private static class DayNode
    {
        private DayNode next; // pointer to next day.
        private ExpNode head; // head experiment of the day.
        private int size = 0; // number of experiment in that day.
        private final int day;

        /**
         * Creates a Day Node with starting experiment of the day.
         * @param expNode is the first experiment of the day.
         */
        public DayNode(ExpNode expNode)
        {
            head = expNode;
            next = null;
            ++size;
            day = head.data.getDay();
        }

        /**
         * Inserts the given experiment to the appropriate place among days.
         * @param exp is the experiment which will be added.
         */
        private void add(Experiment exp)
        {
            // First, create the Experiment Node.
            ExpNode newExp = new ExpNode(exp);

            // Case: Experiment's day is this node's day.
            if (day == exp.getDay())
                addToEnd(newExp);

            // Case: Experiment's day is another next node's day.
            // There is no another next day :  (day2-0) -> (day2-day3)
            else if(next == null)
            {
                next = new DayNode(newExp);
                lastExp().next = next.head; // connecting the experiment of different days.
            }
            // The day doesn't exist and must be created in between days.
            // (day2-0-day4) -> (day2-day3-day4)
            else if(next.day > exp.getDay())
            {
                DayNode newDay = new DayNode(newExp);
                newDay.next = next;
                newDay.head.next = next.head;
                next = newDay;
                lastExp().next = newDay.head;
            }
            else
                next.add(exp);

        }

        /**
         * Inserts the specified experiment node end of the day node.
         * @param expNode is the experiment which will be added to end of the day.
         */
        private void addToEnd(ExpNode expNode)
        {
            // Get the last experiment of the day.
            ExpNode last = lastExp();

            // Connect.
            expNode.next = last.next;
            last.next = expNode;
            ++size;
        }


        /**
         * Returns the last experiment of the day.
         * @return ExpNode is the last experiment of the day.
         */
        private ExpNode lastExp()
        {
            // Shift the exp. to the end of the day.
            ExpNode temp = head;
            while(temp.next != null && temp.next.data.getDay() == day)
                temp = temp.next;
            return temp;
        }

        /**
         * Returns the index'th experiment of the day.
         * @param index is the index of experiment in the day.
         * @return the Experiment Node of that index at that day.
         * @exception IndexOutOfBoundsException if there is no
         *             experiment in that index.
         */
        private ExpNode getExp(int index)
        {
            // Shift the exp. to the index.
            ExpNode temp = head;

            for (int i=1;i<index && temp != null;++i) // Shift the exp to experiment index.
                temp = temp.next;

            if (temp == null || temp.data.getDay() != day) // Experiment does not exist.
                throw new IndexOutOfBoundsException();

            return temp;

        }

        /**
         * Search the given day among the days. If it exists returns the day
         * node. Otherwise, throws an exception.
         * @param day is the day number.
         * @return the desired Day Node with given day.
         * @throws IndexOutOfBoundsException if the day index does not
         * exist among the days.
         */
        private DayNode getDay(int day)
        {

            if (this.day == day)
                return this;
            else if (next == null)
                throw new IndexOutOfBoundsException();
            else
                return next.getDay(day);
        }

    }
}
