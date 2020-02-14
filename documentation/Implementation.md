### Implementation details
The application draws two-dimensional arrays, or heightmaps, on a JavaFX canvas where each array value is a pixel that is assigned an appropriate color depending on display mode. 

There are two algorithms implemented for the generation of maps, Perlin noise and diamond-square. Both are so-called noise functions and produce a 2D array of double values.

There is also a class for adding rivers to a heightmap. This works by using Dijkstra's algorithm to find shortest paths in a graph created from the heightmap and adjusting the original's values on the river's path.

#### Perlin noise
Perlin noise has a time complexity of O(2^n) where n equals number of dimensions, with significant constant factors due to the application of math functions and several layers of noise. In practice, the time complexity should be O(n) for this project.

#### Diamond-square algorithm
The time complexity of diamond-square is O(n) where n is the number of pixels in the map.

#### Dijkstra's algorithm
Depending on the type of queue structure used, Dijkstra's algorithm runs in time O(n^2) or O(e + n log n) where n is nodes and e is edges.

#### Sources
- https://en.wikipedia.org/wiki/Perlin_noise
- https://en.wikipedia.org/wiki/Diamond-square_algorithm
- https://longwelwind.net/2017/02/09/perlin-noise.html
- https://learn.64bitdragon.com/articles/computer-science/procedural-generation/the-diamond-square-algorithm
- https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
