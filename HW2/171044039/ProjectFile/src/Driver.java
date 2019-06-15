import java.sql.Time;

/**
 * Driver Class for ExperimentList.
 * Contains the test code for Experiment and
 * ExperimentList Class.
 * @see Experiment
 * @see ExperimentList
 * @author Ahmed Semih Özmekik
 */
public class Driver
{
    /**
     * Shows the Experiment List.
     * @param list Experiment List.
     */
    public static void show(ExperimentList list)
    {
        for (Experiment e:list)
            print(e.toString());
    }

    /**
     * Prints the string with DEBUG annotation.
     * @param debug Debug string for info.
     */
    public static void print(String debug)
    {
        System.out.println("[DEBUG]  " + debug);
    }

    public static void main(String args[])
    {
        ExperimentList list = new ExperimentList();

        Experiment[] expArray = new Experiment[5];

        // Testing the addExp() method.
        for (int i=0;i<expArray.length;++i){
            expArray[i] = new Experiment(i+1, "setup" + i);
            if(i%2 == 0){
                expArray[i].setCompleted(true);
                expArray[i].setAccuracy(i*i);
            }
            list.addExp(expArray[i]);
        }

        print("Starting to Test...");
        print("{  ExperimentList  }:");
        show(list);

        // Testing addExp() more.
        list.addExp(new Experiment(1,"new0"));
        list.addExp(new Experiment(1,"new1"));
        list.addExp(new Experiment(3, "new2"));
        list.addExp(new Experiment(1, "new3"));

        print("{  ExperimentList  } + Experiments(new0, new1, new2, new3):");
        show(list);

        // Testing getExp().
        print("{  GET EXPERIMENT FROM LIST  } (Exp[day1, 3th], Exp[day5, 1st]):");
        print(list.getExp(1,3).toString());
        print(list.getExp(5,1).toString());

        //Testing setExp().
        print("{  SET EXPERIMENT FROM LIST  } (Exp[day3, 2nd], Exp[day1,4th])");
        list.setExp(3,2, new Experiment(3, "SET1"));
        list.setExp(1,4, new Experiment(1,"SET2"));
        show(list);

        //Testing orderDay().
        int i=100;
        for (Experiment e:list)
            e.setAccuracy(i-=11);
        print("{ ExperimentList  } (Not in order)");
        show(list);
        list.orderDay(1);
        list.orderDay(3);
        print("{  ExperimentList  } (1st and 3th Days are in order)");
        show(list);

        //Testing orderExperiments().
        ExperimentList orderedList = list.orderExperiments();
        print("{ OrderedList } (ExperimentList is in order.)");
        show(orderedList);




        //Testing removeExp().
        list.removeExp(4,1);
        list.removeExp(1,1);
        list.removeExp(3,2);
        list.removeExp(3,1);
        print("{  ExperimentList  } - [day4, 1st], [day1, 1st], [day3, 2nd], [day3, 1st] ");

        show(list);


        //Testing listExp()..
        list.getExp(1,1).setCompleted(true);
        list.getExp(1,2).setCompleted(true);
        print("{  ExperimentList  } (Day 1 Completed Experiments:(1st, 2nd))");
        list.listExp(1);

        //Testing removeDay().
        list.removeDay(1);
        print("{  ExperimentList  } (Day 1 is removed)");
        show(list);

        print("Testing is Over...");


    }
}
