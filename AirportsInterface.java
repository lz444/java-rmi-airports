import java.rmi.Remote;
import java.rmi.RemoteException;
import AirportData.AirportDataProto.Airport;

public interface AirportsInterface extends Remote
{
	public Airport[] find_airports(int num, double lat, double lon) throws RemoteException;
	public double[] getDistances() throws RemoteException;
}
