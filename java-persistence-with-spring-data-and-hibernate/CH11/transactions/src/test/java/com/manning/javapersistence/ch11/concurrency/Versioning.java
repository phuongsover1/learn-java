package com.manning.javapersistence.ch11.concurrency;

import org.junit.jupiter.api.Test;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Versioning {
  EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch11");

  ConcurrencyTestData storeCategoriesAndItems() {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();

    ConcurrencyTestData testData = new ConcurrencyTestData();
    testData.categories = new TestData(new Long[3]);
    testData.items = new TestData(new Long[5]);
    for (int i = 1; i <= testData.categories.identifiers.length; i++) {
      Category category = new Category();
      category.setName("Category: " + i);
      em.persist(category);
      testData.categories.identifiers[i - 1] = category.getId();
      for (int j = 1; j <= testData.categories.identifiers.length; j++) {
        Item item = new Item("Item " + j);
        item.setCategory(category);
        item.setBuyNowPrice(new BigDecimal(10 + j));
        em.persist(item);
        testData.items.identifiers[(i - 1) + (j - 1)] = item.getId();
      }
    }
    em.getTransaction().commit();
    em.close();
    return testData;
  }

  @Test
  void firstCommitWins() throws ExecutionException, InterruptedException {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();

    Item someItem = new Item("Some Item");
    em.persist(someItem);
    em.getTransaction().commit();
    em.close();
    final Long ITEM_ID = someItem.getId();

    EntityManager em1 = emf.createEntityManager();
    em1.getTransaction().begin();

    /*
       Retrieving an entity instance by identifier loads the current version from the
       database with a <code>SELECT</code>.
    */
    Item item = em1.find(Item.class, ITEM_ID);
    // select * from ITEM where ID = ?

    /*
       The current version of the <code>Item</code> instance is 0.
    */
    assertEquals(0L, item.getVersion());

    item.setName("New Name");

    // The concurrent second unit of work doing the same
    Executors.newSingleThreadExecutor()
        .submit(
            () -> {
              try {
                EntityManager em2 = emf.createEntityManager();
                em2.getTransaction().begin();

                Item item1 = em2.find(Item.class, ITEM_ID);
                // select * from ITEM where ID = ?

                item1.setName("Other Name");

                em2.getTransaction().commit();
                // update ITEM set NAME = ?, VERSION = 1 where ID = ? and VERSION = 0
                // This succeeds, there is a row with ID = ? and VERSION = 0 in the database!
                em2.close();
              } catch (Exception ex) {
                // This shouldn't happen, this commit should win!
                throw new RuntimeException("Concurrent operation failure: " + ex, ex);
              }
              return null;
            })
        .get();

    /*
       When the persistence context is flushed Hibernate will detect the dirty
       <code>Item</code> instance and increment its version to 1. The SQL
       <code>UPDATE</code> now performs the version check, storing the new version
       in the database, but only if the database version is still 0.
    */
    assertThrows(OptimisticLockException.class, () -> em1.flush());
    // update ITEM set NAME = ?, VERSION = 1 where ID = ? and VERSION = 0
  }

  @Test
  void manualVersionChecking() throws ExecutionException, InterruptedException {
    final ConcurrencyTestData testData = storeCategoriesAndItems();
    Long[] CATEGORIES = testData.categories.identifiers;

    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();

    BigDecimal totalPrice = BigDecimal.ZERO;
    for (Long categoryId : CATEGORIES) {
      /*
        For each <code>Category</code>, query all <code>Item</code> instances with
        an <code>OPTIMISTIC</code> lock mode. Hibernate now knows it has to
        check each <code>Item</code> at flush time.
      */
      List<Item> items =
          em.createQuery("select i from Item i where i.category.id = :catId", Item.class)
              .setLockMode(LockModeType.OPTIMISTIC)
              .setParameter("catId", categoryId)
              .getResultList();
      for (Item item : items) {
        totalPrice = totalPrice.add(item.getBuyNowPrice());

//        if (item.getId().equals(testData.items.getFirstId())) {
//          item.setBuyNowPrice(BigDecimal.valueOf(1));
//        }
        }


      // Now a concurrent transaction will move an item to another category
      if (categoryId.equals(testData.categories.getFirstId())) {
        Executors.newSingleThreadExecutor()
            .submit(
                () -> {
                  try {
                    EntityManager em2 = emf.createEntityManager();
                    em2.getTransaction().begin();

                    // Moving the first item from the first category into the last category
                    List<Item> items1 =
                        em2.createQuery(
                                "select i from Item i where i.category.id = :catId", Item.class)
                            .setParameter("catId", testData.categories.getFirstId())
                            .getResultList();

                    Category lastCategory =
                        em2.getReference(Category.class, testData.categories.getLastId());

                    items1.iterator().next().setCategory(lastCategory);

                    em2.getTransaction().commit();
                    em2.close();
                  } catch (Exception ex) {
                    // this should
                    throw new RuntimeException("Concurrent operation failure: " + ex, ex);
                  }
                  return null;
                })
            .get();
      }
    }

    em.getTransaction().commit();
    em.close();
    assertEquals("108.00", totalPrice.toString());

    em = emf.createEntityManager();
    em.getTransaction().begin();

    List<Item> allItemsFirstCategory =
        em.createQuery("select i from Item i where i.category.id = :catId", Item.class)
            .setParameter("catId", testData.categories.getFirstId())
            .getResultList();

    em.getTransaction().commit();
    em.close();

    assertEquals(2, allItemsFirstCategory.size());
  }
}
