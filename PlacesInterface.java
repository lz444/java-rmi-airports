import java.rmi.Remote;
import java.rmi.RemoteException;
import PlaceData.PlaceDataProto.Place;

public interface PlacesInterface extends Remote
{
	public Place lookup(String name, String state) throws RemoteException;
}
