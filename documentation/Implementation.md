### Implementation details
The application draws 2D arrays, or heightmaps, on a JavaFX canvas where each array value is a pixel that is assigned an appropriate color depending on display mode. 

So far there are two algorithms implemented, Perlin noise and diamond-square. Both produce a 2D array of double values.

#### Perlin noise
Perlin noise has a time complexity of O(2^n) where n equals number of dimensions, with significant constant factors due to the application of math functions and several layers of noise. In practice, the time complexity should be O(n) for this project.

#### Diamond-square algorithm
The time complexity of diamond-square is O(n).

#### Sources
- https://en.wikipedia.org/wiki/Perlin_noise
- https://en.wikipedia.org/wiki/Diamond-square_algorithm
- https://longwelwind.net/2017/02/09/perlin-noise.html
- https://learn.64bitdragon.com/articles/computer-science/procedural-generation/the-diamond-square-algorithm
