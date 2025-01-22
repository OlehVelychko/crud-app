package ua.com.alicecompany.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.alicecompany.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Person> index() {
        return entityManager.createQuery("from Person", Person.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<Person> show(String email) {
        return entityManager.createQuery("from Person where email = :email", Person.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Transactional(readOnly = true)
    public Person show(int id) {
        return entityManager.find(Person.class, id);
    }

    @Transactional
    public void save(Person person) {
        entityManager.persist(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        Person existingPerson = entityManager.find(Person.class, id);
        if (existingPerson != null) {
            existingPerson.setName(updatedPerson.getName());
            existingPerson.setAge(updatedPerson.getAge());
            existingPerson.setEmail(updatedPerson.getEmail());
            existingPerson.setAddress(updatedPerson.getAddress());
        }
    }

    @Transactional
    public void delete(int id) {
        Person person = entityManager.find(Person.class, id);
        if (person != null) {
            entityManager.remove(person);
        }
    }
}