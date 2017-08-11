package rmi.admin;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import rmi.common.*;

public class Admin implements Runnable {

	Common remoteObject;

	public static void main(String[] args) throws IndexOutOfBoundsException {

		new Thread(new Admin()).start();

	}
	
	public void run() {
		Registry reg;
		try {
			
			reg = LocateRegistry.getRegistry("localhost");
			// Looking up the registry for the remote object
			remoteObject = (Common) reg.lookup("Server");
			// Calling the remote method using the obtained object
			System.out.println("Admin: " + remoteObject.printMsg());
			
			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}


}
