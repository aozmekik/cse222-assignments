Important Notes:

1. In your homework, you should explicitly define a graph ADT and use the graph object for all
    operations.
2. You can use adjacency matrix, adjacency list or any other method to implement your graph.
3. You are not allowed to use java.util.Collections in any level of the implementation. If you
    need to use a data structure, you should implement it.



**Description**

We have a group of people in which an ordered popularity relation is defined between person
pairs. If there exist a relation such that (P1,P2) this means that A thinks that B is popular. The re-
lation is transitive which means that if the relations (P1,P2) and (P2,P3) exist, than (P 1 ,P 3 ) also ex-
ist event if it is not specified by the input pairs. You are supposed to write a Java program which
finds the people who are considered popular by every other person.

**Input (input.txt)**

* Line 1: Two space-separated integers, N (number of people) and M (number of ordered rela-
tions)

* Lines 2..1+M: Two space-separated numbers P1 and P2, meaning that P1 thinks P2 is popular.

**Output**

* Line 1: An integer which represents the number of people who are considered popular by every
other person.

**Sample Input**

## 3 3

## 1 2

## 2 1

## 2 3

**Sample Output**

1

Submit your homework with file name <stdID>.zip which includes your IntelliJ project and your
report. You can ask your questions via asturan@gtu.edu.tr or moodle discussion forum.


