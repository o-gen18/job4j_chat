package ru.job4j.chat.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sun.istack.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class Message extends Id {

    @Column(columnDefinition = "TEXT")
    private String message;

    private Timestamp created = new Timestamp(System.currentTimeMillis());

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "AUTHOR_ID_FR"))
    private Person author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(name = "ROOM_ID_FK"))
    private Room room;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", created=" + created +
                ", author=" + author +
                ", room=" + room.name +
                ", id=" + id +
                '}';
    }
}