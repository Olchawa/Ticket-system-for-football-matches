package rmi.admin;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import rmi.common.ClientAccount;
import rmi.common.Common;
import rmi.common.Event;

public class Admin implements Runnable {

	// global variables
	Common remoteObject;
	private Scanner input = new Scanner(System.in);
	public static final String ANSI_RED = "\u001B[31m";

	public static void main(String[] args) throws IndexOutOfBoundsException {

		new Thread(new Admin()).start();

	}

	public void run() {
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry("localhost");
			remoteObject = (Common) reg.lookup("Server");

			System.out.println("Welcome in  TBS - Ticket Booking System for football matches ");

			while (true) {
				String choosenOption;
				System.out.println("\nWhat you want to do? ");
				System.out.println("[a]dd new event, [e]vents list showcase, [d]isconnect: ");
				if (input.hasNextLine()) {
					choosenOption = input.nextLine();
					if (!choosenOption.matches("[aed]")) {
						System.err.println("You entered invalid command!");
						continue;
					}
					switch (choosenOption) {
					case "a":
						remoteObject.addEvent(setEventDetails());
						break;
					case "e":
						showEvents(remoteObject.getEvents());
						break;
					case "d":
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

	private void showEvents(ArrayList<Event> events) {
		int matchId = 0;
		for (Event e : events) {
			System.out.println("\n=====================================");
			System.out.println("Match ID: " + matchId);
			System.out.println("Name: " + e.getName());
			System.out.println("Place: " + e.getPlace());
			System.out.println("Date: " + e.getDate());
			System.out.println("Ticket left: " + e.getTicketLeft());
			System.out.println("Number of participants: " + e.getTicketBooked());
			matchId++;
		}

	}

	private Event setEventDetails() {

		String name = null, place = null;
		Date date = null;
		int ticketLeft = 0, ticketBooked = 0;
		List<ClientAccount> participants = new ArrayList<ClientAccount>();

		// user input
		System.out.println("Event name:");
		name = input.nextLine();

		System.out.println("Event place:");
		place = input.nextLine();

		System.out.println("Date of the event [dd-M-yyyy hh:mm:ss]");
		date = setDate();

		System.out.println("Number of tickets available for booking:");
		try {
			ticketLeft = Integer.parseInt(input.nextLine());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		Event newEvent = new Event(name, place, date, ticketLeft, ticketBooked, participants);

		return newEvent;
	}

	private Date setDate() {

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		try {
			Date date = formatter.parse("08-09-2017 20:45:00");
			System.out.println("Date is: " + date);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}
}
