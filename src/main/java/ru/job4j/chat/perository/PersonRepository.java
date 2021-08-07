package ru.job4j.chat.perository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    Person findByName(String name);
}
