import AirportData.AirportDataProto.Airport;
import AirportData.AirportDataProto.AirportList;
import java.io.PrintStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.String;
import java.lang.Math;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.*;

class Airports extends UnicastRemoteObject implements AirportsInterface
{
	private AirportList.Builder airportlist;
	private String path = "airports-proto.bin";
	private double[] distances;
	private Airport[] closestAirports;

	public Airports() throws RemoteException
	{
		// Read airport datafile
		airportlist = AirportList.newBuilder();
		try {
			airportlist.mergeFrom(new FileInputStream(path));
		} catch (IOException e) {
			System.out.println(path + ": file not found");
			System.exit(-1);
		}
	}

	public Airport[] find_airports(int num, double lat, double lon) throws RemoteException
	{
		distances = new double[num];
		closestAirports = new Airport[num];

		// Search through airport datafile
		for(Airport airport: airportlist.getAirportList())
		{
			// Compute distance
			double lat1 = Math.toRadians(airport.getLat());
			double lat2 = Math.toRadians(lat);
			double lon1 = Math.toRadians(airport.getLon());
			double lon2 = Math.toRadians(lon);
			double dist = Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2 - lon1));
			dist = 60 * Math.toDegrees(dist);

			// See if distance is closer than currently found closest airports
			int i;
			for(i = 0; (i < num) && (dist > distances[i]); i++)
			{
				// Special case: distances[i] is zero means we're still looking at the beginning of the airportlist
				if(distances[i] == 0.0) break;
			}

			if(i < num)
			{
				// Found a new airport
				// First shift the other elements downward
				for(int j = num - 1; j > i ; j--)
				{
					closestAirports[j] = closestAirports[j - 1];
					distances[j] = distances[j - 1];
				}

				// Now add the new airport to the arrays
				closestAirports[i] = airport;
				distances[i] = dist;
			}
		}

		return closestAirports;
	}

	public double[] getDistances() throws RemoteException
	{
		return distances;
	}
}
