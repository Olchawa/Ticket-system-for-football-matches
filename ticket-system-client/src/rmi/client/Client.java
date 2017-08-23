package rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import rmi.common.User;
import rmi.common.Common;
import rmi.common.Event;

public class Client implements Runnable {

	private static Scanner input = new Scanner(System.in);
	Common remoteObject;
	int userFlag = 0;
	String separator = "\n=====================================";
	User user = null;

	public static void main(String[] args) throws IndexOutOfBoundsException {

		new Thread(new Client()).start();

	}

	public void run() {
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry("localhost");
			remoteObject = (Common) reg.lookup("Server");

			System.out.println("Welcome in  TSS - Ticket Sale System for football matches ");

			// accessing the system - logging, sign up
			accessToSystem();

			// main functionality
			while (user != null) {

				// reloading user account - if any changes to event occurs
				user = remoteObject.LogIn(user.getEmail(), user.getPassword());

				String choosenOption;
				System.out.println(separator + "\nWhat you want to do? "
						+ "\n[b]uy tickets\n[s]how your matches\n[r]eturn tickets\n[p]rofile info\n[d]isconnect: ");

				if (input.hasNextLine()) {
					choosenOption = input.nextLine();
					if (!choosenOption.matches("[bsrpd]")) {
						System.err.println("You entered invalid command!");
						continue;
					}
					switch (choosenOption) {
					case "b":
						showAllEvents();
						break;
					case "s":
						showUserEvents(user);
						break;
					case "r":
						resignBooking();
						break;
					case "p":
						showProfileOrUpdate();
						break;
					case "d":
						remoteObject.LogMessage(
								"User " + user.getFirstName() + "_" + user.getLastName() + " has logged out.");
						System.out.println("Thanks for using our system!!!");
						return;
					}
				}
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}

	private void showAllEvents() throws RemoteException {
		// first loading of the list
		List<Event> eventList = remoteObject.getEvents();
		while (true) {

			String choosenOption;
			System.out.println(separator  + "\n[b]uy tickets" + "\n[m]enu"
					+ "\n sort by: [n]ame, [p]lace, [d]ate" + separator);
			System.out.println(remoteObject.showEvents(eventList, userFlag));

			if (input.hasNextLine()) {
				choosenOption = input.nextLine();
				if (!choosenOption.matches("[bnpdm]")) {
					System.err.println("You entered invalid command!");
					continue;
				}
				switch (choosenOption) {
				case "b":
					booking(eventList);
					return;
				case "n":
					System.out.println("sort by name");
					eventList = remoteObject.sortEvents(remoteObject.getEvents(), "byName");
					break;
				case "p":
					eventList = remoteObject.sortEvents(remoteObject.getEvents(), "byPlace");
					break;
				case "d":
					eventList = remoteObject.sortEvents(remoteObject.getEvents(), "byDate");
					break;
				case "m":
					return;
				}
			}
		}

	}

	// BOOKING//
	private void booking(List<Event> list) throws RemoteException {

		int eventID = getIntegerInput(separator + "\nSelect the [Match ID]: \n ");
		input.nextLine();

		if (eventID >= remoteObject.getEventsNumber()) {
			System.out.println(separator + "\nINVALID MATCH INDEX!!!");
		} else {

			int ticketBooked = getIntegerInput(separator + "\nHow many tickets: \n ");
			input.nextLine();

			// tu trzeba ztobiæ filter

			Event tempEvent = list.get(eventID);
			if (tempEvent.getTicketLeft() >= ticketBooked) {

				// add event to user
				String eventKey = tempEvent.getName() + "\n" + tempEvent.getPlace() + "\n" + tempEvent.getStringDate();
				user.add(eventKey, ticketBooked);
				// perform changes on server
				remoteObject.buyOrReturn(user.getFirstName() + "_" + user.getLastName(), ticketBooked, eventKey, "buy");

			} else if (tempEvent.getTicketLeft() == 0) {
				System.out.println(separator + "\nTICKETS FOR THIS MATCH HAVE BEEN SOLD OUT!!!!");
			}

			else
				System.out.println(
						separator + "\nYou can't buy so many tickets. The max is: " + tempEvent.getTicketLeft());

		}

	}

	// RETURN TICKETS//

	private void resignBooking() throws RemoteException {

		LinkedHashMap<String, Integer> clientEvents = user.getEvents();

		if (clientEvents != null && !clientEvents.isEmpty()) {

			showUserEvents(user);
			int eventID = getIntegerInput(separator + "\nSelect the [Match ID]: \n ");
			// clear the buffer
			input.nextLine();

			// temporary lists to store events keys
			List<String> keyList = new ArrayList<String>();

			// iterate through map and add key to list
			clientEvents.forEach((k, v) -> keyList.add(k));

			String keyToRemove = null;
			int ticketToRemove = 0;

			if (eventID < keyList.size()) {
				keyToRemove = keyList.get(eventID);
				ticketToRemove = clientEvents.get(keyToRemove);

				// perform changes on server
				remoteObject.buyOrReturn(user.getFirstName() + "_" + user.getLastName(), ticketToRemove, keyToRemove,
						"return");
				// update locally
				user.remove(keyToRemove);

			} else {
				System.out.println(separator + "\nTHE SELECTED INDEX IS INCORECT!!!");
			}

		} else {
			System.out.println(separator + "\nYou have no tickets bought.");
		}

	}

	// SHOW USER EVENTS//

	private void showUserEvents(User client) {

		LinkedHashMap<String, Integer> clientEvents = client.getEvents();

		if (clientEvents != null && !clientEvents.isEmpty()) {
			int i = 0;
			for (Entry<String, Integer> event : clientEvents.entrySet()) {
				System.out.println(separator + "\n[Match ID]: " + i + "\n" + event.getKey() + "\nYour tickets : "
						+ event.getValue());
				i++;
			}

		} else
			System.out.println(separator + "\nYou have no tickets bought.");

	}
	// SHOW USER PROFILE WITH OPTIONAL UPDATE FUNCTION//

	private void showProfileOrUpdate() throws RemoteException {

		while (true) {

			String choosenOption;
			System.out.println(separator + "\n[s]how profile \n[u]pdate profile\n[b]ack ");

			if (input.hasNextLine()) {
				choosenOption = input.nextLine();
				if (!choosenOption.matches("[sub]")) {
					System.err.println("You entered invalid command!");
					continue;
				}
				switch (choosenOption) {
				case "s":
					System.out.println(user.toString());
					break;
				case "u":
					String[] userDetails = new String[4];
					// set new properties
					userDetails = setUser();

					// check if there is any user with that properties
					String action = remoteObject.checkIfUserExist(userDetails[0], userDetails[1], userDetails[2]);

					if (action.equals("good")) {
						// perform changes on server
						remoteObject.updateUser(user.getFirstName(), user.getLastName(), userDetails);

						// update locally
						user.setFirstName(userDetails[0]);
						user.setLastName(userDetails[1]);
						user.setPassword(userDetails[2]);
						user.setEmail(userDetails[3]);

					} else if (action.equals("names")) {
						System.out.println("Provided names are already used!!!");
						user = null;
						continue;
					} else if (action.equals("email")) {
						System.out.println("Provided email is already used!!!");
						user = null;
						continue;
					}

					break;
				case "b":
					return;
				}
			}
		}

	}

	// LOGGING ACTIONS //

	boolean accessToSystem() throws RemoteException {
		String choosenOption;
		while (true) {
			System.out.println(
					"\n- select [1] to log into system \n- select [2] to create new accout \n- select [3] to exit the TBS");
			if (input.hasNextLine()) {
				choosenOption = input.nextLine();
				if (!choosenOption.matches("[123]")) {
					System.err.println("You entered invalid command!");
					continue;
				}
				switch (choosenOption) {
				case "1":
					Logging();
					remoteObject
							.LogMessage("User " + user.getFirstName() + "_" + user.getLastName() + " has logged in.");
					System.out.println(separator + "\nWelcome " + user.getFirstName() + "!!!");
					break;

				case "2":
					String[] userDetails = new String[4];
					userDetails = setUser();
					user = new User(userDetails[0], userDetails[1], userDetails[2], userDetails[3]);

					// before sign up check if user already exists
					String action = remoteObject.checkIfUserExist(userDetails[0], userDetails[1], userDetails[2]);

					if (action.equals("good")) {
						remoteObject.SignUp(user);
						remoteObject.LogMessage(
								"New account created for user " + user.getFirstName() + "_" + user.getLastName() + ".");
						System.out.println(separator + "\nWelcome " + userDetails[0] + "!!!");
					} else if (action.equals("names")) {
						System.out.println("Provided names are already used!!!");
						user = null;
						continue;
					} else if (action.equals("email")) {
						System.out.println("Provided email is already used!!!");
						user = null;
						continue;
					}
					break;

				case "3":
					System.out.println("Thanks for using our system!!!");
					return false;
				}
			}
			return true;
		}
	}

	private String[] setUser() {

		String[] userDetails = new String[4];
		userDetails[0] = typeStringInput("Your first name: ");
		userDetails[1] = typeStringInput("Your last name: ");
		userDetails[2] = typeStringInput("Your password: ");
		userDetails[3] = typeStringInput("Your email: ");
		return userDetails;
	}

	User Logging() throws RemoteException {

		while (true) {
			String email = typeStringInput("Your email: ");
			String password = typeStringInput("Your password: ");
			user = remoteObject.LogIn(email, password);

			if (user == null) {
				System.out.println(separator + "\nNo such user in the TBS database! \nPassword or email is incorrect!"
						+ separator);
				continue;
			}
			return user;
		}
	}

	String typeStringInput(String inputInfo) {
		System.out.println(inputInfo);
		return input.nextLine();
	}

	public static int getIntegerInput(String prompt) {
		int value = -1;
		while (value < 0) {
			try {
				System.out.print(prompt);
				value = input.nextInt();
			} catch (InputMismatchException ime) {
				System.err.println("Incorrect entry. Please input only a positive integer.");
				input.nextLine();
			}
		}
		return value;
	}

}
