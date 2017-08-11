package rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import rmi.common.*;

public class Client implements Runnable {

	Common remoteObject;

	public static void main(String[] args) throws IndexOutOfBoundsException {

		new Thread(new Client()).start();

	}
	
	public void run() {
		Registry reg;
		try {
			
			reg = LocateRegistry.getRegistry("localhost");
			// Looking up the registry for the remote object
			remoteObject = (Common) reg.lookup("Server");
			// Calling the remote method using the obtained object
			System.out.println("Client: "+remoteObject.printMsg());
			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}


}
