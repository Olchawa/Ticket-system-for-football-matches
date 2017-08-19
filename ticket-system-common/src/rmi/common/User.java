package rmi.common;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private String password;
	private String email;

	LinkedHashMap<String, Integer> events;

	// constructor

	public User() {

	}

	public User(String firstName, String lastName, String password, String email) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
	}

	// functions for updating client events
	public void add(String key, int ticketsBooked) {

		if (events == null) {
			events = new LinkedHashMap<>();
		}
		events.computeIfPresent(key, (k, v) -> v + ticketsBooked);
		events.putIfAbsent(key, ticketsBooked);

	}

	public void remove(String key) {
		if (events.containsKey(key)) {
			events.remove(key);
		}
	}

	public boolean hasKey(String oldKey) {
		return events.containsKey(oldKey);
	}

	public void updateEvents(String oldKey, String newKey) {

		int value = events.remove(oldKey);
		events.put(newKey, value);
	}

	// setters and getters

	public LinkedHashMap<String, Integer> getEvents() {
		return events;
	}

	public void setEvents(LinkedHashMap<String, Integer> events) {
		this.events = events;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", password=" + password + ", email=" + email
				+ ", events=" + events + "]";
	}

	public String toStringForFileName() {
		return firstName + "_" + lastName + "_" + email + ".usr";
	}

}
