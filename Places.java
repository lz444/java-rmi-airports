import PlaceData.PlaceDataProto.Place;
import PlaceData.PlaceDataProto.PlaceList;
import java.io.PrintStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.String;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.*;

// Places implements the remote interface
class Places extends UnicastRemoteObject implements PlacesInterface
{
	private PlaceList.Builder placelist;
	private String path = "places-proto.bin";

	public Places() throws RemoteException
	{
		// Read places datafile
		placelist = PlaceList.newBuilder();
		try {
			placelist.mergeFrom(new FileInputStream(path));
		} catch (IOException e) {
			System.err.println(path + ": file not found");
			System.exit(-1);
		}
	}

	public Place lookup(String name, String state) throws RemoteException
	{
		// Search through list for city, state
		for(Place place: placelist.getPlaceList())
		{
			if(place.getState().equalsIgnoreCase(state) && place.getName().regionMatches(true, 0, name, 0, name.length()))
				return place;
		}

		// No place found, return null
		return null;
	}
}
