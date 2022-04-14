
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Carlos Verduzco, Steven Dao
 * @version 1.0
 *
 * Date: October 14, 2021
 * Purpose: This class represents each Philosopher thread. Philosophers alternate between eating and thinking.
 *          - ReentrantLocks are used to represent the fork objects.
 *          - Condition variables are used to await and signal certain threads depending on the states of directly
 *              adjacent Philosophers.
 */
public class Philosopher implements Runnable {

    boolean isActive;
    // the representation of the forks on each the side of the Philosopher
    private final ReentrantLock leftFork;
    private final ReentrantLock rightFork;
    // this philosopher's current state
    private states currentState;

    // global variables
    // the global list of enumerated types for different Philosopher states
    public enum states {
        THINKING,
        HUNGRY,
        EATING
    }
    // the global list of condition variables needed to await or signal specific threads
    public static final ArrayList<Condition> forksAvailable = new ArrayList<>();
    // the global list of all Philosophers, so we can identify their relative positions
    public static final ArrayList<Philosopher> philosophers = new ArrayList<>();


    /**
     * Constructs a new Philosopher instance, initialized to a thinking state.
     *
     * @param newLeftFork the lock representing the Philosopher's left fork
     * @param newRightFork the lock representing the Philosopher's right fork
     */
    public Philosopher(ReentrantLock newLeftFork, ReentrantLock newRightFork) {

        this.isActive = true;
        leftFork = newLeftFork;
        rightFork = newRightFork;
        // Philosophers start in a thinking state
        currentState = states.THINKING;
        // we'll set our condition variables to monitor each Philosopher's left fork
        forksAvailable.add(leftFork.newCondition());
        philosophers.add(this);
    }

    /**
     * Returns the Philosopher's left lock, representing its left fork.
     *
     * @return the Philosopher's left lock instance
     */
    public ReentrantLock getLeftFork() {

        return leftFork;
    }

    /**
     * Returns the Philosopher's right lock, representing its right fork.
     *
     * @return the Philosopher's right lock instance
     */
    public ReentrantLock getRightFork() {

        return rightFork;
    }

    /**
     * Returns the current state of the Philosopher.
     *
     * @return the current state of the Philosopher
     */
    public states getCurrentState() {

        return currentState;
    }

    /**
     * Sets the current state of the Philosopher.
     *
     * @param newState the provided Philosopher state
     */
    public void setCurrentState(states newState) {

        currentState = newState;
    }

    /**
     * Executes the thread by simulating a Philosopher cycling through thinking / hungry / eating states.
     */
    @Override
    public void run() {

        // create the Philosopher Diner environment
        Diner ourDiner = new Diner();
        // get the integer range for sleeping threads
        int sleepingMin = DiningPhilosophers.sleepingMin;
        int sleepingMax = DiningPhilosophers.sleepingMax;
        // store the thread name within a variable for readability
        String threadName = Thread.currentThread().getName();
        // store the index identifier (the integer after the space) of the Philosopher
        // initialized within the Main method
        String idAsString = threadName.split(" ")[1];
        int philosopherID = Integer.parseInt(threadName.substring(threadName.indexOf(idAsString.charAt(0)))) - 1;

        // Philosophers begin in a thinking state
        System.out.println(Thread.currentThread().getName() + ": Currently thinking");

        // catch InterruptedExceptions
        try {
            // exit the cycle when an exception is thrown
            while (this.isActive) {
                // currently thinking; have the thread sleep for a random period between the min and max (in seconds)
                Thread.sleep((int) ((Math.random() * sleepingMax) + sleepingMin) * 1_000);

                // done thinking; now hungry
                setCurrentState(states.HUNGRY);
                // attempt to lock both left/right locks to begin eating
                ourDiner.takeForks(philosopherID);
                // wait for both left/right threads to be signaled ...

                // both forks have been acquired; begin eating
                setCurrentState(states.EATING);
                // currently eating; have the thread sleep for a random period between the min and max (in seconds)
                Thread.sleep((int) ((Math.random() * sleepingMax) + sleepingMin) * 1_000);

                // done eating; back to thinking
                setCurrentState(states.THINKING);
                // signal and unlock both left/right threads
                ourDiner.returnForks(philosopherID);
                // repeat the cycle
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            this.isActive = false;
        }
    }
}
