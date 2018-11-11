# Intervals

Implement a MERGE function that takes a list of intervals 
and returns a list of intervals. 
In the result all overlapping intervals should be merged. 
All non-overlapping intervals remain unchanged. 

###### Sample:

Input: `[25,30] [2,19] [14, 23] [4,8]`

Output: `[2,23] [25,30]`

###### Questions to be answered:

- What's the performance of your program? (Complexity?)
- How can robustness be ensured, especially considering huge input?
- How is the memory consumption of your program?

## Considerations

Considerations may change over time and even contradict each other. 
Nevertheless I put them in chronological order (more or less) as they appear in my mind.

*   I'll implement the solution in Java because that's the language I'm most familiar with.
For this reason I'll phrase some of my considerations in Java-terminology as well. 

*   Although the sample operates on integers, the task talks about intervals in general. 
So probably it makes sense not to limit the API to integers. 
  
    In my understanding an interval can be formed by any two values 
that are comparable to each other. Any interval that is comparable 
to any other interval can be merged. 
   
    Therefore I'm tempted to base the API on a generic type 
extending (implementing) the `Comparable` interface.
  
    Probably something like:
    ```java
    public interface IntervalMerger<T extends Comparable<T>> {   
        List<Interval<T>> merge(List<Interval<T>> intervals);
    }
    ```
  
    ... where `Interval` will look more or less like this:
  
    ```java
    public final class Interval<T extends Comparable<T>> implements Comparable<Interval<T>> {
        private T lowerBoundInclusive;
        private T upperBoundExclusive;
        
        public Interval(T lowerBoundInclusive, T upperBoundExclusive) {
            this.lowerBoundInclusive = lowerBoundInclusive;
            this.upperBoundExclusive = upperBoundExclusive;
        }
        
        public T getFirst() {
            return lowerBoundInclusive;
        }
        
        public T getLast() {
            return upperBoundExclusive;
        }
        
        @Override
        public int compareTo(Interval<T> other) {
            return ComparisonChain.start()
                    .compare(this.lowerBoundInclusive, other.lowerBoundInclusive)
                    .compare(this.upperBoundExclusive, other.upperBoundExclusive)
                    .result();
        }
    
    }
    ```
*   Well, thinking in Java the sample actually suggests a slightly different public API.
 
    So I'll define this less type-safe function as well:
    
    ```java
    public interface IntervalMerger<T extends Comparable<T>> {
        List<Interval<T>> merge(List<Interval<T>> intervals); // probably I'll use this one internally
        List<T[]> mergePrimitive(List<T[]> intervals); // the signature suggested by the sample
    }
    
    ```
  
* There seems to be a focus on performance especially when considering potentially huge input.
  
  This suggests that it will make sense to focus on scalability rather than optimization. 
  
  Even the most optimized algorithm will hit hard limits when executed by a single instance,
  while even unoptimized algorithms can perform (almost) arbitrarily well
  if load can be shared across an arbitrary number of collaborating service instances.
  
* Saying this I become aware that the API supports scalability by its' statelessness
  (the task implies a purely functional approach which makes scaling much easier).
  
  To ensure scalability we just need to ensure that portions of effort can be separated.
  So some thoughts on that:
  
* It seems that the problem boils down to looking at two intervals and checking 
  if they can be merged. And if yes, merge them. 
  If this simple task is repeated for any possible pair of intervals we are done. 
  
  This means that the possibility to share the effort between multiple instances 
  (or threads or whatever) 
  comes for free by the nature of the problem. Great!
  
  So we can ...
  * split an input array to any number of sub-arrays
  * let them be processed (in parallel) by an equal number of processors (threads, service-instances, whatever)
  * collect the merged (reduced) results for these parts
  * and repeat this until no merge can be done any more
  
* The basic idea of `quick sort` comes into my mind 
  when I think of this recursive way of dividing and conquering. 
  
  This approach would imply a time complexity of `Θ(n log(n))` in average 
  and a time complexity of `O(n^2)` for the worst case. 
  
  Different approaches might provide a better performance but as mentioned before
  I think that given the problem statement it makes more sense to initially invest effort 
  into scalability rather than optimization. 
  Especially since performance improvement can probably only be achieved by memory trade-off.
  
  Finally I'll probably go for avoiding pre-mature optimization by implementing 
  the most straightforward scalable solution. 
  
* By defining a public interface being implemented by a non-public class
  I'll decouple clients that rely on DI (IOC) from future alternatives 
  that are optimized in a different way. 

