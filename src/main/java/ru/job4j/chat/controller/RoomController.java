package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoomRepository;

import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomRepository roomRepository;
    private final PersonRepository personRepository;
    private static final Supplier<? extends RuntimeException> personNotFoundExceptionSupplier =
            () -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No person was found by the given id!");

    public RoomController(RoomRepository roomRepository,
                          PersonRepository personRepository) {
        this.roomRepository = roomRepository;
        this.personRepository = personRepository;
    }

    @PostMapping("/create/{personId}")
    public ResponseEntity<Room> create(@RequestBody Room room,
                                       @PathVariable int personId) {
        Person person = personRepository.findById(personId).orElseThrow(personNotFoundExceptionSupplier);

        System.out.println(person);
        room.addPerson(person);
        room.setCreator(person);
        return new ResponseEntity<>(roomRepository.save(room), HttpStatus.CREATED);
    }

    @PostMapping("/{roomId}/{personId}")
    public ResponseEntity<Room> addPerson(@PathVariable int personId,
                                          @PathVariable int roomId) {
        Person persistedPerson = personRepository.findById(personId).orElseThrow(personNotFoundExceptionSupplier);

        Room persistedRoom = roomRepository.findById(roomId)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No room was found by the given id!"));

        persistedRoom.addPerson(persistedPerson);

        return new ResponseEntity<>(roomRepository.save(persistedRoom), HttpStatus.OK);
    }

    @GetMapping("/all/{personId}")
    public List<Room> getPersonRooms(@PathVariable int personId) {
        Person person = personRepository.findById(personId).orElseThrow(personNotFoundExceptionSupplier);
        return roomRepository.findRoomsByCreatorName(person.getName());
    }
}