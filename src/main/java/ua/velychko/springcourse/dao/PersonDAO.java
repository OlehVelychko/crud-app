package ua.velychko.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.velychko.springcourse.models.Person;

import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // SQL Queries
    private static final String SELECT_ALL = "SELECT * FROM crud_app_schema.person";
    private static final String SELECT_BY_ID = "SELECT * FROM crud_app_schema.person WHERE id=?";
    private static final String INSERT_PERSON = "INSERT INTO crud_app_schema.person (name, age, email) VALUES (?, ?, ?)";
    private static final String UPDATE_PERSON = "UPDATE crud_app_schema.person SET name=?, age=?, email=? WHERE id=?";
    private static final String DELETE_PERSON = "DELETE FROM crud_app_schema.person WHERE id=?";

    // Fetch all persons
    public List<Person> index() {
        return jdbcTemplate.query(SELECT_ALL, new PersonMapper());
    }

    // Fetch a single person by ID
    public Person show(int id) {
        return jdbcTemplate.query(SELECT_BY_ID, new Object[]{id}, new PersonMapper())
                .stream().findAny().orElse(null);
    }

    // Save a new person
    public void save(Person person) {
        jdbcTemplate.update(INSERT_PERSON, person.getName(), person.getAge(), person.getEmail());
    }

    // Update an existing person by ID
    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update(UPDATE_PERSON,
                updatedPerson.getName(),
                updatedPerson.getAge(),
                updatedPerson.getEmail(),
                id);
    }

    // Delete a person by ID
    public void delete(int id) {
        jdbcTemplate.update(DELETE_PERSON, id);
    }
}