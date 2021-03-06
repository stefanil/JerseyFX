package org.devel.jerseyfx.server.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.devel.jerseyfx.common.exceptions.PersonAlreadyExistsException;
import org.devel.jerseyfx.common.exceptions.PersonNotFoundException;
import org.devel.jerseyfx.common.model.Person;

public class PeopleService {

	private static PeopleService instance;

	public PeopleService() {
	}

	public static PeopleService getInstance() {
		if (instance == null)
			instance = new PeopleService();
		return instance;
	}

	private final ConcurrentMap<String, Person> persons = new ConcurrentHashMap<String, Person>();

	public Collection<Person> getPeople(int page, int pageSize) {
		final Collection<Person> slice = new ArrayList<Person>(pageSize);

		final Iterator<Person> iterator = persons.values().iterator();
		for (int i = 0; slice.size() < pageSize && iterator.hasNext();) {
			if (++i > ((page - 1) * pageSize)) {
				slice.add(iterator.next());
			}
		}

		return slice;
	}

	public Person getByEmail(final String email) {
		final Person person = persons.get(email);

		if (person == null) {
			throw new PersonNotFoundException(email);
		}

		return person;
	}

	public Person addPerson(final String email, final String firstName,
			final String lastName) {
		
		final Person person = new Person(email, firstName, lastName,
				new ArrayList<Person>() {
					private static final long serialVersionUID = 3018131847446402380L;
					{
						add(new Person("generic.mother@mail.com"));
					}
				});

		if (persons.putIfAbsent(email, person) != null) {
			throw new PersonAlreadyExistsException(email);
		}

		return person;
	}

	public void removePerson(final String email) {
		if (persons.remove(email) == null) {
			throw new PersonNotFoundException(email);
		}
	}

}
