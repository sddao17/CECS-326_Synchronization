

CECS-326 Project 2 - Synchronization


-----------------------------------------------
Authors
-----------------------------------------------


* Carlos Verduzco  (018718282)

* Steven Dao  (017503055)


-----------------------------------------------
Usage notes
-----------------------------------------------


* Purpose: To solve the Dining Philosophers problem, where Philosophers cycle between various stages of thinking / hunger / eating. When in a hungry state, they alternate picking up / putting down utensils respective to their neighbors, and after they are done eating, they resume thinking and repeat the cycle.

* Supported by Java-based applications.

* JDK version 9.0+ recommended.

* No installations required.


-----------------------------------------------
Compile / Run Instructions
-----------------------------------------------


------------------------
For Java-supported IDEs:
------------------------


* Load the source folder containing the `Diner.java`, `DiningPhilosophers.java`, `DiningService.java`, and `Philosopher.java` files into the IDE of your choice.

* (Optional) Customize the number of philosophers / thread-sleeping intervals by editing the global variables found in `DiningPhilosophers.java` file.

* Compile and then run the `DiningPhilosophers.java` file. You should see the Philosophers cycling through the different states and actions.


--------------------------------
For Command Prompts / Terminals:
--------------------------------


* On the command prompt / terminal, change the current directory to the path of the source folder containing the files `Diner.java`, `DiningPhilosophers.java`, `DiningService.java`, and `Philosopher.java`. You can do this with the command `cd <insert path here>`. The path should end with a pattern similar to the following : `...\src`.

* Compile the `DiningPhilosophers.java` file with the command: `javac DiningPhilosophers.java`.

* Run the `DiningPhilosophers.java` file with the command: `java DiningPhilosophers.java`. You should see the Philosophers cycling through the different states and actions.

