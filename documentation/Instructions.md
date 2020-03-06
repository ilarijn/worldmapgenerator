Instructions for use
----

To use the application, either download and run the jar release found in this repository, or run `./gradlew run` at the root of a cloned version of this repository.

Pick an algorithm from the drop down menu and click `Generate`.

## Parameters

`Water level` controls the height at which water starts. Higher value means more water.

`Map seed` is the seed number based on which random values in the heightmap are generated. Using the same seed gives the same result each time.

`Terrain` gives a terrain representation of the map.

`Grayscale` gives a grayscale representation of the map.

`Rivers` controls the amount of rivers that will be generated on the map. River start and end points are random, and if a new river falls on the path of an existing one, it will make the existing river wider.



### Perlin noise

`Scale` may be thought of as a value representing the distance from which the noise is viewed. Higher value zooms in closer.

`Octaves` controls the amount of iterations of noise that will be added together. Higher value means more details/local variation.

`Amplitude` controls the variance of heightmap values. Higher value means more details/local variation.


### Diamond-square

Pick `Random` corner values or give custom ones.