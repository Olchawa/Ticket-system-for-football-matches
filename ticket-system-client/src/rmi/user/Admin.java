package rmi.user;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import rmi.common.Common;
import rmi.common.Event;
import rmi.common.InputValidation;
import rmi.user.UserInterface;

public class Admin implements Runnable, UserInterface {
	// global variables
	Common remoteObject;
	private static Scanner input = new Scanner(System.in);
	List<Event> eventList = null;

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
				System.out.println("[a]dd new event\n[e]vents list showcase\n[d]isconnect: ");

				if (input.hasNextLine()) {
					choosenOption = input.nextLine().toLowerCase();
					if (!choosenOption.matches("[aed]")) {
						System.err.println("You entered invalid command!");
						continue;
					}
					switch (choosenOption) {
					case "a":
						Event event = null;
						remoteObject.addEvent(setEventDetails(event));
						break;
					case "e":
						showEvents();
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

	@Override
	public void showEvents() throws RemoteException {
		eventList = remoteObject.getEvents();

		while (true) {
			String choosenOption;
			printEventDetails(eventList);
			System.out.println(
					separator + "\n sort by: [n]ame, [p]lace, [d]ate\n event [s]elect\n back to [m]enu" + separator);
			if (input.hasNextLine()) {
				choosenOption = input.nextLine().toLowerCase();
				if (!choosenOption.matches("[npdsm]")) {
					System.out.println("You entered invalid command!");
					continue;
				}
				switch (choosenOption) {
				case "n":
				case "p":
				case "d":
					eventList = remoteObject.sortEvents(choosenOption);
					break;
				case "s":
					selectEvent();
					break;
				case "m":
					return;
				}
			}
		}

	}

	private void selectEvent() throws RemoteException {
		int indexForUpdate = InputValidation.getIntegerInput(separator + "\nSelect the [ID]: ");
		if (indexForUpdate >= remoteObject.getEventsNumber()) {
			System.out.println(separator + "\nINVALID INDEX!!!");
		} else {

			Event selectedEvent = eventList.get(indexForUpdate);

			while (true) {
				String choosenOption;
				System.out.println(separator + "\nSelected event: \n" + selectedEvent.toString() + ",\nTICKETS: LEFT - "
						+ selectedEvent.getTicketLeft() + ", BOOKED - " + selectedEvent.getTicketBooked());
				System.out.println(separator + "\n [s]how participants\n [u]pdate event info\n [m]enu" + separator);
				if (input.hasNextLine()) {
					choosenOption = input.nextLine();
					if (!choosenOption.matches("[sum]")) {
						System.err.println("You entered invalid command!");
						continue;
					}
					switch (choosenOption) {
					case "s":
						System.out.println("Participants: " + selectedEvent.showParticipants());
						break;
					case "u":
						update(selectedEvent);
						break;
					case "m":
						return;
					}
				}

			}

		}

	}

	private void update(Event event) throws RemoteException {

		// copy old properties for future update
		Event oldEvent = new Event(event.getName(), event.getPlace(), event.getDate(), event.getTicketLeft());
		// set new properties
		Event updatedEvent = setEventDetails(event);
		// save the update
		remoteObject.updatEvent(oldEvent, updatedEvent);

	}

	private Event setEventDetails(Event event) {
		Event reEvent = event;
		if (reEvent == null)
			reEvent = new Event(null, null, null, 0);

		System.out.println("\nSet properties: " + separator);
		reEvent.setName(InputValidation.getStringInput("Event name: "));
		reEvent.setPlace(InputValidation.getStringInput("Event place: "));
		reEvent.setDate(InputValidation.getDate());
		reEvent.setTicketLeft(InputValidation.getIntegerInput("Number of tickets available for booking: "));
		return reEvent;
	}

	@Override
	public void printEventDetails(List<Event> eventlist) {

		final AtomicInteger count = new AtomicInteger(0);
		eventlist.forEach(e -> System.out.println("[" + count.getAndIncrement() + "]" + e.toString() + ", TICKET LEFT: "
				+ e.getTicketLeft() + ", PARTICIPANTS: " + e.getTicketBooked()));

	}

}
