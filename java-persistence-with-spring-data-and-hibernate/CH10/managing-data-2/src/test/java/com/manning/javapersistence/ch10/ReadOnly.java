package com.manning.javapersistence.ch10;

import org.hibernate.Session;
import org.hibernate.annotations.QueryHints;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ReadOnly {
  private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch10");

  private FetchTestData storeTestData() {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    Long[] itemIds = new Long[3];
    Long[] userIds = new Long[3];

    User johndoe = new User("johndoe");
    em.persist(johndoe);
    userIds[0] = johndoe.getId();

    User janeroe = new User("janeroe");
    em.persist(janeroe);
    userIds[1] = janeroe.getId();

    User robertdoe = new User("robertdoe");
    em.persist(robertdoe);
    userIds[2] = robertdoe.getId();

    Item item = new Item("Item One", LocalDate.now().plusDays(1), johndoe);
    em.persist(item);
    itemIds[0] = item.getId();
    for (int i = 1; i <= 3; i++) {
      Bid bid = new Bid(item, robertdoe, new BigDecimal(9 + i));
      item.addBid(bid);
      em.persist(bid);
    }

    item = new Item("Item Two", LocalDate.now().plusDays(1), johndoe);
    em.persist(item);
    itemIds[1] = item.getId();
    for (int i = 1; i <= 1; i++) {
      Bid bid = new Bid(item, janeroe, new BigDecimal(2 + i));
      item.addBid(bid);
      em.persist(bid);
    }

    item = new Item("Item Three", LocalDate.now().plusDays(2), janeroe);
    em.persist(item);
    itemIds[2] = item.getId();

    em.getTransaction().commit();
    em.close();

    FetchTestData testData = new FetchTestData();
    testData.items = new TestData(itemIds);
    testData.users = new TestData(userIds);
    return testData;
  }

  @Test
  void selectiveReadOnly() {
    EntityManager em = emf.createEntityManager();
    FetchTestData testData = storeTestData();
    em.getTransaction().begin();

    Long ITEM_ID = testData.items.getFirstId();

    {
      em.unwrap(Session.class).setDefaultReadOnly(true);

      Item item = em.find(Item.class, ITEM_ID);
      item.setName("New Name");

      em.flush(); // No UPDATE is executed
    }

    {
      em.clear();
      Item item = em.find(Item.class, ITEM_ID);
      assertNotEquals("New Name", item.getName());
    }

    {
      em.clear();
      Item item = em.find(Item.class, ITEM_ID);
      em.unwrap(Session.class).setReadOnly(item, true);

      item.setName("New Name 2");

      em.flush(); // No UPDATE is executed
    }

    {
      em.clear();
      Item item = em.find(Item.class, ITEM_ID);
      assertNotEquals("New Name 2", item.getName());
    }

    {
      em.clear();

      org.hibernate.query.Query<Item> query = em.unwrap(Session.class)
          .createQuery("select i from Item i", Item.class);

      query.setReadOnly(true).list();

      List<Item> result = query.list();

      for (Item item : result) {
        item.setName("New Name");
      }
      em.flush(); // No UPDATE is executed
    }

    // Verify
    {
      em.clear();
      Item item = em.find(Item.class, ITEM_ID);
      assertNotEquals("New Name", item.getName());
    }

    {
      List<Item> items = em.createQuery("select i from Item i", Item.class)
          .setHint(QueryHints.READ_ONLY, Boolean.TRUE)
          .getResultList();
      for (Item item : items) {
        item.setName("New Name");
      }

      em.flush(); // No UPDATE is executed
    }

    // Verify
    {
      em.clear();
      Item item = em.find(Item.class, ITEM_ID);
      assertNotEquals("New Name", item.getName());
    }

    em.getTransaction().commit();
    em.close();
  }
}
