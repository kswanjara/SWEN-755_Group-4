# Assignment 3

## Instructions to Run
1. Download and Compile the code.
2. Add the jar files in the lib folder to the classpath.
3. Run the VehicleApplication.java class

## Downloading the Jar outside of our project (if the above doesn't work)
1. Navigate to [this website](https://funofprograming.wordpress.com/2016/10/08/priorityexecutorservice-for-java/).  Download the PriorityExecutorService.zip file, which contains several jar files used in our project.
2. Extract this file into your downloads folder (you can leave it there).  
3. If you are still unsure about the jar files, view the Assignment3.iml file.  This will display the expected path/location of the jar files.

## Critical Process Details
We implemented two critical processes:

1. Generate all prime numbers between 1 and 100,000
2. A Random Coordinate Generator

These processes are sent to a PriorityExecutorService that assigns these tasks to a pool of 10 threads.  The Prime Number generation is assigned a priority of 7, while the Random Coordinate Generation is assigned a priority of 5.
