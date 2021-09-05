package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.MessageRepository;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoomRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;

    public MessageController(RoomRepository roomRepository,
                             MessageRepository messageRepository,
                             PersonRepository personRepository,
                             ObjectMapper objectMapper) {
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/{roomId}/{authorId}")
    public ResponseEntity<Message> postMessage(@RequestBody Message message,
                                               @PathVariable int roomId,
                                               @PathVariable int authorId) {
        Room persistedRoom = roomRepository.findById(roomId).orElseThrow(() ->
                new NoSuchElementException("No room was found by the given id!"));
        List<Person> listInRoom = persistedRoom.getPersons();
        Person persistedPerson = personRepository.findById(authorId).orElseThrow(() ->
                new NoSuchElementException("No person was found by the given id!"));

        if ( !persistedRoom.getPersons().contains(persistedPerson) ) {
            throw new IllegalStateException("The person is not registered in the room so he cannot post the message!");
        }

        message.setRoom(persistedRoom);
        message.setAuthor(persistedPerson);
        Message messagePersisted = messageRepository.save(message);
        return new ResponseEntity<Message>(
                messagePersisted,
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

    @ExceptionHandler(value = {IllegalStateException.class, NoSuchElementException.class})
    private void handleLocalException(Exception e, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setStatus(HttpStatus.FORBIDDEN.value());
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(new HashMap<>(){
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
    }
}
