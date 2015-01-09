package org.devel.jerseyfx.common.model;

import java.util.List;

public class Person {

	private String email;
	private String firstName;
	private String lastName;
	private List<Person> parents;
		
	public Person() {
	}
	
	public Person( final String email ) {
		if(email == null)
			throw new IllegalArgumentException("Email of Person must not be null.");
		this.email = email;
	}
	
	public Person(final String email, final String firstName, final String lastName) {
		this(email);
		if(firstName == null)
			throw new IllegalArgumentException("First name of Person must not be null.");
		if(lastName == null)
			throw new IllegalArgumentException("Last name of Person must not be null.");
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Person(final String email, final String firstName, final String lastName, final List<Person> parents) {
		this(email, firstName, lastName);
		if(parents == null) {
			throw new IllegalArgumentException("Mother of Person must not be null.");
		}
		this.parents = parents;
	}

	@Override
	public String toString() {
		return "Person = {\n\teMail: " + this.email + "\n\tFirst name: " + this.firstName + "\n\tLast name: " + this.lastName +
				"\n\tSize of parents: " + (this.parents != null ? this.parents.size() : "0") + "\n}";
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail( final String email ) {
		this.email = email;
	}
		
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setFirstName( final String firstName ) {
		this.firstName = firstName;
	}
	
	public void setLastName( final String lastName ) {
		this.lastName = lastName;
	}

	public List<Person> getParents() {
		return parents;
	}

	public void setParents(List<Person> parents) {
		this.parents = parents;
	}
	
}
