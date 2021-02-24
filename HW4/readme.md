
# For the questions 1-4, prepare a pdf document with the same font as the Report

# Format. For the last question prepare a Java project with a main class. Submit

# your work as studentID.zip

1. Given a single linked list of integers, we want to find the maximum length sorted sublist in
this list. For example for the list L= {1, 9, 2, 7, 20, 13} the returned list should be S = {2, 7,
20}.

```
a. Write an iterative function which performs this task. Analyze its complexity.
b. Write a recursive function for the same purpose. Analyze its complexity by using
both the Master theorem and induction.
```
2. Describe and analyze a Θ (n) time algorithm that given a sorted array searches two
numbers in the array whose sum is exactly x.
3. Calculate the running time of the code snippet below

```
for (i=2*n; i>=1; i=i-1)
for (j=1; j<=i; j=j+1)
for (k=1; k<=j; k=k*3)
print(“hello”)
```
4. Write a recurrence relation for the following function and analyze its time
complexity T(n).
5. It’s the year 2019, and the Göktürk 3 satellite is in orbit. You work as a data analyst for the
Turkish Space Agency. The satellite transmits data as 2D arrays (not necessarily square) of non
negative integers. You are tasked with coding an iterator class for these data that will traverse a
given 2D array spirally clockwise starting at the top left element.


Example: if the data is

```
1 2 3 4
5 6 7 8
9 10 11 12
```
(^13141516)
the iterator should iterate it in the order: 1 2 3 4 8 12 16 15 14 13 9 5 6 7 11 10. Code in java
this iterator recursively, and provide a main class showing it in action. Do not implement its
remove method (final exam question from 2 years ago of BIL443; it was worth 15 points at the
time).


