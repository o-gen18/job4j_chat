package ru.job4j.chat.controller;

import antlr.debug.MessageAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.perository.MessageRepository;
import ru.job4j.chat.perository.PersonRepository;
import ru.job4j.chat.perository.RoomRepository;

import java.util.Optional;

@RestController
@RequestMapping("/message")
public class MessageController {
    RoomRepository roomRepository;
    MessageRepository messageRepository;
    PersonRepository personRepository;

    public MessageController(RoomRepository roomRepository,
                             MessageRepository messageRepository,
                             PersonRepository personRepository) {
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
    }

    @PostMapping("/{roomId}/{authorId}")
    public ResponseEntity<Message> postMessage(@RequestBody Message message,
                                               @PathVariable int roomId,
                                               @PathVariable int authorId) {
        Optional<Room> room = roomRepository.findById(roomId);
        Optional<Person> author = personRepository.findById(authorId);
        if (room.isEmpty() || author.isEmpty()) {
            System.out.println("Room doesn't exist or Person not found");
            Message errored = new Message();
            errored.setAuthor(author.orElse(new Person()));
            errored.setRoom(room.orElse(new Room()));
            return new ResponseEntity<Message>(
                errored, HttpStatus.NOT_FOUND
            );
        }
        Room persistedRoom = room.get();
        Person persistedPerson = author.get();

        if ( !persistedRoom.getPersons().containsKey(persistedPerson.getId()) ) {
            System.out.println("Person not in the room");
            Message noPersonInThisRoom = new Message();
            message.setMessage("The person is not registered in the room");
            return new ResponseEntity<Message>(
                    noPersonInThisRoom, HttpStatus.UNAUTHORIZED
            );
        }

        message.setRoom(room.get());
        message.setAuthor(author.get());
        return new ResponseEntity<Message>(
                messageRepository.save(message),
                HttpStatus.OK
        );
    }
}
