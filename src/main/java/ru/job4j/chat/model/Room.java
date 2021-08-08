package ru.job4j.chat.model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "PERSON_ID_FK"))
    private Person creator;

    @ManyToMany
    private Map<Integer, Person> persons = new HashMap<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Map<Integer, Message> messages = new HashMap<>();

    public Person addPerson(Person person) {
        return persons.put(person.getId(), person);
    }

    public Person removePerson(Person person) {
        return persons.remove(person.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public Map<Integer, Person> getPersons() {
        return persons;
    }

    public void setPersons(Map<Integer, Person> persons) {
        this.persons = persons;
    }

    public Map<Integer, Message> getMessages() {
        return messages;
    }

    public void setMessages(Map<Integer, Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}