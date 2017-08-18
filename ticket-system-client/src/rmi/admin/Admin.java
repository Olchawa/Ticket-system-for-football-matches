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
						remoteObject.addEvent(setEventDetails());
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

			Event selectedEvent = null;
			selectedEvent = remoteObject.getEvent(indexForUpdate);
			System.out.println(separator + "\nSelected event: \n" + selectedEvent.toString());

			// set updated event
			System.out.println("\nSet new properties: " + separator);
			selectedEvent.setName(typeStringInput("Event name: "));
			selectedEvent.setPlace(typeStringInput("Event place: "));
			selectedEvent.setDate(typeEventDate());
			selectedEvent.setTicketLeft(typeTicketNumber());

			// save the update
			remoteObject.updatEvent(selectedEvent, indexForUpdate);
		}
	}

	private Event setEventDetails() {

		// user input
		String name = typeStringInput("Event name: ");
		String place = typeStringInput("Event place: ");
		Date date = typeEventDate();
		int ticketLeft = typeTicketNumber();

		Event newEvent = new Event(name, place, date, ticketLeft);
		return newEvent;
	}

	private Date setDate() {
		Date date=null;
		int value = -1;
		while (value < 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String dateString=input.nextLine();
			try {
				date = formatter.parse(dateString); 
				System.out.println("Date is: " + date);
				value=1;
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

	Date typeEventDate() {
		System.out.println("Date of the event [dd-MM-yyyy hh:mm:ss]");
		return setDate();
	}

	Integer typeTicketNumber() {
		int ticketLeft = 0;
		System.out.println("Number of tickets available for booking:");
		try {
			ticketLeft = Integer.parseInt(input.nextLine());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return ticketLeft;
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
