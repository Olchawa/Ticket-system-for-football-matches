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

import rmi.common.User;
import rmi.common.Common;
import rmi.common.Event;

public class ServantRemoteImpl extends UnicastRemoteObject implements Common {

	// global variables
	private static final long serialVersionUID = 1L;
	final String dir = System.getProperty("user.dir");

	CopyOnWriteArrayList<Event> eventsList = new CopyOnWriteArrayList<Event>();
	CopyOnWriteArrayList<User> usersList = new CopyOnWriteArrayList<User>();

	public ServantRemoteImpl() throws RemoteException {
		(new LoadLIST()).start();
	}

	// administrator actions
	@Override
	public void addEvent(Event recievedEvent) throws RemoteException {
		eventsList.add(recievedEvent);
		saveOnServer(recievedEvent);
	}

	@Override
	public void updatEvent(Event event, int index) throws RemoteException {
		eventsList.set(index, event);
		saveOnServer(event);
	}

	// user actions
	@Override
	public void SignUp(User newAccount) throws RemoteException {
		usersList.add(newAccount);
		saveOnServer(newAccount);
	}

	@Override
	public User LogIn(String email, String password) throws RemoteException {

		User readUser = usersList.stream().filter((u) -> email.equals(u.getEmail()) && password.equals(u.getPassword()))
				.findAny().orElse(null);
		return readUser;

	}

	@Override
	public void bookTheEvent(String userNick, int eventId, int ticketBooked) throws RemoteException {

		// load user
		Event event = eventsList.get(eventId);
		String eventKey = event.getName() + "\n" + event.getPlace() + "\n" + event.getStringDate();
		
		// perform changes to even
		event.add(userNick, ticketBooked);
		saveOnServer(event);

		// perform changes to user
		usersList.forEach(user -> {
			if (userNick.equals(user.getFirstName() + "_" + user.getLastName())) {
				user.add(eventKey, ticketBooked);
				try {
					saveOnServer(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
				try {
					saveOnServer(event);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		// perform changes to user
		usersList.forEach(user -> {
			if (userNick.equals(user.getFirstName() + "_" + user.getLastName())) {
				user.remove(eventKey);
				try {
					saveOnServer(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// general actions

	@Override
	public void saveOnServer(Object object) throws RemoteException {

		if (object instanceof Event) {
			String name = ((Event) object).toStringForFileName();
			serialize(object, name);
		} else if (object instanceof User) {
			String name = ((User) object).toStringForFileName();
			serialize(object, name);
		}

	}

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
					if (file.isFile() && (file.getName().endsWith(".me"))) {
						Event readEvent = (Event) deserialize(file.getName());
						eventsList.add(readEvent);
					} else if (file.isFile() && (file.getName().endsWith(".usr"))) {
						User readUser = (User) deserialize(file.getName());
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
