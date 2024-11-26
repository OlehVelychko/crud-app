package ua.velychko.springcourse.dao;

import org.springframework.stereotype.Component;
import ua.velychko.springcourse.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PersonDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/crud_app_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "crud";

    private static final Logger LOGGER = Logger.getLogger(PersonDAO.class.getName());

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }
    }

    // Helper method for obtaining a connection
    private Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection error", e);
            throw new RuntimeException("Cannot establish a database connection", e);
        }
    }

    // Helper method for exception handling
    private void handleSQLException(SQLException e, String message) {
        LOGGER.log(Level.SEVERE, message, e);
        throw new RuntimeException(message, e);
    }

    public List<Person> index() {
        List<Person> people = new ArrayList<>();
        String SQL = "SELECT * FROM crud_app_schema.person";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {

            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));
                people.add(person);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error fetching people");
        }
        return people;
    }

    public Person show(int id) {
        String SQL = "SELECT * FROM crud_app_schema.person WHERE id=?";
        Person person = null;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    person = new Person();
                    person.setId(resultSet.getInt("id"));
                    person.setName(resultSet.getString("name"));
                    person.setAge(resultSet.getInt("age"));
                    person.setEmail(resultSet.getString("email"));
                } else {
                    LOGGER.log(Level.WARNING, "Person with id {0} not found", id);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error fetching person with id " + id);
        }
        return person;
    }

    public void save(Person person) {
        String SQL = "INSERT INTO crud_app_schema.person (name, age, email) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {

            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e, "Error saving person");
        }
    }

    public void update(int id, Person updatedPerson) {
        String SQL = "UPDATE crud_app_schema.person SET name=?, age=?, email=? WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {

            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                LOGGER.log(Level.WARNING, "No person found to update with id {0}", id);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error updating person with id " + id);
        }
    }

    public void delete(int id) {
        String SQL = "DELETE FROM crud_app_schema.person WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                LOGGER.log(Level.WARNING, "No person found to delete with id {0}", id);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error deleting person with id " + id);
        }
    }
}