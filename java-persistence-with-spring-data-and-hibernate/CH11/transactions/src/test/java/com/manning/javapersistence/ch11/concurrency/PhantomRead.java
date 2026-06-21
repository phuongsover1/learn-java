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
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Demonstrates <strong>phantom reads</strong> — one of the classic transaction isolation
 * phenomena.
 *
 * <h2>What is a phantom read?</h2>
 * Within a single transaction, you run the same <em>search</em> twice (a range query,
 * {@code COUNT}, or any predicate-based read) and get a different number of rows because
 * another transaction committed an {@code INSERT} or {@code DELETE} between the two reads.
 *
 * <pre>
 *   Time ──────────────────────────────────────────────────────────────►
 *
 *   TX A (reader)     BEGIN ── COUNT(*) WHERE price≥100 → 2 ── (still open)
 *   TX B (writer)              BEGIN ── INSERT price=180 ── COMMIT
 *   TX A (reader)                              COUNT(*) → 3  ◄── phantom row!
 *   TX A (reader)     COMMIT
 * </pre>
 *
 * Unlike a non-repeatable read (same row, different column value after an {@code UPDATE}),
 * a phantom read is about rows that appear or disappear in a result set. The "phantom" is a
 * new row that was not there on the first read but matches the same query on the second.
 *
 * <h2>When can it happen?</h2>
 * At {@link Connection#TRANSACTION_READ_COMMITTED READ COMMITTED} and below. The SQL
 * standard allows phantom reads at {@link Connection#TRANSACTION_REPEATABLE_READ REPEATABLE
 * READ} too, but MySQL InnoDB (this project's default) uses MVCC snapshots and gap locking
 * that prevent them — the second test shows that behavior.
 *
 * <h2>Hibernate note</h2>
 * {@link EntityManager#clear()} is called before the second query so Hibernate does not
 * reuse cached query results from the first-level cache.
 */
public class PhantomRead {

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

  /** Counts rows matching the price threshold in a fresh transaction. */
  long countPricesAtOrAbove(int minPrice) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    long count = countPricesAtOrAbove(em, minPrice);
    em.getTransaction().commit();
    em.close();
    return count;
  }

  long countPricesAtOrAbove(EntityManager em, int minPrice) {
    return em.createQuery(
            "select count(cp) from CatalogPrice cp where cp.price >= :minPrice", Long.class)
        .setParameter("minPrice", minPrice)
        .getSingleResult();
  }

  /**
   * With READ COMMITTED, a reader can see new rows on a second search after another
   * transaction commits an INSERT that matches the same predicate.
   */
  @Test
  void phantomReadWithReadCommitted() throws Exception {
    persistCatalogPrice("Widget A", 150);
    persistCatalogPrice("Widget B", 200);
    final int minPrice = 100;

    CountDownLatch readerFirstQuery = new CountDownLatch(1);
    CountDownLatch writerCommitted = new CountDownLatch(1);
    AtomicLong firstCount = new AtomicLong(-1);
    AtomicLong secondCount = new AtomicLong(-1);

    // TX B: wait until the reader has queried once, then insert a matching row and commit
    Executors.newSingleThreadExecutor()
        .submit(
            () -> {
              EntityManager em = emf.createEntityManager();
              try {
                readerFirstQuery.await(5, TimeUnit.SECONDS);
                em.getTransaction().begin();
                em.persist(new CatalogPrice("Widget C", 180));
                em.getTransaction().commit();
                writerCommitted.countDown();
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              } finally {
                em.close();
              }
              return null;
            });

    // TX A: run the same COUNT twice while another TX inserts a matching row in between
    EntityManager readerEm = emf.createEntityManager();
    Session readerSession = readerEm.unwrap(Session.class);
    readerSession.doWork(
        connection -> connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED));

    readerEm.getTransaction().begin();
    firstCount.set(countPricesAtOrAbove(readerEm, minPrice));
    readerFirstQuery.countDown();

    writerCommitted.await(5, TimeUnit.SECONDS);

    readerEm.clear();
    secondCount.set(countPricesAtOrAbove(readerEm, minPrice));
    readerEm.getTransaction().commit();
    readerEm.close();

    assertEquals(2, firstCount.get(), "First search sees the two seeded rows");
    assertEquals(
        3,
        secondCount.get(),
        "READ COMMITTED allows a second search to see another transaction's committed INSERT");
    assertNotEquals(
        firstCount.get(),
        secondCount.get(),
        "Same transaction, same query — two different row counts (phantom read)");
    assertEquals(3, countPricesAtOrAbove(minPrice));
  }

  /**
   * MySQL InnoDB default (REPEATABLE READ) keeps a consistent snapshot for the lifetime
   * of the transaction, so both searches return the same count even after another TX
   * commits an INSERT that would match the predicate.
   */
  @Test
  void noPhantomReadWithDefaultIsolation() throws Exception {
    persistCatalogPrice("Gadget A", 150);
    persistCatalogPrice("Gadget B", 200);
    final int minPrice = 100;

    CountDownLatch readerFirstQuery = new CountDownLatch(1);
    CountDownLatch writerCommitted = new CountDownLatch(1);
    AtomicLong firstCount = new AtomicLong(-1);
    AtomicLong secondCount = new AtomicLong(-1);

    Executors.newSingleThreadExecutor()
        .submit(
            () -> {
              EntityManager em = emf.createEntityManager();
              try {
                readerFirstQuery.await(5, TimeUnit.SECONDS);
                em.getTransaction().begin();
                em.persist(new CatalogPrice("Gadget C", 180));
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
    firstCount.set(countPricesAtOrAbove(readerEm, minPrice));
    readerFirstQuery.countDown();

    writerCommitted.await(5, TimeUnit.SECONDS);

    readerEm.clear();
    secondCount.set(countPricesAtOrAbove(readerEm, minPrice));
    readerEm.getTransaction().commit();
    readerEm.close();

    assertEquals(2, firstCount.get());
    assertEquals(
        2,
        secondCount.get(),
        "REPEATABLE READ must return the same snapshot for every search in the transaction");
    assertEquals(
        firstCount.get(),
        secondCount.get(),
        "Both searches in one transaction must agree under REPEATABLE READ");
    assertEquals(
        3,
        countPricesAtOrAbove(minPrice),
        "The writer's row is visible only after the reader's transaction ends");
  }
}
