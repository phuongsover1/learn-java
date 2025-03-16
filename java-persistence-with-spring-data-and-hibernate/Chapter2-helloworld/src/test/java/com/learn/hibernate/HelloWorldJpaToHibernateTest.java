package com.learn.hibernate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.learn.hibernate.ch02.Message;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

public class HelloWorldJpaToHibernateTest {
  private static final EntityManagerFactory entityManagerFactory =
      Persistence.createEntityManagerFactory("ch02");

  private static SessionFactory getSessionFactory() {
    return entityManagerFactory.unwrap(SessionFactory.class);
  }

  @Test
  void storeLoadMessage() {
    try (SessionFactory sessionFactory = getSessionFactory();
        Session session = sessionFactory.openSession()) {
      session.beginTransaction();

      Message message = new Message();
      message.setText("Hello World from JPA configuration to Hibernate");

      session.persist(message);
      session.getTransaction().commit();
      ;
      // INSERT INTO MESSAGE (ID, TEXT)
      // VALUES (1, 'Hello World from JPA configuration to Hibernate')

      session.beginTransaction();

      CriteriaQuery<Message> criteriaQuery =
          session.getCriteriaBuilder().createQuery(Message.class);
      criteriaQuery.from(Message.class);

      List<Message> messages = session.createQuery(criteriaQuery).getResultList();
      // SELECT * FROM MESSAGE

      session.getTransaction().commit();

      assertAll(
          () -> assertEquals(1, messages.size()),
          () ->
              assertEquals(
                  "Hello World from JPA configuration to Hibernate", messages.get(0).getText()));
    }
  }
}
