package ru.job4j.chat.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    List<Room> findRoomsByCreatorName(String name);

    @Override
    @EntityGraph(attributePaths = {"persons"})
    Optional<Room> findById(Integer integer);
}
