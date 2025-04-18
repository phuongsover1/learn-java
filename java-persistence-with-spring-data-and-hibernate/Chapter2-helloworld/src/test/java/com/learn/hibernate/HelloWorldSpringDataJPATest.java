package com.learn.hibernate;

import com.learn.hibernate.ch02.Message;
import com.learn.hibernate.ch02.repository.MessageRepository;
import com.learn.hibernate.configuration.SpringDataConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class HelloWorldSpringDataJPATest {

  @Autowired
  private MessageRepository messageRepository;

  @Test
  public void storeLoadMessage() {
    Message message = new Message();
    message.setText("Hello World from Spring Data");

    messageRepository.save(message);


    List<Message> messages = (List<Message>) messageRepository.findAll();

    assertAll(
            () -> assertEquals(1, messages.size()),
            () -> assertEquals("Hello World from Spring Data", messages.get(0).getText())
    );
  }
}
