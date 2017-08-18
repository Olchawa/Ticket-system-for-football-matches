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

import rmi.common.ClientAccount;
import rmi.common.Common;
import rmi.common.Event;

public class Client implements Runnable {

	private static Scanner input = new Scanner(System.in);
	Common remoteObject;
	int userFlag = 0;
	String separator = "\n=====================================";
	ClientAccount loadedClient = null;

	public static void main(String[] args) throws IndexOutOfBoundsException {

		new Thread(new Client()).start();

	}

	public void run() {
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry("localhost");
			remoteObject = (Common) reg.lookup("Server");

			System.out.println("Welcome in  TBS - Ticket Booking System for football matches ");

			// check if user is the new client
			accessToSystem();

			// main functionality
			while (loadedClient != null) {

				String choosenOption;
				System.out.println(separator + "\nWhat you want to do? "
						+ "\n[b]ook a ticket, [s]how your events, \n[c]ancel reservation, [d]isconnect: ");

				if (input.hasNextLine()) {
					choosenOption = input.nextLine();
					if (!choosenOption.matches("[bscd]")) {
						System.err.println("You entered invalid command!");
						continue;
					}
					switch (choosenOption) {
					case "b":
						booking();
						break;
					case "s":
						showUserEvents(loadedClient);
						break;
					case "c":
						resignBooking();
						break;
					case "d":
						remoteObject.LogMessage("User " + loadedClient.getFirstName() + "_" + loadedClient.getLastName()
								+ " has logged out.");
						System.out.println("Thanks for using the TBS!!!");
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

	private void booking() throws RemoteException {
		System.out.println(remoteObject.showEvents(userFlag));

		int eventID = getIntegerInput(separator + "\nSelect the [Match ID] for booking: \n ");
		input.nextLine();
		if (eventID >= remoteObject.getEventsNumber()) {
			System.out.println(separator + "\nINVALID MATCH INDEX!!!");
		} else {

			int ticketBooked = getIntegerInput(separator + "\nHow many tickets: \n ");
			input.nextLine();
			Event tempEvent = remoteObject.getEvent(eventID);
			if (tempEvent.getTicketLeft() >= ticketBooked) {
				// add event to client
				loadedClient.add(tempEvent, ticketBooked);
				// perform changes on server
				remoteObject.bookTheEvent(loadedClient.getFirstName() + "_" + loadedClient.getLastName(), eventID,
						ticketBooked);
			} else if (tempEvent.getTicketLeft() == 0) {
				System.out.println(separator + "\nTICKETS FOR THIS MATCH HAVE BEEN SOLD OUT!!!!");
			}

			else
				System.out.println(
						separator + "\nYou can't buy so many tickets. The max is: " + tempEvent.getTicketLeft());

		}

	}

	private void resignBooking() throws RemoteException {

		LinkedHashMap<String, Integer> clientEvents = loadedClient.getEvents();

		if (clientEvents != null && !clientEvents.isEmpty()) {

			showUserEvents(loadedClient);
			int eventID = getIntegerInput(separator + "\nSelect the [Match ID] you want to cancel booking: \n ");
			// clear the buffer
			input.nextLine();

			// load user events to temporary map

			// temporary lists to store events keys
			List<String> keyList = new ArrayList<String>();

			// iterate through map and add key to list
			clientEvents.forEach((k, v) -> keyList.add(k));

			String keyToRemove = null;
			int ticketToRemove = 0;

			if (eventID < keyList.size()) {
				keyToRemove = keyList.get(eventID);
				ticketToRemove = clientEvents.get(keyToRemove);
				// remove event from client
				loadedClient.remove(keyToRemove);
				// perform changes on server
				remoteObject.cancelReservation(loadedClient.getFirstName() + "_" + loadedClient.getLastName(),
						ticketToRemove, keyToRemove);

			} else {
				System.out.println(separator + "\nTHE SELECTED INDEX IS INCORECT!!!");
			}

		} else {
			System.out.println(separator + "\nYou have no events booked.");
		}

	}

	private void showUserEvents(ClientAccount client) {

		LinkedHashMap<String, Integer> clientEvents = client.getEvents();

		if (clientEvents != null && !clientEvents.isEmpty()) {
			int i = 0;
			for (Entry<String, Integer> event : clientEvents.entrySet()) {
				System.out.println(separator + "\n[Match ID]: " + i + "\nMatch details: \n" + event.getKey()
						+ "\nYour tickets : " + event.getValue());
				i++;
			}

		} else
			System.out.println(separator + "\nYou have no events booked.");

	}

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
					remoteObject.LogMessage("User " + loadedClient.getFirstName() + "_" + loadedClient.getLastName()
							+ " has logged in.");
					System.out.println(separator + "\nWelcome " + loadedClient.getFirstName() + "!!!");
					break;

				case "2":
					remoteObject.SignUp(setUser());
					remoteObject.LogMessage("New account created for user " + loadedClient.getFirstName() + "_"
							+ loadedClient.getLastName() + ".");
					System.out.println(separator + "\nWelcome " + loadedClient.getFirstName() + "!!!");
					break;

				case "3":
					System.out.println("Thanks for using the TBS!!!");
					return false;
				}
			}
			return true;
		}
	}

	private ClientAccount setUser() {

		String firstName = typeStringInput("Your first name: ");
		String lastName = typeStringInput("Your last name: ");
		String password = typeStringInput("Your password: ");
		String email = typeStringInput("Your email: ");
		loadedClient = new ClientAccount(firstName, lastName, password, email);
		return loadedClient;
	}

	ClientAccount Logging() throws RemoteException {

		while (true) {
			String firstName = typeStringInput("Your first name: ");
			String lastName = typeStringInput("Your last name: ");
			String password = typeStringInput("Your password: ");
			loadedClient = remoteObject.LogIn(firstName + "_" + lastName, password);

			if (loadedClient == null) {
				System.out.println(
						separator + "\nNo such user in the TBS database! \nPassword or name is incorrect!" + separator);
				continue;
			}
			return loadedClient;
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
