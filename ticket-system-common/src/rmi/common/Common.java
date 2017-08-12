package rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Common extends Remote {
	
	// admin actions
	void addEvent()throws RemoteException;	
	void updatEvent()throws RemoteException;	
	
	// client actions
	void SignUp()throws RemoteException;	
	void LogIn()throws RemoteException;
	boolean verifyUser() throws RemoteException; 
	void showUserBookedEvents()throws RemoteException; 
	void bookTheEvents()throws RemoteException;
	void cancelReservation()throws RemoteException; 
	
	// both
	String showEvents()throws RemoteException; 
	

}
