
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Carlos Verduzco, Steven Dao
 * @version 1.0
 *
 * Date: October 14, 2021
 * Purpose: This class starts the Dining Philosophers problem, where Philosophers cycle between various stages of
 *          thinking / hunger / eating. When in a hungry state, they alternate picking up / putting down utensils
 *          respective to their neighbors, and after they are done eating, they resume thinking and repeat the cycle.
 *          - ReentrantLocks are used to represent the fork objects.
 *          - See the `Diner` class to see the solution implementation for handling deadlock.
 */
public class DiningPhilosophers {

    // global variables
    // total number of philosophers
    public static int numOfPhilosophers = 5;
    // lower bound for sleeping threads (in seconds)
    public static int sleepingMin = 1;
    // upper bound for sleeping threads (in seconds)
    public static int sleepingMax = 3;

    /**
     * Executes the application.
     *
     * @param args the command-line arguments to the application
     */
    public static void main(String[] args) {

        // store the Philosophers
        ArrayList<Philosopher> philosophers = new ArrayList<>();
        // store the Locks, which will be our fork objects
        ArrayList<ReentrantLock> forks = new ArrayList<>();

        // add one fork per Philosopher
        for (int i = 0; i < numOfPhilosophers; ++i) {
            forks.add(new ReentrantLock());
        }

        // as represented in the Dining Philosophers problem, model each fork object to be on a side of a Philosopher
        for (int i = 0; i < numOfPhilosophers; ++i) {
            // each left fork will be on the same index as the philosopher
            ReentrantLock leftFork = forks.get(i);
            // each right fork will be on the next index of the philosopher
            ReentrantLock rightFork = forks.get((i + 1) % numOfPhilosophers);

            // create an instance of a Philosopher using the specified fork locks
            philosophers.add(new Philosopher(leftFork, rightFork));
        }

        // once all Philosophers are created and the lists are appropriately filled, begin each synchronized thread
        for (int i = 0; i < numOfPhilosophers; ++i) {
            // create a new Thread for each philosopher, and name it starting from 1 instead of 0 for console readability
            Thread currentThread = new Thread(philosophers.get(i), "Philosopher " + (i + 1));

            // start each Thread immediately after creating it
            currentThread.start();
        }
    }
}
