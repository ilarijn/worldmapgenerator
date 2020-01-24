### Week 2

#### Progress during this week
Roughly half the time this week was spent setting up project structure, Gradle and a UI skeleton along with checkstyle and test configurations. The rest of the time went into trying out Perlin noise and getting something useful out of it. The algorithm I now have returns a table of grayscale RGB values that can be rendered on the UI canvas. I did not have the time to get into proper map generation yet, but that should be just a matter of fiddling around with threshold values and setting the appropriate colors. Altogether I'm happy with the progress as I didn't expect to get this far yet, there's plenty of time to add more features.

#### Problems
No real problems at this point, although I suspect at some point my understanding of the math involved will become the biggest hurdle.

#### Questions
I haven't used checkstyle before, what's an appropriate configuration for this type of project? Right now I just have the Sun-style check.

#### Next week
See what the current noise looks like rendered into a map (probably not that great). The common technique for map generation seems to be using "octaved" noise, try that out. 