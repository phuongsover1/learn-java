package com.learn.hibernate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.learn.hibernate.ch02.Message;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.jupiter.api.Test;

public class HelloWorldJPATest {
  @Test
  void storeLoadMessage() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch02");
    try {
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();

      Message message = new Message();
      message.setText("Hello World");

      Message goodbyeMessage = new Message();
      goodbyeMessage.setText("Goodbye");

      em.persist(message);
      em.persist(goodbyeMessage);

      em.getTransaction().commit();
      // INSERT into MESSAGE (ID, TEXT) values (1, 'Hello World'), (2, 'Goodbye')

      em.getTransaction().begin();

      List<Message> messages =
          em.createQuery("select m from Message m", Message.class).getResultList();
      // SELECT * from MESSAGE

      messages.get(messages.size() - 1).setText("Hello World from JPA!");
      // UPDATE MESSAGE set TEXT = 'Hello World from JPA!' WHERE ID = 2

      em.getTransaction().commit();

      assertAll(
          () -> assertEquals(2, messages.size()),
          () -> assertEquals("Hello World from JPA!", messages.get(1).getText()));
      em.close();
    } finally {
      emf.close();
    }
  }
}
