package rmi.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CopyOnWriteArrayList;

import rmi.common.ClientAccount;
import rmi.common.Common;
import rmi.common.Event;

public class ServantRemoteImpl extends UnicastRemoteObject implements Common {

	// global variables
	private static final long serialVersionUID = 1L;
	final String dir = System.getProperty("user.dir");

	CopyOnWriteArrayList<Event> eventsList = new CopyOnWriteArrayList<Event>();
	CopyOnWriteArrayList<ClientAccount> usersList = new CopyOnWriteArrayList<ClientAccount>();

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
	public void updatEvent(Event event, int index) throws RemoteException {
		eventsList.set(index, event);
		String name = event.toStringForFileName();
		serialize(event, name);
	}

	// user actions
	@Override
	public void SignUp(ClientAccount newAccount) throws RemoteException {
		usersList.add(newAccount);
		String name = newAccount.toStringForFileName();
		serialize(newAccount, name);

	}

	@Override
	public ClientAccount LogIn(String userName, String password) throws RemoteException {

		ClientAccount readUser = usersList.stream().filter(
				(u) -> userName.equals(u.getFirstName() + "_" + u.getLastName()) && password.equals(u.getPassword()))
				.findAny().orElse(null);
		return readUser;

	}

	@Override
	public void bookTheEvent(String userNick, int eventId, int ticketBooked) throws RemoteException {

		// perform changes to event
		Event event = eventsList.get(eventId);
		event.add(userNick, ticketBooked);
		String eventName = event.toStringForFileName();
		serialize(event, eventName);

		// perform changes to user
		usersList.forEach(user -> {
			if (userNick.equals(user.getFirstName() + "_" + user.getLastName())) {
				user.add(eventsList.get(eventId), ticketBooked);
				String name = user.toStringForFileName();
				serialize(user, name);
			}
		});

	}

	@Override
	public void cancelReservation(String userNick, int ticketToReturn, String eventKey) throws RemoteException {

		// perform changes to event
		String[] keyParts = eventKey.split("\n");
		eventsList.forEach(event -> {
			if (keyParts[0].equals(event.getName())) {
				event.remove(userNick, ticketToReturn);
				String name = event.toStringForFileName();
				serialize(event, name);
			}
		});

		// perform changes to user
		usersList.forEach(user -> {
			if (userNick.equals(user.getFirstName() + "_" + user.getLastName())) {
				user.remove(eventKey);
				String name = user.toStringForFileName();
				serialize(user, name);

			}
		});
	}

	// general actions
	@Override
	public int getEventsNumber() throws RemoteException {
		return eventsList.size();
	}

	@Override
	public void LogMessage(String message) throws RemoteException {
		System.out.println(message);
	}

	@Override
	public Event getEvent(int indexOfEvent) throws RemoteException {

		Event selectedEvent = eventsList.get(indexOfEvent);
		return selectedEvent;
	}

	@Override
	public String showEvents(int userTypeFlag) throws RemoteException {
		StringBuilder listShowCase = new StringBuilder();
		int matchId = 0;

		// userTypeFlag: 1 - Administrator, 0 - Client
		for (Event e : eventsList) {

			listShowCase.append("\n=====================================");
			if (e.getTicketLeft() == 0 && userTypeFlag == 0) {
				listShowCase.append("\nTICKETS HAVE BEEN SOLD OUT!!!!").append("\nName: " + e.getName())
						.append("\nPlace: " + e.getPlace()).append("\nDate: " + e.getDate());

			} else {
				listShowCase.append("\nMatch ID: " + matchId).append("\nName: " + e.getName())
						.append("\nPlace: " + e.getPlace()).append("\nDate: " + e.getDate())
						.append("\nTicket left: " + e.getTicketLeft());
				if (userTypeFlag == 1) {
					listShowCase.append("\nNumber of participants: " + e.getTicketBooked())
							.append("\nParticipants: " + e.showParticipants());
				}
			}
			matchId++;
		}
		return listShowCase.toString();
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

		// load list of events and users
		private void laodFilesToLists() throws ClassNotFoundException {
			try {
				File[] files = new File(dir).listFiles();
				for (File file : files) {
					if (file.isFile() && (file.getName().endsWith(".afb") || file.getName().endsWith(".re"))) {
						Event readEvent = (Event) deserialize(file.getName());
						eventsList.add(readEvent);
					} else if (file.isFile() && (file.getName().endsWith(".usr"))) {
						ClientAccount readUser = (ClientAccount) deserialize(file.getName());
						usersList.add(readUser);
					}
				}
				System.out.println("Events and users loaded successfully!!!");

			} catch (NullPointerException e) {
				System.out.print("NullPointerException caught - no files in directory");
			}

		}

	}

	// serialization
	private static boolean serialize(Object obj, String path) {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
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
