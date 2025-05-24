package com.manning.javapersistence.ch05;

import com.manning.javapersistence.ch05.model.Item;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldJPATest {
  @Test
  void storeLoadItem() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch05.generator");
    EntityManager em = emf.createEntityManager();

    try {
      em.getTransaction().begin();

      Item item = new Item();
      item.setName("Some Item");
      item.setAuctionEnd(Helper.tomorrow());

      em.persist(item);

      em.getTransaction().commit();
      em.getTransaction().begin();

      List<Item> items = em.createQuery("select i from Item i", Item.class).getResultList();

      em.getTransaction().commit();

      assertAll(
          () -> assertEquals(1, items.size()),
          () -> assertEquals("Some Item", items.get(0).getName())
      );
    } finally {
      em.close();
      emf.close();
    }
  }
}
