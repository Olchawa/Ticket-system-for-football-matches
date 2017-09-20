package rmi.user;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Scanner;

import rmi.common.User;
import rmi.user.UserInterface;
import rmi.common.Common;
import rmi.common.Event;
import rmi.common.InputValidation;

public class Client implements Runnable, UserInterface {

	private static Scanner input = new Scanner(System.in);
	Common remoteObject;
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
					choosenOption = input.nextLine().toLowerCase();
					if (!choosenOption.matches("[bsrpd]")) {
						System.err.println("You entered invalid command!");
						continue;
					}
					switch (choosenOption) {
					case "b":
						showEvents();
						break;
					case "s":
						showUserEvents(user);
						break;
					case "r":
						returnTickets();
						break;
					case "p":
						System.out.println(user.toString());
						break;
					case "d":
						remoteObject.LogMessage(
								"UserInterface " + user.getFirstName() + "_" + user.getLastName() + " has logged out.");
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

	@Override
	public void showEvents() throws RemoteException {

		List<Event> eventList = remoteObject.getEvents();
		while (true) {

			String choosenOption;

			printEventDetails(eventList);

			System.out.println(
					separator + "\n sort by: [n]ame, [p]lace, [d]ate\n tickets [b]uy\n back to [m]enu" + separator);

			if (input.hasNextLine()) {
				choosenOption = input.nextLine().toLowerCase();
				if (!choosenOption.matches("[bnpdm]")) {
					System.out.println("You entered invalid command!!!\n");
					continue;
				}
				switch (choosenOption) {
				case "b":
					booking(eventList);
					return;
				case "n":
				case "p":
				case "d":
					eventList = remoteObject.sortEvents(choosenOption);
					break;
				case "m":
					return;
				}
			}
		}

	}

	private void booking(List<Event> list) throws RemoteException {

		int eventID = InputValidation.getIntegerInput(separator + "\nSelect the [ID]: \n ");

		if (eventID >= remoteObject.getEventsNumber()) {
			System.out.println(separator + "\nINVALID MATCH INDEX!!!");
		} else {

			int ticketBooked = InputValidation.getIntegerInput(separator + "\nHow many tickets: \n ");

			Event tempEvent = list.get(eventID);
			if (tempEvent.getTicketLeft() >= ticketBooked) {

				String eventKey = tempEvent.getName() + "\n" + tempEvent.getPlace() + "\n" + tempEvent.getStringDate();

				// perform changes on server
				remoteObject.buyOrReturn(user.getFirstName() + "_" + user.getLastName() + "_" + user.getEmail(),
						ticketBooked, eventKey, "buy");

				// add event to user
				user.add(eventKey, ticketBooked);

			} else if (tempEvent.getTicketLeft() == 0) {
				System.out.println(separator + "\nTICKETS FOR THIS MATCH HAVE BEEN SOLD OUT!!!!");
			}

			else
				System.out.println(
						separator + "\nYou can't buy so many tickets. The max is: " + tempEvent.getTicketLeft());

		}

	}

	private void returnTickets() throws RemoteException {

		LinkedHashMap<String, Integer> clientEvents = user.getEvents();

		if (clientEvents != null && !clientEvents.isEmpty()) {

			showUserEvents(user);
			int eventID = InputValidation.getIntegerInput(separator + "\nSelect the [ID]: \n ");

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
				remoteObject.buyOrReturn(user.getFirstName() + "_" + user.getLastName() + "_" + user.getEmail(),
						ticketToRemove, keyToRemove, "return");
				// update locally
				user.remove(keyToRemove);

			} else {
				System.out.println(separator + "\nTHE SELECTED INDEX IS INCORECT!!!");
			}

		} else {
			System.out.println(separator + "\nYou have no tickets bought.");
		}

	}

	private void showUserEvents(User client) {

		LinkedHashMap<String, Integer> clientEvents = client.getEvents();

		if (clientEvents != null && !clientEvents.isEmpty()) {
			int i = 0;
			System.out.println(separator);
			for (Entry<String, Integer> event : clientEvents.entrySet()) {
				String[] keyParts = event.getKey().split("\n");
				System.out.println("\n[" + i + "]:" + " NAME: " + keyParts[0] + ", PLACE: " + keyParts[1] + ", DATE: "
						+ keyParts[2] + ", YOUR TICKETS: " + event.getValue());
				i++;
			}

		} else
			System.out.println(separator + "\nYou have no tickets bought.");

	}

	boolean accessToSystem() throws RemoteException {
		String choosenOption;
		while (true) {
			System.out.println("\n[l]og into system \n[c]reate new accout\n[e]xit the TSS system");
			if (input.hasNextLine()) {
				choosenOption = input.nextLine().toLowerCase();
				if (!choosenOption.matches("[lce]")) {
					System.err.println("You entered invalid command!");
					continue;
				}
				switch (choosenOption) {
				case "l":
					if (logging()) {
						remoteObject.LogMessage(
								"UserInterface " + user.getFirstName() + "_" + user.getLastName() + " has logged in.");
						System.out.println(separator + "\nWelcome " + user.getFirstName() + "!!!");
						break;
					} else {
						continue;
					}

				case "c":
					String[] userDetails = new String[4];
					userDetails = setUserDetails();

					// before sign up check if user already exists
					remoteObject.checkIfEmailExist(userDetails[3]);

					if (!remoteObject.checkIfEmailExist(userDetails[3])) {
						user = new User(userDetails[0], userDetails[1], userDetails[2], userDetails[3]);
						remoteObject.SignUp(user);
						remoteObject.LogMessage(
								"New account created for user " + user.getFirstName() + "_" + user.getLastName() + ".");
						System.out.println(separator + "\nWelcome " + userDetails[0] + "!!!");
					} else {
						System.out.println("This email is already used!!!");
						continue;
					}
					break;

				case "e":
					System.out.println("Thanks for using our system!!!");
					return false;
				}
			}
			return true;
		}
	}

	private String[] setUserDetails() {

		String[] userDetails = new String[4];
		userDetails[0] = InputValidation.getStringInput("Your first name: ");
		userDetails[1] = InputValidation.getStringInput("Your last name: ");
		userDetails[2] = InputValidation.getStringInput("Your password: ");
		userDetails[3] = InputValidation.getStringInput("Your email: ");
		return userDetails;
	}

	boolean logging() throws RemoteException {

		while (true) {
			String email = InputValidation.getStringInput("Your email: ");
			String password = InputValidation.getStringInput("Your password: ");
			user = remoteObject.LogIn(email, password);

			if (user == null) {
				System.out.println(separator + "\nPassword or email is incorrect!" + separator);
				return false;
			}
			return true;
		}
	}

	@Override
	public void printEventDetails(List<Event> eventlist) {

		final AtomicInteger count = new AtomicInteger(0);
		eventlist.forEach(e -> System.out.println("[" + count.getAndIncrement() + "]" + e.toString()));
	}

}
