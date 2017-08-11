package rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import rmi.common.*;



public class ServantRemoteImpl  extends UnicastRemoteObject implements Common {
	
	
	public ServantRemoteImpl() throws RemoteException {
	}
	
	@Override
	public String printMsg() throws RemoteException {
		return "This is an example RMI program";  
	}


}
