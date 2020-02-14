### Testing


#### Noise functions
Tests for the Perlin noise and diamond-square classes consist of confirming that the produced arrays only contain values in the accepted range of -1.0 to 1.0.

#### River generation
Graph integrity is tested by verifying that neighbors and node numbers are correctly formed on the basis of array positions. Dijkstra's algorithm is tested by giving it a hardcoded array and confirming shortest paths.