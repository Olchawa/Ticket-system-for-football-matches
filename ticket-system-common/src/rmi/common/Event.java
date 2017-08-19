package rmi.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Event implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String place;
	private Date date;
	private int ticketLeft;
	private int ticketBooked;

	LinkedHashMap<String, Integer> participants;

	public Event() {

	}

	public Event(String name, String place, Date date, int ticketLeft) {

		this.name = name;
		this.place = place;
		this.date = date;
		this.ticketLeft = ticketLeft;
		this.ticketBooked=0;
	}

	public LinkedHashMap<String, Integer> getParticipants() {
		return participants;
	}

	public String showParticipants() {

		StringBuilder listShowCase = new StringBuilder();
		if (participants != null && !participants.isEmpty()) {
			for (Entry<String, Integer> person : participants.entrySet()) {
				listShowCase.append("\n " + person.getKey() + "  -  tickets : " + person.getValue());

			}

		}
		return listShowCase.toString();
	}

	public void setParticipants(LinkedHashMap<String, Integer> participants) {
		this.participants = participants;
	}

	public void add(String name, int ticket) {

		if (participants == null) {
			participants = new LinkedHashMap<>();
		}
		participants.put(name, ticket);
		if (ticketLeft > 0) {
			ticketLeft -= ticket;
			ticketBooked += ticket;
		}
	}

	public void remove(String key, int ticket) {

		if (participants.containsKey(key)) {
			participants.remove(key);
		}
		ticketLeft += ticket;
		ticketBooked -= ticket;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getTicketLeft() {
		return ticketLeft;
	}

	public void setTicketLeft(int ticketLeft) {
		this.ticketLeft = ticketLeft;
	}

	public int getTicketBooked() {
		return ticketBooked;
	}

	public void setTicketBooked(int ticketBooked) {
		this.ticketBooked = ticketBooked;
	}

	public String getStringDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(date);
	}

	@Override
	public String toString() {
		return " name = " + name + ",\n place = " + place + ",\n date = " + date + ",\n ticketLeft = " + ticketLeft
				+ ",\n ticketBooked = " + ticketBooked;
	}

	public String toStringForFileName() {
		return name + "_" + place + "_" + getStringDate() + "_" + ".me";
	}

}