* After having defined the API, my next step will be to implement unit tests
  that also help to align the requirements with the stakeholders ...
  
  This raises some questions in advance:
  
    * Neither the requirement description nor the sample indicate if `lowerBoundInclusive` or `upperBoundExclusive` value of an interval are inclusive or  exclusive.

      For example:
  
      **Should or shouldn't `[a,b][b,c]` be merged to `[a,c]`?**
      
      In a real-world scenario I now have two options regarding how to proceed:
      
      * Either I clarify this in advance by asking the stakeholders if they have a strong preference for any specification.
            
      * Or I find which option is most preferable from implementation (and/or -performance) point of view
        and suggest that option to the stakeholder, reasoning why this would be the "better" option.
        
        * In that case I could implement unit tests for both options by using JUnit *assumptions* (`Assume.assumeThat(...)`).
      
      Since I'm in a demo-scenario where I just want to save effort, I'll initially avoid testing these edge-cases until I know how it works best,
      and afterwards add according tests to address regression issues.
      
      *Update*: thinking more about merging `[a,b][b,c]` to `[a,c]` it now seems quite clear to me
      that these intervals definitely should be merged for the following reason:  
      Regardless if the upper bound of the lower interval (`[a,b]`) is inclusive or exclusive
      there is not single value `>= a <c` that doesn't fall into either of the intervals. 
      Assuming that the intention of joining intervals is to cover values with the least possible amount of intervals
      not joining them wouldn't make sense.
      
* When starting to think closer about the QuickSort-like implementation I found 
  that this is quite complex to implement. 
  
  So I'll go for a two-step approach: lowerBoundInclusive sort then merge. 
  This looks a bit less elegant but seems to be much easier to implement. 
  Performance-wise it's probably close to (or even better than)the one-step approach, because existing 
  sorting algorithms are usually highly optimized. 
  
  I'll create a sorted set of the input list, then iterate over it (in ascending order) 
  and either update the latest element of the result list or append a new one.
  
* It turned out that this implementation is incredibly simple. And it seems to work like charm.

## Remarks

* The time complexity of the implemented algorithm should be 
  * `O(log(n))` for inserting into the sorted set (a balanced binary tree)
  * plus `O(n)` for iterating the sorted set and inserting results
  
* The space complexity should be
  * `O(n)` for the sorted set
  * plus `O(n)` (worst case, if nothing can be merged) for the result list.

  Since the input data is owned by the client I'm not adding it to space considerations
  for the algorithm.
  
* The current solution intentionally focuses on readability, testability and maintainability rather than
  focusing on performance. Using array of primitives (e.g. `int[]`) instead of polymorphic
  java classes might significantly improve performance. 
  
  The beauty of the current solution lays in the fact that almost every detail 
  can be tested in isolation
  
* The test code uses the junit4 feature of parameterized tests. In my opinion apart from
  reducing amount of code this also significantly increases readability. 
  Just looking at the data returned by `getScenarios` provides you almost all relevant
  information in one place. 
  
  A downside of this approach is that you need multiple test classes for testing a single class,
  which is a bit unusual. 
  
  JUnit5 has a better feature that allows parameterizing more fine-grained, but as far as I remember
  this feature is still documented as being experimental. 

## Next Steps

(...without knowing if I'll ever implement them...)

* Introduce a size-limit for the input list. 

  The memory consumption for the current implementation (SimpleMerger) is quite high:
  
  * The input-list provided by the client is copied to a sorted set
  * The result list also consumes memory - if not too many intervals can be merged
    this will be significant as well.
    
* Stress-test the solution. In particular I'd be interested in the following findings:
  * What value for the size limit makes sense for a service instance on a particular platform 
  (e.g. an EC2 instance with particular sizing)
  * What's the actual overhead of the quite generic solution that operates on high-level java objects
  rather than plain arrays of primitives.  

* Verify that splitting the input list, 
  processing each sublist separately and merging the results actually works as I expect.
  This would just require some further relatively simple unit-tests. 

# Bonus Task

Under certain conditions the problem can be solved in O(n).
How does this work and what are the conditions?

A solution should be found for a non-sorted interval list as input. 

## Ideas

After some initial ideas that I discarded I got one that looks promising so far:

### Idea 1

#### Conditions

* Intervals are boundaries for discrete values - e.g. integers. 
  So any interval specifies a countable set of values. 

* Relatively small amount of discrete values within the *range*.
  By *range* I mean everything enclosed between the lowest lower bound and the highest upper bound.
  By relatively small amount I mean relatively small compared to the number of intervals.
  Ideally the number of values covered by intervals is less then or equal to the numbers of intervals.
  Otherwise we would talk about a different `n` when comparing complexity to `O(n*log(n))`
  of the previous solution.  
  
  In the previous solution `n` was the number of intervals.  
  In this solution `n` would be the number of discrete values within the range - so the cardinality of the range. 
  
#### Approach

We could map all possible values within the range as array indices. 
Any value that is referred by an interval bound (lower or upper) would be represented 
by a record in the array (or ArrayList), whose index is the bound. A record could look like this:
```java
class BoundInfo {
    int lowerBoundOccurrencesCount;
    int upperBoundOccurrencesCount;
}
```

This would consume quite a lot of memory - even for the lot of array elements that are actually unused.

Therefore I go for a `BitSet`, using 2 bits for every discrete value within the range.
Every first bit indicates if this value is used as a lower bound, every 2nd bit indicates if it is used as an upper bound. 
Whenever a bit is set, the count of occurrences in a HashMap entry (with the index as a key) is incremented.
This way we safe a lot of memory, especially if there are relatively few relatively big ranges.

I implemented it like this and it looks like a quite efficient and elegant solution.

#### TODO

Consider a dynamic offset solution for the BitSet (see comments in code).

Consider to improve readability and maintainability by implementing a utility class
for BitSet and HashMap handling. 