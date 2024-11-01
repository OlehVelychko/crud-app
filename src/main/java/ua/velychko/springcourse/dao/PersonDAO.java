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

        people.add(new Person(++PEOPLE_COUNT, "Alice"));
        people.add(new Person(++PEOPLE_COUNT, "Oleh"));
        people.add(new Person(++PEOPLE_COUNT, "Rita"));
        people.add(new Person(++PEOPLE_COUNT, "Eileen"));
    }

    public List<Person> index() {
        return people;
    }

    public Person show(int id) {
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    public void save(Person person) {
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }
}
