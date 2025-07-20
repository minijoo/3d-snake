# 3d-snake

<img width="202" height="265" alt="Screen Shot 2025-07-19 at 8 01 08 PM" src="https://github.com/user-attachments/assets/744b576b-c8de-445a-b11d-c711b21dfa68" />

For-fun Snake game with a twist programmed in Java.

This version of Snake is similar to the first one that Nokia put on their phones. Specifically, walls are out, and eating a snack increases the speed of your snake in addition to making it longer.

Whereas in the Nokia version moving into the snake's body was considered illegal, in this version the snake will move over its own body, hence the "3D". Snacks can appear above ground level, too! To reach such a snack, the snake must double up its body on that cell. 

Finally, Nokia's snake had a little quirck where there was a slight pause when you hit the wall that acted as a cushion before the player's potential demise. It helped with survivability when the game got to high speeds where running into a wall was very common. That feature (or bug?) is also a part of this game. 

# Run
```
javac -d classes *.java
java -cp classes Snake
```

# Notes
- To keep the speeding up of the game non-linear, i.e. faster in the beginning and slower near the end, I asked Google Gemini for an equation that does just that, with a starting value (y-intercept) and an asymptotic value as x goes to infinity. Its answer was $y = A + B \cdot 2.0 / (1.0 + e^{0.2 \cdot (x + 1)})$, where $A$ is the asymptotic value and $B$ is the y-intercept. This can be found in the `getNextInterval(int x)` function.
- New snacks are generated at random and without collision by leveraging the TreeSet data structure for keeping track of all blank spaces. By using TreeSet's `floor` and `ceiling` functions, even if the randomly generated number is not in the TreeSet, we can quickly take the first adjacent one available. TreeSet behind-the-scenes uses a self-balancing BST to gaurantee O(lgN) searches, adds, and removals. See `newSnack()` function.
