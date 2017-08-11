package rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Common extends Remote {
	String printMsg() throws RemoteException;

}
