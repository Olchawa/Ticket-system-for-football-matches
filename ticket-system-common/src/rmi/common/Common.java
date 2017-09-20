package rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Common extends Remote {

	// administrator actions
	void addEvent(Event event) throws RemoteException;

	void updatEvent(Event oldEvent, Event updateEvent) throws RemoteException;

	// client actions
	void SignUp(User newAccount) throws RemoteException;

	User LogIn(String email, String password) throws RemoteException;

	void buyOrReturn(String userNick, int tickets, String eventKey, String action) throws RemoteException;

	boolean checkIfEmailExist(String email) throws RemoteException;

	// general actions
	void saveOnServer(Object object) throws RemoteException;

	List<Event> getEvents() throws RemoteException;

	List<Event> sortEvents(String sortType) throws RemoteException;

	void LogMessage(String userName) throws RemoteException;

	int getEventsNumber() throws RemoteException;

}
