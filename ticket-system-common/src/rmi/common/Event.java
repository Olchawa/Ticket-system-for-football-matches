package rmi.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String place;
	private Date date;
	private int ticketLeft;
	private int ticketBooked;

	List<ClientAccount> participants = new ArrayList<ClientAccount>();

	public Event() {

	}

	public Event(String name, String place, Date date, int ticketLeft, int ticketBooked,
			List<ClientAccount> participants) {

		this.name = name;
		this.place = place;
		this.date = date;
		this.ticketLeft = ticketLeft;
		this.ticketBooked = ticketBooked;
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

	public List<ClientAccount> getParticipants() {
		return participants;
	}

	public void setParticipants(List<ClientAccount> participants) {
		this.participants = participants;
	}

	@Override
	public String toString() {
		return "Event [name=" + name + ", place=" + place + ", date=" + date + ", ticketLeft=" + ticketLeft
				+ ", ticketBooked=" + ticketBooked + ", participants=" + participants + "]";
	}

	public String toStringForFileName() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = formatter.format(date);

		return name + "_" + place + "_" + strDate + "_" + ticketLeft + ".afb";
	}

}
