
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;

/**
 * @author Carlos Verduzco, Steven Dao
 * @version 1.0
 *
 * Date: October 14, 2021
 * Purpose: This class implements the methods called by the dining philosophers.
 *          - ReentrantLocks are used to represent the fork objects.
 *          - Condition variables are used to await and signal certain threads depending on the states of directly
 *              adjacent Philosophers.
 *          - This program prevents deadlock by disallowing all left forks to be picked up at the same time.
 *              Preventing all Philosophers from having only one chopstick each solves the problem of circular wait.
 */
public class Diner implements DiningService {

    // store the global number of philosophers for readability
    private final int numOfPhilosophers = DiningPhilosophers.numOfPhilosophers;

    // store all global Philosopher instances as another class member for readability
    private final ArrayList<Philosopher> philosophers = Philosopher.philosophers;
    // store the condition variables as another class member for readability
    private final ArrayList<Condition> forksAvailable = Philosopher.forksAvailable;


    /**
     * Called by a philosopher when they wish to eat.
     *
     * @param philosopherID the index identifier of the Philosopher
     */
    @Override
    public void takeForks(int philosopherID) throws InterruptedException {

        // acquire the left lock before we alter its condition variable
        philosophers.get(philosopherID).getLeftFork().lock();

        // if the Philosopher to the left is eating, the left fork is not available
        if (philosophers.get((philosopherID + numOfPhilosophers - 1) % numOfPhilosophers).getCurrentState()
                == Philosopher.states.EATING) {
            // signal the left thread to sleep and wait
            forksAvailable.get(philosopherID).await();

            // the left Philosopher isn't eating, so the left fork should be available
        } else {
            // implementing solution to deadlock:
            // every time this Philosopher's left fork is signaled, we must verify that all other forks are not in use
            while (true) {
                // keep track of the number of left forks currently in use by other threads
                int numOfForksInUse = 0;

                // iterate through each of the philosopher's left forks
                for (Philosopher philosopher : philosophers)
                    // if the fork is locked, it's currently being used by another thread
                    if (philosopher.getLeftFork().isLocked())
                        ++numOfForksInUse;

                // check if all other forks are in use
                if (numOfForksInUse == numOfPhilosophers)
                    // prevent this last Philosopher from picking up the last available fork
                    // and repeat this loop when the fork is signaled again
                    forksAvailable.get(philosopherID).await();

                    // at least one other fork is available to grab; we can break out and allow this Philosopher to eat
                else
                    break;
            }

            // if the thread has reached this point, there are other forks available for other Philosophers
            // signal the left thread to wake up and continue
            forksAvailable.get(philosopherID).signal();
            System.out.println("Philosopher " + (philosopherID + 1) + ": Picked up left fork");
        }

        // acquire the right lock before we alter its condition variable
        philosophers.get(philosopherID).getRightFork().lock();

        // if the Philosopher to the right is eating, the right fork is not available
        if (philosophers.get((philosopherID + 1) % numOfPhilosophers).getCurrentState() == Philosopher.states.EATING) {
            // signal the right thread to sleep and wait
            forksAvailable.get((philosopherID + 1) % numOfPhilosophers).await();

            // the right Philosopher isn't eating, so the right fork should be available
        } else {
            // signal the right thread to wake up and continue
            forksAvailable.get((philosopherID + 1) % numOfPhilosophers).signal();
            System.out.println("Philosopher " + (philosopherID + 1) + ": Picked up right fork; now eating");
        }
    }

    /**
     * Called by a philosopher when they are finished eating.
     *
     * @param philosopherID the index identifier of the Philosopher
     */
    @Override
    public void returnForks(int philosopherID) throws InterruptedException {

        // wake up the left thread and let the user know the left fork is available
        forksAvailable.get(philosopherID).signal();
        System.out.println("Philosopher " + (philosopherID + 1) + ": Put down left fork");

        // unlock the left lock so other threads can access it
        philosophers.get(philosopherID).getLeftFork().unlock();

        // wake up the right thread and let the user know the right fork is available
        forksAvailable.get((philosopherID + 1) % numOfPhilosophers).signal();
        System.out.println("Philosopher " + (philosopherID + 1) + ": Put down right fork; back to thinking");

        // unlock the right lock so other threads can access it
        philosophers.get(philosopherID).getRightFork().unlock();
    }
}
