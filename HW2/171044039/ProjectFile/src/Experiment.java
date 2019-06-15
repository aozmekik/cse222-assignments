import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A Machine Learning Experiment.
 * Represents an experiment which has simple attributes such as
 * the starting date of experiment or accuracy representing the output.
 *
 * @see ExperimentList
 * @author Ahmed Semih Özmekik
 */
public class Experiment implements Comparable<Experiment>
{
    /*
     * Explains the experimental setup.
     */
    private final String setup;

    /*
     * Represents the day of start.
     */
    private final int day;

    /*
     * Represents the time of start.
     */
    private final String time;


    /*
     * Indicates whether the experiment is completed or not.
     */
    private boolean completed = false;

    /*
     * Represents the output of experiment. Not a valid value if the
     * experiment is not completed.
     */
    private float accuracy = -1f;

    /**
     * Creates the experiment with given day and setup string.
     * @param day day of the experiment.
     * @param setup
     */
    public Experiment(int day, String setup) {
        if (day<=0 || day>30)
            throw new IllegalArgumentException("Invalid 'day' arg!");

        this.day = day;
        this.setup = setup;

        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        Date date = new Date();
        time = dateFormat.format(date);
    }

    /**
     * Sets the completeness of the experiment.
     * @param completed true if the experiment is completed,
     *                  false otherwise.
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
        accuracy = 0f; // becomes valid.
    }

    /**
     * Gets the accuracy rate of the experiment.
     * @param accuracy Accuracy rate of the experiment.
     */
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * Gets the experimental setup.
     * @return setup.
     */
    public String getSetup() {
        return setup;
    }

    /**
     * Gets the day of the experiment.
     * @return day.
     */
    public int getDay() {
        return day;
    }

    /**
     * Gets the time of the experiment.
     * @return the time.
     * @see Time
     */
    public String getTime() {
        return time;
    }

    /**
     * Returns the completeness of the experiment.
     * @return true if experiment is completed, false otherwise.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Gets the accuracy rate of the experiment.
     * @return accuracy.
     */
    public float getAccuracy() {
        return accuracy;
    }

    /**
     * Gets the brief experiment output.
     * @return String of experiment.
     */
    @Override
    public String toString() {
        return "Day[" + getDay() + "]->{ Setup: " + getSetup() + " }"
                + "  (Accuracy: " + getAccuracy() + ") " + "(Completed: "
                +isCompleted() +")" + "   (Time:"+ getTime() + ")";
    }


    /**
     * Compares the accuracy rates of the experiment.
     * @param other is the other experiment to be compared with this.
     * @return negative: if this.accuracy<other.accuracy
     *         zero: this.accuracy == other.accuracy
     *         positive: this.accuracy > other.accuracy
     */
    @Override
    public int compareTo(Experiment other) {
        return Float.compare(getAccuracy(), other.getAccuracy());
    }
}
