import java.rmi.Naming;
import java.rmi.RemoteException;
import java.io.PrintStream;
import java.lang.String;
import java.rmi.ConnectException;
import PlaceData.PlaceDataProto.Place;
import PlaceData.PlaceDataProto.PlaceList;
import AirportData.AirportDataProto.Airport;
import AirportData.AirportDataProto.AirportList;

class Client
{
	private static final String defaulthost = "localhost";
	private static final int defaultport = 1099;
	private static final int nearestAirports = 5;

	public static void main(String[] args) throws Exception
	{
		try {
			if(args.length < 2)
			{
				System.err.println("usage: java Client [ -h rmiregistryserver ] [ -p port ] city state");
				System.exit(1);
			}
			String servername = setServerName(args);
			int serverport = setPort(args);
			String city = args[args.length - 2];
			String state = args[args.length - 1];

			String placesURL = "//" + servername + ":" + serverport + "/Places";
			String airportsURL = "//" + servername + ":" + serverport + "/Airports";

			// Lookup place
			PlacesInterface placesRemote;
			Place place = null;
			try {
				placesRemote = (PlacesInterface)Naming.lookup(placesURL);
				place = placesRemote.lookup(city, state);
			} catch (ConnectException e) {
				System.err.println("Could not connect to place server");
				System.exit(1);
			}
			if(place == null)
			{
				System.out.println("Place not found");
				System.exit(0);
			}

			System.out.println(place.getName() + ", " + place.getState() + ": " + place.getLat() + ", " + place.getLon());

			// Find airports
			AirportsInterface airportsRemote;
			double[] distances = null;
			Airport[] airportlist = null;
			try {
				airportsRemote = (AirportsInterface)Naming.lookup(airportsURL);
				airportlist = new Airport[nearestAirports];
				airportlist = airportsRemote.find_airports(nearestAirports, place.getLat(), place.getLon());
				distances = airportsRemote.getDistances();
			} catch (ConnectException e) {
				System.err.println("Could not connect to airport server");
				System.exit(1);
			}

			for(int i = 0; i < nearestAirports; i++)
			{
				System.out.print("code=");
				System.out.print(airportlist[i].getCode());
				System.out.print(", name=");
				System.out.print(airportlist[i].getName());
				System.out.print(", state=");
				System.out.print(airportlist[i].getState());
				System.out.print(" distance: ");
				System.out.print(Math.round(distances[i] * 1.1507794));
				System.out.println(" miles");
			}
		} catch(Exception e) {
			System.out.println("Client exception: " + e);
		}
	}

	// "Stupid way" command line processing
	private static String setServerName(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].contentEquals("-h"))
				return args[i + 1];
		}
		return defaulthost;
	}

	private static int setPort(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].contentEquals("-p"))
				try {
					return Integer.parseInt(args[i + 1]);
				} catch(Exception e) {
					System.err.println("Port must be a number");
					System.exit(1);
				}
		}
		return defaultport;
	}

}
