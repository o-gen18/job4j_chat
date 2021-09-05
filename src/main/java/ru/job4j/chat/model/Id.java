package ru.job4j.chat.model;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public abstract class Id {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    public Id(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Id id = (Id) o;
        return this.id == id.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
