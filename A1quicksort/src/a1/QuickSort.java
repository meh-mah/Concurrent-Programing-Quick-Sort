package a1;

/*
 * this class implements parallel recursive quick sort algorithm with a task bag
 */

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

    
public class QuickSort implements Runnable {
    int array[];
    int start_cell;
    int end_cell;
    QuickSortManager qsManager;
    //the maximum number of threads is defined according to the available number of processors.
    private static final int maxNUmberOfThreads = Runtime.getRuntime().availableProcessors();
    // cunstroctor of the class to initialize class attributes.
    public QuickSort( int array[], int start_cell, int end_cell,QuickSortManager qsManager ) {
	this.array    = array ;
	this.start_cell    = start_cell ;
	this.end_cell       = end_cell ;
	this.qsManager = qsManager ;
    }
    
    // overrides the run method of Runnable interface. It start an executing thread to perform one of the tasks in quick sorting.
    @Override
    public void run() 
    {      
        //performs quick sort
        quickSort(start_cell, end_cell);
        /**
         * notify the manager that the task is done. so manager can decide to shut down, if no other task left.
         * @ see comment in done() in QuickSortManager class
         */
	qsManager.done();
    }
    
    /*
     * it is responsible to check the values against the pivot value as discribed in the assignment description
     * the method will return the last value that start was taken. 
     * So we can define the subarrays for continuing the sorting process according to that value
     */
    int partition(int start, int end) {
        // temporary variable for swapping
        int tmp;
        //value of the middle cell in the array as pivot value
        int pivot = array[(start + end) / 2];
        // while not all cell have been checked yet
        while (start <= end) {
            // if the value on the right is smaller than pivot just move to next cell
            while (array[start] < pivot)
                start++;
            // if the value on the left is greater than pivot value just move to previous cell
            while (array[end] > pivot)
                end--;
            // otherwise swap the left and right values. and move to next cell
                if (start <= end) {
                    tmp = array[start];
                    array[start] = array[end];
                    array[end] = tmp;
                    start++;
                    end--;
                }
        };
        return start;
    }
    
    // recursive quick sort
    void quickSort(int left, int right) {
        int index = partition(left, right);
        /**
         * the value returned by the partition is used to divide the array into two subarrays.
         * each subarray will be sorted by a thread
         */
        if (left < index - 1)
            qsManager.newTask( array,left, index-1);
        if (index < right)
            qsManager.newTask( array,index, right);
    }
    
    public static void main(String[] args) {

        //default array size
        int  NO_OF_ELEMENT = 100000;
        
        //get the array size from the command if any
        int l= args.length;
        if(l>0)
        {
            NO_OF_ELEMENT=Integer.parseInt(args[0]);
        }
        
        //random number generator to fill the array cells with random numbers
        Random random = new Random();
        
        int [] sortArray = new int [NO_OF_ELEMENT ];
        for (int i=0; i< NO_OF_ELEMENT ;i++){
            sortArray[i] = random.nextInt(NO_OF_ELEMENT );
        }
        
        // print the array befor sorting
        System.out.println("the original array is:");
        for (int i=0; i<sortArray.length; i++){
            System.out.print(sortArray[i]+", ");
        }
        System.out.println();
        
        //new manager to mange the threads task
        QuickSortManager manager=new QuickSortManager(maxNUmberOfThreads);
        
        long startTime = System.currentTimeMillis();
        // submit the array to be manager to be sorted by new tread from the thread pool
        manager.newTask(sortArray, 0, sortArray.length-1);
        
        // Causes the current thread to wait until all tasks of sorting is done
        try {
            manager.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(QuickSort.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //print time taken to perform sorting
        System.out.println("Time taken " + (System.currentTimeMillis() - startTime)+ "ms");
        //print number of threads
        System.out.println("number of threads: " +maxNUmberOfThreads);
        //print the sorted array
        System.out.println("sorted array is: ");
        for (int i=0; i<sortArray.length; i++){
            System.out.print(sortArray[i]+", ");
        }
    }
}

 


