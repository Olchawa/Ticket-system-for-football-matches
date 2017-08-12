package rmi.admin;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import rmi.common.Common;

public class Admin implements Runnable {

	Common remoteObject;
	private Scanner input = new Scanner(System.in);

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
				System.out.println("What you want to do? ");
				System.out.println("[a]dd new event, [e]vents list, [d]isconnect: ");
				if (input.hasNextLine()) {
					choosenOption = input.nextLine();
					if (!choosenOption.matches("[aed]")) {
						System.err.println("You entered invalid command!");
						continue;
					}
					switch (choosenOption) {
					case "a":
						remoteObject.addEvent();
						break;
					case "e":
						remoteObject.showEvents();
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
}
