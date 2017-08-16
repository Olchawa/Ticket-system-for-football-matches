package rmi.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import rmi.common.Common;
import rmi.common.Event;

public class ServantRemoteImpl extends UnicastRemoteObject implements Common {

	// global variables
	private static final long serialVersionUID = 1L;
	final String dir = System.getProperty("user.dir");

	ArrayList<Event> eventsList = new ArrayList<Event>();

	public ServantRemoteImpl() throws RemoteException {
		(new LoadLIST()).start();
	}

	// administrator actions
	@Override
	public void addEvent(Event recievedEvent) throws RemoteException {
		eventsList.add(recievedEvent);
		String name = recievedEvent.toStringForFileName();
		serialize(recievedEvent, name);
	}

	@Override
	public void updatEvent() throws RemoteException {

	}

	@Override
	public void SignUp() throws RemoteException {

	}

	@Override
	public void LogIn() throws RemoteException {

	}

	@Override
	public boolean verifyUser() throws RemoteException {

		return false;
	}

	@Override
	public void showUserBookedEvents() throws RemoteException {

	}

	@Override
	public void bookTheEvents() throws RemoteException {

	}

	@Override
	public void cancelReservation() throws RemoteException {

	}

	// general actions
	@Override
	public ArrayList<Event> getEvents() throws RemoteException {
		return eventsList;
	}
	
	
	// class for loading lists
	class LoadLIST extends Thread {

		public void run() {
			try {
				laodFilesToLists();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		// load list of events
		private void laodFilesToLists() throws ClassNotFoundException {
			try {
				File[] files = new File(dir).listFiles();
				for (File file : files) {
					if (file.isFile() && (file.getName().endsWith(".afb"))) {
						Event readEvent = (Event) deserialize(file.getName());
						eventsList.add(readEvent);
					}
				}
				System.out.println("Event loaded successfully!!!");

			} catch (NullPointerException e) {
				System.out.print("NullPointerException caught - no files in directory");
			}

		}

	}

	// serialization and deserialization
	private static boolean serialize(Object obj, String path) {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
			System.out.println("\nSerialized data was saved in " + path);
			return true;
		} catch (IOException i) {
			i.printStackTrace();
			return false;
		}
	}

	private static Object deserialize(String path) throws ClassNotFoundException {
		Object obj = null;
		try {
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			obj = in.readObject();
			in.close();
			fileIn.close();
			return obj;
		} catch (IOException i) {
			i.printStackTrace();
			return obj;

		}
	}

}
