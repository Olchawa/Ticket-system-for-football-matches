package rmi.user;

import java.rmi.RemoteException;
import java.util.List;
import rmi.common.Event;

public interface UserInterface {
	String separator = "\n=====================================";

	void printEventDetails(List<Event> list);
	
    void showEvents()throws RemoteException ;

}
