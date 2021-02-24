
1. Given any two functions f(n) and g(n), show that f(n) + g(n) = Θ(max{f(n),g(n)}))
    (10P).P).).
2. Show that f(n) =n^2 + 2n+ 1 is Θ(n^2 ) using induction. ( If you use l’Hopital, you will
    lose points.)(5P).).
3. P).rove the functions below (40P).P).).
    a) If f(n) = 10P). log(n) + 5 (log(n))^3 + 7n + 3n^2 + 6n^3 , then f(n) = O(n^3 ) (5P).)
    b) 1 = O(n) (5P).)
    c) n = O(n^2 ) (5P).)
    d) log(n) = O(n), 2 n + 1 = O(n) (5P).)
    e) n = Ω(1) (5P).)
    f) n^2 = Ω(n) (5P).)
    g) n^2 = Ω(n log(n)) (5P).)
    h) 2 n + 1 = Θ(n) (5P).)
4. Sort the following functions from fastest to slowest with respect to their growth
    rate. Do not use l’Hopital! P).rove all of them using induction (20P).P).).

```
n!, nk+n , n, logn, n(logn), e^7 , 20P).19, -7n+m, n^4 , 10P).0P).*n
```
```
*k and m are constants.
```
5. Explain the time complexity of the code snippets below (10P).P).).

```
a-)System.out.println = SOP
void method4(int [] arr) {
for(int i = 0; i < arr.length; i++) {
for(int k = arr.length - 1; k > 0; k = k / 3 ) {
SOP(arr[i]);
}
}
}
```
b-)
void method3(int [] arr)
{
for(int i = 0; i < arr.length; i++)
{
method1(arr);
method2(arr);
}


## }

```
void method1(int [] arr)
{
int n = arr.length;
```
```
for(int i = n - 1 ; i >= 0; i = i - 3)
{
SOP (arr[i]);
}
}
```
6. Calculate the time complexity of the following recurrence
functions (Use the master theorem)(10P)
    - T(n) = T(n/7) + n^4
    - T(n) = T(n/99) + n^75
    - T(n) = 2^3 T(n/12) + 6
7. Write mergesort with pseudo-code and analyze the
algorithm’s worst case, best case and average case using
asymptotic notations(15P).

## • PS:

- If you have any questions about the hw, please send an email
    to ogoksu@gtu.edu.tr
- Your submission should be handwritten. Do not send any files
    as .pdf, .pptx etc.
- You should hand over your submission to (Office No: 109) Özgü
    Göksu, before the due date.

```
Good Luck!
```

