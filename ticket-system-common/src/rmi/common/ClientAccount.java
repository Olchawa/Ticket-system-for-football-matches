package rmi.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientAccount implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String firstName;
	private String lastName;
	private String password;
	private String email;

	List<Event> events = new ArrayList<Event>();

	public ClientAccount() {

	}

	public ClientAccount(String firstName, String lastName, String password, String email, List<Event> events) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
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

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "ClientAccount [firstName=" + firstName + ", lastName=" + lastName + ", password=" + password
				+ ", email=" + email + ", events=" + events + "]";
	}

}
