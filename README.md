# SWEN-755_Group-4

## Group Members
* Andrew DiStasi
* Jan Guillermo
* Jeff Palmerino
* Kunal Shirish Wanjara 


## Class Diagram
![Class Diagram](http://andydistasi.com/dev/755Models/Heartbeat%20Class%20Diagram%20-%20Version%202.png)


## Instructions to Run
1. Download and compile the code
2. Launch the Server - HeartbeatReceiver.java
3. Launch the Client - VehicleApplication.java
4. At some point, the critical process (located in the client) will break, ending the VehicleApplication process.  The Server will continue to run and function properly.  You can run the client again to see the connection is established until the client fails again.


## Critical Process Details
Our critical process mimics the functionality of a car sending its GPS Coordinates to a server.  In our application, a pair of coordinates are randomly determined and sent to the server.  If the latitude is greater than 89.1 degrees or the longitude is less than 0.2 degrees, then the application will behave as though the critical process has failed.
