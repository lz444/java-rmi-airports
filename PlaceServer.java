import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;

// java -Djava.security.policy=policy PlaceServer port

public class PlaceServer {
	final private static int defaultport = 1099;
	public static void main(String args[]) {
		/*
		   if (args.length != 1) {
		   System.err.println("usage: java PlaceServer rmi_port");
		   System.exit(1);
		   }
		   */
		// Create and install a security manager
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		try {
			// first command-line argument is the port of the rmiregistry
			int port;
			if(args.length == 1)
				port = Integer.parseInt(args[0]);
			else
				port = defaultport;
			String url = "//localhost:" + port + "/Places";
			System.out.println("binding " + url);
			Naming.rebind(url, new Places());
			System.out.println("server " + url + " is running...");
		}
		catch (Exception e) {
			System.out.println("Place server failed:" + e.getMessage());
		}
	}
}
