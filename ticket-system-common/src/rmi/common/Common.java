package rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Common extends Remote {

	// administrator actions
	void addEvent(Event event) throws RemoteException;

	void updatEvent(Event oldEvent, Event updateEvent) throws RemoteException;

	// client actions
	void SignUp(User newAccount) throws RemoteException;

	User LogIn(String email, String password) throws RemoteException;

	void buyOrReturn(String userNick, int tickets, String eventKey, String action) throws RemoteException;

	// general actions
	void saveOnServer(Object object) throws RemoteException;

	String showEvents(int userTypeFlag) throws RemoteException;

	Event getEvent(int indexOfEvent) throws RemoteException;

	void LogMessage(String userName) throws RemoteException;

	int getEventsNumber() throws RemoteException;

}
