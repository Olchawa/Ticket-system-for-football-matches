package rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import rmi.common.*;

public class ServantRemoteImpl extends UnicastRemoteObject implements Common {

	private static final long serialVersionUID = 1L;

	public ServantRemoteImpl() throws RemoteException {
	}


	@Override
	public void addEvent() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatEvent() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void SignUp() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void LogIn() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean verifyUser() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showUserBookedEvents() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void bookTheEvents() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelReservation() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String showEvents() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
