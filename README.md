# Large file reading challenge

Thank You for taking the time to review this solution to the challenge. 
In this project I have prepared a couple of different solutions to the problem at hand, with one of them being the designated implementation.

## Proposed Solutions

### Solution 1: Stream the file and aggregate temperatures in a map

This is the designated solution which can be accessed through the 
`/temperature/agg/{city}` endpoint. The CSV file is streamed and the data is split into simple type variables.
Then the data is merged into a Map which contains the sum and count of temperature measurements per year.
After all temperatures are summed, the average is calculated.

### Solution 2: Stream the file and calculate through a groupingBy collector

Can be found under the `/temperature/funct/{city}` endpoint.
A solution very similar to the previous one, with the main difference being aggregation implemented with the help of `Collectors.groupingBy`.
This solution appears more elegant as it takes full advantage of functional programming, however we have to keep in mind the size of the processed file. 
When that is taken into consideration the memory usage of this solution is less efficient than that of Solution number 1.

### Solution 3: Use Spring Batch

Can be found under the `/temperature/batch/{city}` endpoint.
This is more of an experiment than a real optimized solution. Having heard of `Spring Batch`, but without ever using it before, 
it sounded like a great alternative to the solutions presented above. But after all it seems that Spring Batch is not the right approach,
as on average it seems to run 10-20 times slower than the previous solutions (despite using multiple threads!). 
However, this dip in performance could be caused by my being inexperienced with the framework. 
I tried to take this one more of a learning opportunity.

## How to launch

In order to launch the application either use an IDE of choice or execute `docker-compose up` from this directory.

## Conclusion

The presented solutions vary in complexity, speed and elegance,
but once again keeping the potential file size in mind optimization should be the main focus, that is why Solution 1 is my main choice.
That does not mean the solution is perfect and 100% optimized - the first improvement (to speed, definitely a hit to the elegance and complexity)
would be to use concurrency. But doing that would introduce a lot of new challenges such as manually splitting the file into equal-sized
chunks. Multiple threads reading from the same file significantly reduces the performance as opposed to a sequential read (one thread).
Perhaps one thread should read the CSV contents and pass the data to other worker threads. Such a solution would most likely improve the performance 
by a decent margin, but for the sake of readability and testability I opted to stick to the sequential solution.
