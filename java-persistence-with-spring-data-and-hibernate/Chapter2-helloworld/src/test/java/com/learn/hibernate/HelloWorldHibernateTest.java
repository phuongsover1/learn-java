package com.learn.hibernate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.learn.hibernate.ch02.Message;
import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;

public class HelloWorldHibernateTest {
  private static SessionFactory createSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure().addAnnotatedClass(Message.class);
    ServiceRegistry serviceRegistry =
        new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    return configuration.buildSessionFactory(serviceRegistry);
  }

  @Test
  void storeLoadMessage() {
    try (SessionFactory sessionFactory = createSessionFactory();
        Session session = sessionFactory.openSession()) {

      session.beginTransaction();

      Message message = new Message();
      message.setText("Hello World from Hibernate!");

      session.persist(message);

      session.getTransaction().commit();
      // INSERT INTO MESSAGE (ID, TEXT)
      // VALUES (1, 'Hello World from Hibernate!')

      session.beginTransaction();

      CriteriaQuery<Message> criteriaQuery =
          session.getCriteriaBuilder().createQuery(Message.class);

      List<Message> messages = session.createQuery(criteriaQuery).getResultList();
      // SELECT * from MESSAGE

      session.getTransaction().commit();

      assertAll(
          () -> assertEquals(1, messages.size()),
          () -> assertEquals("Hello World from Hibernate!", messages.get(0).getText()));
    }
  }
}
