package ua.velychko.springcourse.dao;

import org.springframework.stereotype.Component;
import ua.velychko.springcourse.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private final List<Person> people;
    private static int PEOPLE_COUNT;

    {
        people = new ArrayList<>();

        people.add(new Person(++PEOPLE_COUNT, "Alice", 2, "alice@gmail.com"));
        people.add(new Person(++PEOPLE_COUNT, "Oleh", 35, "oleh@gmail.com"));
        people.add(new Person(++PEOPLE_COUNT, "Rita", 35, "rita@gmail.com"));
        people.add(new Person(++PEOPLE_COUNT, "Eileen", 9, "eileen@gmail.com"));
    }

    public List<Person> index() {
        return people;
    }

    public Person show(int id) {
        return people.stream()
                .filter(person -> person.getId() == id).findAny().orElse(null);
    }

    public void save(Person person) {
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }

    public void update(int id, Person updatedPerson) {
        Person personToBeUpdated = show(id);

        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setAge(updatedPerson.getAge());
        personToBeUpdated.setEmail(personToBeUpdated.getEmail());
    }

    public void delete(int id) {
        people.removeIf(p -> p.getId() == id);
    }
}
