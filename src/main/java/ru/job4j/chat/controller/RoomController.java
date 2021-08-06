package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.perository.PersonRepository;
import ru.job4j.chat.perository.RoomRepository;

import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomRepository roomRepository;
    private final PersonRepository personRepository;

    public RoomController(RoomRepository roomRepository,
                          PersonRepository personRepository) {
        this.roomRepository = roomRepository;
        this.personRepository = personRepository;
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return new ResponseEntity<>(
                roomRepository.save(room),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/{roomId}/{personId}")
    public ResponseEntity<Room> addPerson(@PathVariable int personId,
                                          @PathVariable int roomId) {
        Optional<Person> persistedPerson = personRepository.findById(personId);
        Optional<Room> persistedRoom = roomRepository.findById(roomId);
        if (persistedPerson.isEmpty()) {
            return new ResponseEntity<>(
                    new Room(),
                    HttpStatus.NOT_FOUND
            );
        }
        if (persistedRoom.isEmpty()) {
            return new ResponseEntity<>(
                    new Room(),
                    HttpStatus.NOT_FOUND
            );
        }
        Room room = persistedRoom.get();
        room.addPerson(persistedPerson.get());
        return new ResponseEntity<>(
                roomRepository.save(room),
                HttpStatus.OK
        );
    }
}