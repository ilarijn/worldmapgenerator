### Implementation details
The application draws two-dimensional arrays, or heightmaps, on a JavaFX canvas where each array value is a pixel that is assigned an appropriate color depending on display mode. 

There are two algorithms implemented for the generation of maps, Perlin noise and diamond-square. Both are so-called noise functions and produce a 2D array of double values.

There is also a class for adding rivers to a heightmap. This works by using Dijkstra's algorithm to find shortest paths in a graph created from the heightmap and adjusting the original's values on the river's path.

Below are given descriptions and details related to the implementation of algorithms, data structures and functions used in this project. Last are listed any that were used but not rewritten along with the reasons thereof. 

### Algorithms

#### Perlin noise
Perlin noise works by filling an n-dimensional grid with n-dimensional random vectors on the basis of which noise values are determined. Noise value is computed by first determining which cell the grid point falls into. Cell is a separate concept from grid point, and is relative to current scale: i.e. there are typically multiple grid points located inside a cell. Then, we compute the dot products between the distance vector of the given point and each of the random vectors at the corners of the corresponding grid point. Finally, we interpolate between the dot products and return the value.

Typically we add together n iterations of noise, where each iteration is called an octave. The scale and amplitude (a factor applied to noise values to affect their range) values are adjusted for each successive octave. This layering causes the "fractal-like" effect and creates maps with more details.  

Perlin noise has a time complexity of O(2^n) where n equals number of dimensions, with significant constant factors due to the application of math functions and several layers of noise. In practice, the time complexity should be O(n) for this project.

#### Diamond-square algorithm
The diamond-square algorithm works by filling a 2D grid of width and height 2^n+1 with the averages of the corner points of each "square" and each "diamond" plus a random value. It must be initialized by assigning values to the four corners points in the grid. The below image illustrates the steps of the algorithm:

![Wikimedia Commons](https://upload.wikimedia.org/wikipedia/commons/thumb/b/bf/Diamond_Square.svg/640px-Diamond_Square.svg.png "Christopher Ewin / CC BY-SA 4.0")

The time complexity of diamond-square is O(n) where n is the number of pixels in the map.

#### Dijkstra's algorithm
Depending on the type of queue structure used, Dijkstra's algorithm runs in time O(n^2) or O(e + n log n) where n is nodes and e is edges.

### Data structures

#### Min heap priority queue
Graph nodes are stored in a priority queue for ordering nodes by smallest cost when running Dijkstra's algorithm. The code is based on the MaxPQ class described in Sedgewick & Wayne: *Algorithms*.

| Operation | Time     |
|-----------|----------|
| poll()    | O(log n) |
| add()     | O(log n) |
| isEmpty() | O(1)     |

#### Integer set
A set structure for integers. Used in recording and retrieving node neighbors for Dijkstra's algorithm. 

| Operation | Time     |
|-----------|----------|
| add()     | O(n)     |
| contains()| O(n)     |
| getSet()  | O(n)     |


#### Random
A pseudorandom number generator based on the Mersenne Twister algorithm.

### Exceptions

#### Math.log
Rewriting this function was beyond the scope of this project. It is mainly used for convenience in getting a right size square grid from the UI canvas for the diamond-square algorithm. For reference, the logarithm function used by Java can be found here: https://github.com/openjdk-mirror/jdk/blob/jdk8u/jdk8u/master/src/share/native/java/lang/fdlibm/src/e_log.c


### Sources
- https://en.wikipedia.org/wiki/Perlin_noise
- https://en.wikipedia.org/wiki/Diamond-square_algorithm
- https://longwelwind.net/2017/02/09/perlin-noise.html
- https://learn.64bitdragon.com/articles/computer-science/procedural-generation/the-diamond-square-algorithm
- https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
- https://en.wikipedia.org/wiki/Mersenne_Twister#Pseudocode
- http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/VERSIONS/C-LANG/mt19937-64.c
- Sedgewick & Wayne: *Algorithms* 4th edition 
