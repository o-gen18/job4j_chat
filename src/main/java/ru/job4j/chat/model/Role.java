package ru.job4j.chat.model;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role extends Name {

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
