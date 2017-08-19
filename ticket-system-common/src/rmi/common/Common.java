package rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Common extends Remote {
	
	// administrator actions
	void addEvent(Event event)throws RemoteException;	
	void updatEvent(Event event, int index)throws RemoteException;	
	
	// client actions
	
	// logging actions
	void SignUp(User newAccount)throws RemoteException;
	User LogIn(String email, String password)throws RemoteException;
	
	// booking actions
	void bookTheEvent(String userNick, int eventId, int ticketBooked)throws RemoteException;
	void cancelReservation(String userNick,int ticketToReturn, String eventKey)throws RemoteException; 
	
	// general actions
	void saveOnServer(Object object) throws RemoteException;
	String showEvents(int userTypeFlag)throws RemoteException;
	Event getEvent(int indexOfEvent)throws RemoteException;
	void LogMessage(String userName)throws RemoteException;
	int getEventsNumber()throws RemoteException;
	
}
