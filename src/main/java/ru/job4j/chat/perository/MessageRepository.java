package ru.job4j.chat.perository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {
}
