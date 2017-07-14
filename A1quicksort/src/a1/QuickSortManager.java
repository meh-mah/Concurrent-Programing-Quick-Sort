
package a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is responsible to manage the sorting process by adding the tasks (using concept of bag of task),
 * removing the finished tasks, make the thread wait until all tasks done,
 * and notify to Wakes up a single thread that is waiting to shutdown the process, when sorting is done. 
 * @author Mehran
 */
public class QuickSortManager {

    int task;
    ExecutorService exe_serv;
    public QuickSortManager(int numberOfThreads)
    {
	task = 0;
        
        /**
         * Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded queue.
         * At any point, at most numberOfThreads threads will be active processing tasks.
         * If additional tasks are submitted when all threads are active, they will wait in the queue until a thread is available.
         */
	exe_serv = Executors.newFixedThreadPool(numberOfThreads);
    }
    
    /**
     * this method will add new task that must be done by a thread. It specifies the part of the array in which the thread is responsible for sorting
     * @param array defines the original array
     * @param start defines the first cell of subarray
     * @param end defines the end cell of subarray
     */
    public synchronized void newTask( int array[], int start, int end )
    {
	// add to task list
        task ++;
        //start a quicksorting on spesified subarray
	Runnable new_task = new QuickSort( array,start,end,this );
	exe_serv.execute(new_task);
    }
    
    /**
     * this method is called when the task of sorting part of the array is done by the thread
     * if no task left, it Wakes up ( notify()) a single thread that is waiting to shutdown the process.
     */
    public synchronized void done()
    {
	task -- ;
	if( task <= 0 )
	    notify();
    }
    
    /*
     * Causes the thread to wait until another thread invokes the Object.notify, when all tasks of sorting is done.
     * so it shutdown the process
     */
    public synchronized void await()
	throws java.lang.InterruptedException
    {
	while( task > 0 )
	{
	    wait();
	}
	exe_serv.shutdown();
    }
}
