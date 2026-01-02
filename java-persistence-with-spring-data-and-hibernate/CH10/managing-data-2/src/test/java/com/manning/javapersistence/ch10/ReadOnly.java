package com.manning.javapersistence.ch10;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;

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
}
