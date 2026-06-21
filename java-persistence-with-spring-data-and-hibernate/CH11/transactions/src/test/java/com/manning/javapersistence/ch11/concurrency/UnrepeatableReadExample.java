package com.manning.javapersistence.ch11.concurrency;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Demonstrates <strong>non-repeatable reads</strong> — one of the classic transaction
 * isolation phenomena.
 *
 * <h2>What is a non-repeatable read?</h2>
 * Within a single transaction, you read the same row twice and get different values
 * because another transaction committed a change between the two reads.
 *
 * <pre>
 *   Time ──────────────────────────────────────────────────────────────►
 *
 *   TX A (reader)     BEGIN ── SELECT price=100 ── (still open)
 *   TX B (writer)              BEGIN ── UPDATE price=250 ── COMMIT
 *   TX A (reader)                              SELECT price=250  ◄── changed!
 *   TX A (reader)     COMMIT
 * </pre>
 *
 * Unlike a dirty read, the value B wrote was committed — it is real data. The problem is
 * that TX A's two reads of the "same" data are inconsistent with each other.
 *
 * <h2>When can it happen?</h2>
 * At {@link Connection#TRANSACTION_READ_COMMITTED READ COMMITTED} and below. It is
 * prevented at {@link Connection#TRANSACTION_REPEATABLE_READ REPEATABLE READ} and
 * {@link Connection#TRANSACTION_SERIALIZABLE SERIALIZABLE}.
 *
 * <h2>Hibernate note</h2>
 * {@link EntityManager#find} returns entities from the first-level cache on subsequent
 * calls within the same transaction. This example calls {@link EntityManager#clear()}
 * before the second read so Hibernate issues a fresh SQL {@code SELECT} and the database
 * isolation level — not the cache — decides what value is visible.
 */
public class UnrepeatableReadExample {

  private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch11");

  /** Persists a catalog price and returns its generated id. */
  Long persistCatalogPrice(String productName, int price) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    CatalogPrice catalogPrice = new CatalogPrice(productName, price);
    em.persist(catalogPrice);
    em.getTransaction().commit();
    Long id = catalogPrice.getId();
    em.close();
    return id;
  }

  /** Reads committed price directly from the database (outside any test transaction). */
  int readCommittedPrice(Long catalogPriceId) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    int price = em.find(CatalogPrice.class, catalogPriceId).getPrice();
    em.getTransaction().commit();
    em.close();
    return price;
  }

  /**
   * With READ COMMITTED, a reader can see different values for the same row within one
   * transaction after another transaction commits an update.
   */
  @Test
  void nonRepeatableReadWithReadCommitted() throws Exception {
    final Long catalogPriceId = persistCatalogPrice("Widget", 100);

    CountDownLatch readerFirstRead = new CountDownLatch(1);
    CountDownLatch writerCommitted = new CountDownLatch(1);
    AtomicInteger firstRead = new AtomicInteger(-1);
    AtomicInteger secondRead = new AtomicInteger(-1);

    // TX B: wait until the reader has read once, then update and commit
    Executors.newSingleThreadExecutor()
        .submit(
            () -> {
              EntityManager em = emf.createEntityManager();
              try {
                readerFirstRead.await(5, TimeUnit.SECONDS);
                em.getTransaction().begin();
                CatalogPrice catalogPrice = em.find(CatalogPrice.class, catalogPriceId);
                catalogPrice.setPrice(250);
                em.getTransaction().commit();
                writerCommitted.countDown();
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              } finally {
                em.close();
              }
              return null;
            });

    // TX A: read twice in one transaction while another TX commits a change in between
    EntityManager readerEm = emf.createEntityManager();
    Session readerSession = readerEm.unwrap(Session.class);
    readerSession.doWork(
        connection -> connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED));

    readerEm.getTransaction().begin();
    firstRead.set(readerEm.find(CatalogPrice.class, catalogPriceId).getPrice());
    readerFirstRead.countDown();

    writerCommitted.await(5, TimeUnit.SECONDS);

    readerEm.clear(); // force a new SELECT so isolation level applies, not L1 cache
    secondRead.set(readerEm.find(CatalogPrice.class, catalogPriceId).getPrice());
    readerEm.getTransaction().commit();
    readerEm.close();

    assertEquals(100, firstRead.get(), "First read sees the original committed price");
    assertEquals(
        250,
        secondRead.get(),
        "READ COMMITTED allows a second read to see another transaction's committed UPDATE");
    assertNotEquals(
        firstRead.get(),
        secondRead.get(),
        "Same transaction, same row — two different values (non-repeatable read)");
    assertEquals(250, readCommittedPrice(catalogPriceId));
  }

  /**
   * MySQL InnoDB default (REPEATABLE READ) keeps a consistent snapshot for the lifetime
   * of the transaction, so both reads return the same value even after another TX commits.
   */
  @Test
  void repeatableReadWithDefaultIsolation() throws Exception {
    final Long catalogPriceId = persistCatalogPrice("Gadget", 100);

    CountDownLatch readerFirstRead = new CountDownLatch(1);
    CountDownLatch writerCommitted = new CountDownLatch(1);
    AtomicInteger firstRead = new AtomicInteger(-1);
    AtomicInteger secondRead = new AtomicInteger(-1);

    Executors.newSingleThreadExecutor()
        .submit(
            () -> {
              EntityManager em = emf.createEntityManager();
              try {
                readerFirstRead.await(5, TimeUnit.SECONDS);
                em.getTransaction().begin();
                CatalogPrice catalogPrice = em.find(CatalogPrice.class, catalogPriceId);
                catalogPrice.setPrice(250);
                em.getTransaction().commit();
                writerCommitted.countDown();
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              } finally {
                em.close();
              }
              return null;
            });

    EntityManager readerEm = emf.createEntityManager();
    // No isolation override — uses the connection default (REPEATABLE READ on MySQL InnoDB)
    readerEm.getTransaction().begin();
    firstRead.set(readerEm.find(CatalogPrice.class, catalogPriceId).getPrice());
    readerFirstRead.countDown();

    writerCommitted.await(5, TimeUnit.SECONDS);

    readerEm.clear();
    secondRead.set(readerEm.find(CatalogPrice.class, catalogPriceId).getPrice());
    readerEm.getTransaction().commit();
    readerEm.close();

    assertEquals(100, firstRead.get());
    assertEquals(
        100,
        secondRead.get(),
        "REPEATABLE READ must return the same snapshot for every read in the transaction");
    assertEquals(
        firstRead.get(),
        secondRead.get(),
        "Both reads in one transaction must agree under REPEATABLE READ");
    assertEquals(
        250,
        readCommittedPrice(catalogPriceId),
        "The writer's change is visible only after the reader's transaction ends");
  }
}
