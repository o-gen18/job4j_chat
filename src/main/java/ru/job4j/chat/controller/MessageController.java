package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.MessageRepository;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoomRepository;

import java.util.Optional;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final PersonRepository personRepository;

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

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int messageId) {
        Message message = new Message();
        message.setId(messageId);
        messageRepository.delete(message);
        return ResponseEntity.ok().build();
    }
}
