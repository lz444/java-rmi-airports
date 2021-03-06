# java-rmi-airports

Three simple programs consisting of an AirportServer, PlaceServer, and Client.
The client connects to the PlaceServer, gives it a city and state, then the
PlaceServer returns a lat&lon coordinate. The client then gives the coordinate
to the AirportServer which returns a list of nearby airports.

Written in Java, using RMI.

Compilation & Usage
To compile, first make sure you have Google Protcol Buffers in your CLASSPATH,
then use javac on the various java files:
	javac AirportData/AirportDataProto.java
	javac PlaceData/PlaceDataProto.java
	javac AirportsInterface.java
	javac AirportServer.java
	javac Airports.java
	javac PlacesInterface.java
	javac PlaceServer.java
	javac Places.java
	javac Client.java
To run, open four terminal windows/tabs, and run the following in each
window/tab: (make sure that all classes are in the CLASSPATH)
	rmiregistry
	java -Djava.security.policy=policy AirportServer port
	java -Djava.security.policy=policy PlaceServer port
	java Client [ -h rmiregistryserver ] [ -p port ] city state
port is optional for AirportServer & PlaceServer. If omitted, the servers will
default to port 1099. Similarily port & rmiregistryserver is optional for
Client. If omitted Client will default to localhost:1099.
