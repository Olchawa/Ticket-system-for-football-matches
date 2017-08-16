package rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Common extends Remote {
	
	// admin actions
	void addEvent(Event event)throws RemoteException;	
	void updatEvent()throws RemoteException;	
	
	// client actions
	void SignUp()throws RemoteException;	
	void LogIn()throws RemoteException;
	boolean verifyUser() throws RemoteException; 
	void showUserBookedEvents()throws RemoteException; 
	void bookTheEvents()throws RemoteException;
	void cancelReservation()throws RemoteException; 
	
	// both
	ArrayList<Event> getEvents()throws RemoteException;
	
	

}
