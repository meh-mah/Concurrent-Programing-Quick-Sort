*Short description of the applications:
The program is implementing the parallel recursive quick sort algorithm using java multithread. The QuickSort class implements the Runnable interface. The aim is to partition the array so each thread is responsible to sort part of the array in parallel. The number of threads used by the program is depended on the number of available threads in the CPU. Therefore, the program can adapt itself automatically to different running platforms. For example, if only four hardware threads is available increasing the number of threads in the program will not change the speed of execution, it just cause large thread queue, which can waste the memory resources.
 
*Description of command-line parameters: 
The array size is 100 by default, also can be defined from cmd before execution.

*Instructions to build and to run the application:
Javac QuickSort.java     // to compile
Java QuickSort <array size> //to run

*Performance:
intel(r) core(tm) i3 cpu 540 with four threads:
size		time
10		1ms
100		2ms
1000		9ms
10000		125ms
100000	159ms
