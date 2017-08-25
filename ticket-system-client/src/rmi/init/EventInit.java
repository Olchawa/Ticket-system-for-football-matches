package rmi.init;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import rmi.common.Common;
import rmi.common.Event;

public class EventInit implements Runnable {

	Common remoteObject;

	public static void main(String[] args) throws IndexOutOfBoundsException {

		new Thread(new EventInit()).start();

	}

	public void run() {
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry("localhost");
			remoteObject = (Common) reg.lookup("Server");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

			// sample events
			remoteObject.addEvent(
					new Event("Real Madryt vs FC Barcelona", "Madryt", formatter.parse("10-10-2017 22:00:00"), 20));
			remoteObject
					.addEvent(new Event("Arsenal vs Everton", "London", formatter.parse("08-09-2017 15:00:00"), 30));
			remoteObject.addEvent(
					new Event("Bayern Monachium vs PSG", "Monachium", formatter.parse("25-09-2017 20:45:00"), 25));
			remoteObject.addEvent(new Event("AS Roma vs AC Milan", "Rome", formatter.parse("22-11-2017 21:00:00"), 40));
			remoteObject.addEvent(new Event("Legia vs Lech", "Warsaw", formatter.parse("28-01-2018 18:00:00"), 10));

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
