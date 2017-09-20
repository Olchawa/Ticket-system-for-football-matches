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

	// constructors
	public Event() {

	}

	public Event(String name, String place, Date date, int ticketLeft) {

		this.name = name;
		this.place = place;
		this.date = date;
		this.ticketLeft = ticketLeft;
		this.ticketBooked = 0;
	}

	// functions for updating
	public void add(String name, int ticket) {

		if (participants == null) {
			participants = new LinkedHashMap<>();
		}
		participants.computeIfPresent(name, (k, v) -> v + ticket);
		participants.putIfAbsent(name, ticket);

		ticketLeft -= ticket;
		ticketBooked += ticket;

	}

	public void remove(String key, int ticket) {

		if (participants.containsKey(key)) {
			participants.remove(key);
		}
		ticketLeft += ticket;
		ticketBooked -= ticket;

	}

	public boolean hasKey(String userKey) {
		if (participants == null) {
			return false;
		} else
			return participants.containsKey(userKey);
	}

	public void updateEvents(String oldKey, String newKey) {
		int value = participants.remove(oldKey);
		participants.put(newKey, value);

	}

	// setters and getters
	public LinkedHashMap<String, Integer> getParticipants() {
		return participants;
	}

	public void setParticipants(LinkedHashMap<String, Integer> participants) {
		this.participants = participants;
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

	public String showParticipants() {

		StringBuilder listShowCase = new StringBuilder();
		if (participants != null && !participants.isEmpty()) {
			for (Entry<String, Integer> person : participants.entrySet()) {
				listShowCase.append("\n " + person.getKey() + "  -  tickets : " + person.getValue());

			}

		}
		return listShowCase.toString();
	}

	public String getStringDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(date);
	}

	@Override
	public String toString() {
		return "NAME: " + getName() + ", PLACE: " + getPlace() + ", DATE: "+ getDate();
		
	}

	public String toStringForFileName() {
		return name + "_" + place + "_" + getStringDate() + "_" + ".me";
	}

}
