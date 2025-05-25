package com.manning.javapersistence.ch06;

import com.manning.javapersistence.ch06.model.Item;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityFactoryTest {
  @Test
  void testStoreItem() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch06.mapping_value_types");
    EntityManager em = emf.createEntityManager();

    try {
      Item item = new Item();
      item.setName("New Item");
      em.getTransaction().begin();

      em.persist(item);

      em.getTransaction().commit();

      em.getTransaction().begin();
      List<Item> items = em.createQuery("select i from Item i", Item.class).getResultList();
      em.getTransaction().commit();

      assertAll(
          () -> assertEquals(1, items.size()),
          () -> assertEquals("AUCTION: New Item", items.get(0).getName()));
    } finally {
      em.close();
      emf.close();
    }
  }
}
