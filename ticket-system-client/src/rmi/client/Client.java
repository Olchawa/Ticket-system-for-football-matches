package rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import rmi.common.*;

public class Client implements Runnable {

	private Scanner input = new Scanner(System.in);
	Common remoteObject;

	public static void main(String[] args) throws IndexOutOfBoundsException {

		new Thread(new Client()).start();

	}

	public void run() {
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry("localhost");
			remoteObject = (Common) reg.lookup("Server");

			System.out.println("Welcome in  TBS - Ticket Booking System for football matches ");

			while (logIntoSystem()) {
				String choosenOption;
				System.out.println("\nWhat you want to do? ");
				System.out.println("[b]ook a ticket, [s]how your events, [c]ancel reservation, [d]isconnect: ");

				if (input.hasNextLine()) {
					choosenOption = input.nextLine();
					if (!choosenOption.matches("[aed]")) {
						System.err.println("You entered invalid command!");
						continue;
					}
					switch (choosenOption) {
					case "b":
						remoteObject.bookTheEvents();
						break;
					case "s":
						remoteObject.showUserBookedEvents();
						break;
					case "c":
						remoteObject.cancelReservation();
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

	boolean logIntoSystem() {
		System.out.println("\n- select [1] to log into system \n- select [2] to create new accout \n- select [3] to exit the TBS");
		
		return false;
	}

}
