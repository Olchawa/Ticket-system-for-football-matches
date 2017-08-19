package rmi.admin;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import rmi.common.Common;
import rmi.common.Event;

public class Admin implements Runnable {

	// global variables
	Common remoteObject;
	private static Scanner input = new Scanner(System.in);
	int adminTypeFlag = 1;
	String separator = "\n=====================================";

	public static void main(String[] args) throws IndexOutOfBoundsException {

		new Thread(new Admin()).start();

	}

	public void run() {
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry("localhost");
			remoteObject = (Common) reg.lookup("Server");

			System.out.println("Welcome in  TBS - Ticket Booking System for football matches ");
			remoteObject.LogMessage("Admin has logged in.");

			while (true) {

				String choosenOption;
				System.out.println(separator + "\nWhat you want to do? ");
				System.out.println("[a]dd new event, [e]vents list showcase,\n[u]pdate the event, [d]isconnect: ");

				if (input.hasNextLine()) {
					choosenOption = input.nextLine();
					if (!choosenOption.matches("[aeud]")) {
						System.err.println("You entered invalid command!");
						continue;
					}
					switch (choosenOption) {
					case "a":
						Event event = null;
						remoteObject.addEvent(setEventDetails(event));
						break;
					case "e":
						System.out.println(remoteObject.showEvents(adminTypeFlag));
						break;
					case "u":
						performUpdate();
						break;
					case "d":
						remoteObject.LogMessage("Admin has logged out.");
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

	private void performUpdate() throws RemoteException {
		System.out.println(remoteObject.showEvents(adminTypeFlag));
		int indexForUpdate = getIntegerInput(separator + "\nTo update select the correct [Match ID]: ");
		// clear the buffer
		input.nextLine();

		if (indexForUpdate >= remoteObject.getEventsNumber()) {
			System.out.println(separator + "\nINVALID MATCH INDEX!!!");
		} else {

			Event selectedEvent = remoteObject.getEvent(indexForUpdate);
			System.out.println(separator + "\nSelected event: \n" + selectedEvent.toString());

			// set properties
			selectedEvent = setEventDetails(selectedEvent);

			// save the update
			remoteObject.updatEvent(selectedEvent, indexForUpdate);
		}
	}

	// GOOD//
	private Event setEventDetails(Event event) {
		Event reEvent = event;
		if (reEvent == null)
			reEvent = new Event(null, null, null, 0);
		// user input
		System.out.println("\nSet properties: " + separator);
		reEvent.setName(typeStringInput("Event name: "));
		reEvent.setPlace(typeStringInput("Event place: "));
		reEvent.setDate(typeEventDate());
		reEvent.setTicketLeft(getIntegerInput("Number of tickets available for booking: "));
		input.nextLine();
		return reEvent;
	}

	private Date typeEventDate() {
		Date date = null;
		int value = -1;
		System.out.println("Date of the event [dd-MM-yyyy hh:mm:ss]");
		while (value < 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String dateString = input.nextLine();
			try {
				date = formatter.parse(dateString);
				value = 1;
			} catch (ParseException e) {
				System.out.println("Date format must be: [dd-M-yyyy hh:mm:ss]");
			}
		}
		return date;

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
