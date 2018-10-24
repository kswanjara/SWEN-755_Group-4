# Assignment 2

## Assigment Details
In this assigment, we have implemented active redundancy into our critical process of sending (randomly generated) GPS coordinates to a server.  We maintain a primary and secondary critical process that both send a heartbeat message to a receiver class.  If the Receiver detects that the heartbeat of the primary component has stopped sending, it notifies the new third component (ProcessManager) to switch to the backup process.

## Redundancy Design
![Redundancy Diagram](http://andydistasi.com/dev/755Models/755_ActiveRedundancy.png)
