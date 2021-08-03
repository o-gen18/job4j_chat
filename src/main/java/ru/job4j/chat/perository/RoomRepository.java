package ru.job4j.chat.perository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Room;

public interface RoomRepository extends CrudRepository<Room, Integer> {
}
