import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;

// java -Djava.security.policy=policy AirportServer port

public class AirportServer {
	private static final int defaultport = 1099;
	public static void main(String args[]) {
		/*
		   if (args.length != 1) {
		   System.err.println("usage: java AirportServer rmi_port");
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
			String url = "//localhost:" + port + "/Airports";
			System.out.println("binding " + url);
			Naming.rebind(url, new Airports());
			System.out.println("server " + url + " is running...");
		}
		catch (Exception e) {
			System.out.println("Airport server failed:" + e.getMessage());
		}
	}
}
