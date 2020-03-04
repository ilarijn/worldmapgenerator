## Testing
Run tests with `./gradlew test` for feedback.

You can run the priority queue performance test *n* times with `./gradlew test -Ddijkstra=n`

### Algorithms

#### Noise functions
Tests for the Perlin noise and diamond-square classes consist of confirming that the produced arrays only contain values within the accepted range of [-1.0, 1.0].

Beyond this it seems to me that there is not much to test with unit tests, since functionality is confirmed by looking at a generated map. More tests would be needed if there were specific requirements for generated maps such as "must have at least one flat area of at least size 50x50".

#### Rivers 
Graph integrity is tested by verifying that neighbors and node numbers are correctly formed on the basis of array positions. 

Dijkstra's algorithm is tested by giving it a hardcoded array and confirming shortest paths and distances to different nodes. The tests print out an overview when run.

The application of rivers is tested by applying one to a hardcoded heightmap. The appropriate array points are then checked for correct values. 

### Data structures and utility functions

#### NodePQ
Poll functionality is tested by inserting nodes to the queue and confirming that the polling order is correct. 

#### IntegerSet
Basic operations are tested: you shouldn't be able to add duplicate elements to a set and set size should be correctly managed.

#### Random
100 000 numbers are generated and confirmed to be within the range of [0, 1.0[.

#### Math
Math functions are tested by comparing return values to expected results where this is not redundant.

### Performance testing

#### NodePQ vs PriorityQueue in Dijkstra's algorithm
Across 100 iterations of finding all shortest paths in graphs generated from random heightmaps of size [1000][1000], the average running times of Dijkstra's algorithm using either NodePQ or PriorityQueue were:

|               |       |
|---------------|-------|
| NodePQ        | 2.33s |
| PriorityQueue | 1.92s |

Our heap structure performs around 20% slower than the standard implementation, which is still fine for the purposes of this project.