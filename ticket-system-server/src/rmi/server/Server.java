package rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	
	Registry reg; // rejestr nazw obiektow
	ServantRemoteImpl servant; // klasa us³ugowa
	
	
	public static void main(String[] args) {
		try {
			new Server();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	protected Server() throws RemoteException {
		try {
			reg = LocateRegistry.createRegistry(1099); 
			servant = new ServantRemoteImpl(); // creation of remote object
			reg.rebind("Server", servant); // binding
			System.out.println("Server READY");
		} catch (RemoteException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
