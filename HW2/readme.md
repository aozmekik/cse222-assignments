
In this homework you will create an ExperimentList class to keep track of some machine
learning experiments and their results. A machine learning experiment consists of the following
instance variables:

- setup (String): explains the experimental setup
- day(integer): represents the day of start
- time(Time): represents the time of start
- completed(boolean): indicates whether it is completed or not
- accuracy(float): represents the output (not a valid value if the experiment is not
    completed)

Your class should implement the basics of a single linked list to keep the experiments. In order
to speed up add and remove operations, an additional list structure should be defined in the
level of days.

Your class should support the following functionality:

1. addExp(Experiment): insert experiment to the end of the day
2. getExp(day, index) : get the experiment with the given day and position
3. setExp(day, index, ) set the experiment with the given day and position
4. removeExp(day, index): remove the experiment specified as index from given day
5. listExp(day): list all completed experiments in a given day
6. removeDay(day): remove all experiments in a given day
7. orderDay(day): sorts the experiments in a given day according to the accuracy, the
    changes will be done on the list
8. orderExperiments(): sorts all the experiments in the list according to the accuracy, the
    original list should not be changed since the day list may be damage

Additional requirements are listed as follows:

1. ExperimentList class should be iterable.
2. Do not use any class from Java Collections library, each part should be written from
    scratch.
3. Write a driver class (i.e. Main class) which tests all the functionality of your
    ExperimentList class.
4. Include a table in your report which shows the complexity of all the functions and give
    details on how you calculate it.


**Notes:**

1. Submit your homework as studentnumber.zip and which includes the following files:
    - intelliJ project file
    - Report.pdf
    - Javadoc
2. The report must be in format “ReportFormat.doc” (moodle)
3. The implementations will be 75 points and the report is 25 points out of 100
4. Submit your homework until the last submission date
5. You can ask your questions via email: asturan@gtu.edu.tr or by coming to office 118


