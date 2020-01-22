### Project definition

A Java application for the procedural generation of three-dimensional terrain maps that might be used in a game environment, especially as an overworld map or as the setting of a strategy game.

The starting point is the implementation of one or several algorithms for the generation of height maps, which may then be further processed and rendered for display as terrain maps. The user should be able to enter parameters such as height of sea level that affect the end result. 

Visualization and UI will be realized in a terminal view on roguelike-style ASCII maps and/or a canvas view. Data structures will be implemented as necessary during the course of implementing algorithms.

The first algorithm to be implemented will be Perlin noise, other algorithms and features will be added during the project as time permits. Below are listed some options.

Noise functions:
- Perlin noise
- Simplex noise
- Value noise
- Diamond-square

Automata:
- Langton's ant
- Other "random walkers"

In addition to implementing the algorithms, additional features and tweaks may be added for more varied maps:
- River generation
- Climate types
- Distinct biomes
- Cities, roads...

Sources:
- https://en.wikipedia.org/wiki/Perlin_noise
- https://en.wikipedia.org/wiki/Langton%27s_ant