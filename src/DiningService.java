
/**
 * @author Carlos Verduzco, Steven Dao
 * @version 1.0
 *
 * Date: October 14, 2021
 * Purpose: This interface declares the dining methods called by the dining philosophers.
 */
public interface DiningService {

   /**
    * Called by a philosopher when they wish to eat.
    *
    * @param philosopherID the index identifier of the Philosopher
    */
   void takeForks(int philosopherID) throws InterruptedException;

   /**
    * Called by a philosopher when they are finished eating.
    *
    * @param philosopherID the index identifier of the Philosopher
    */
   void returnForks(int philosopherID) throws InterruptedException;
}
