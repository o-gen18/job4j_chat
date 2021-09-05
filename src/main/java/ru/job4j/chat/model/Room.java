package ru.job4j.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "rooms")
public class Room extends Name {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "PERSON_ID_FK"))
    private Person creator;

    @ManyToMany
    private List<Person> persons = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();

    public boolean addPerson(Person person) {
        return persons.add(person);
    }

    public boolean removePerson(Person person) {
        return persons.remove(person);
    }

    public boolean addMessage(Message message) {
        return messages.add(message);
    }

    public boolean removeMessage(Message message) {
        return messages.remove(message);
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Room{" +
                "creator=" + creator.name +
                ", persons=" + persons +
                ", messages=" + messages +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}