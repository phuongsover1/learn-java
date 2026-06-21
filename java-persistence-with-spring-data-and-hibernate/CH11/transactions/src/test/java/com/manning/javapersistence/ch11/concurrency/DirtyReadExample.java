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

/**
 * Demonstrates <strong>dirty reads</strong> — one of the classic transaction isolation
 * phenomena.
 *
 * <h2>What is a dirty read?</h2>
 * Transaction B reads data that transaction A has changed but not yet committed. If A
 * later rolls back, B read values that never actually existed in the database.
 *
 * <pre>
 *   Time ──────────────────────────────────────────────────────────────►
 *
 *   TX A (writer)     BEGIN ── UPDATE balance=500 ── (holds, no commit)
 *   TX B (reader)              BEGIN ── SELECT ── sees 500  ◄── dirty read
 *   TX A (writer)                              ROLLBACK
 *   Result in DB: balance is still 100, but B already acted on 500
 * </pre>
 *
 * <h2>When can it happen?</h2>
 * Only at {@link Connection#TRANSACTION_READ_UNCOMMITTED READ UNCOMMITTED} isolation.
 * MySQL InnoDB defaults to REPEATABLE READ, which prevents dirty reads — the second test
 * shows that behavior.
 *
 * <h2>Isolation levels (SQL standard)</h2>
 * <ul>
 *   <li>READ UNCOMMITTED — allows dirty reads, non-repeatable reads, phantom reads</li>
 *   <li>READ COMMITTED — prevents dirty reads</li>
 *   <li>REPEATABLE READ — prevents dirty + non-repeatable reads</li>
 *   <li>SERIALIZABLE — prevents all three phenomena</li>
 * </ul>
 */
public class DirtyReadExample {

  private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch11");

  /** Persists a wallet and returns its generated id. */
  Long persistWallet(String owner, int balance) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    Wallet wallet = new Wallet(owner, balance);
    em.persist(wallet);
    em.getTransaction().commit();
    Long id = wallet.getId();
    em.close();
    return id;
  }

  /** Reads committed balance directly from the database (outside any test transaction). */
  int readCommittedBalance(Long walletId) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    int balance = em.find(Wallet.class, walletId).getBalance();
    em.getTransaction().commit();
    em.close();
    return balance;
  }

  /**
   * With READ UNCOMMITTED, a concurrent reader sees the writer's uncommitted change.
   * After the writer rolls back, that value was never really stored.
   */
  @Test
  void dirtyReadWithReadUncommitted() throws Exception {
    final Long walletId = persistWallet("Alice", 100);

    CountDownLatch writerUpdated = new CountDownLatch(1);
    CountDownLatch readerFinished = new CountDownLatch(1);
    AtomicInteger balanceSeenByReader = new AtomicInteger(-1);

    // TX A: update balance but roll back instead of committing
    Executors.newSingleThreadExecutor()
        .submit(
            () -> {
              EntityManager em = emf.createEntityManager();
              try {
                em.getTransaction().begin();
                Wallet wallet = em.find(Wallet.class, walletId);
                wallet.setBalance(500);
                em.flush(); // send UPDATE to DB, but transaction stays open
                writerUpdated.countDown();
                readerFinished.await(5, TimeUnit.SECONDS);
                em.getTransaction().rollback(); // 500 never becomes permanent
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              } finally {
                em.close();
              }
              return null;
            });

    // TX B: deliberately use READ UNCOMMITTED so uncommitted data is visible
    writerUpdated.await(5, TimeUnit.SECONDS);

    EntityManager readerEm = emf.createEntityManager();
    Session readerSession = readerEm.unwrap(Session.class);
    readerSession.doWork(
        connection ->
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED));

    readerEm.getTransaction().begin();
    balanceSeenByReader.set(readerEm.find(Wallet.class, walletId).getBalance());
    readerEm.getTransaction().commit();
    readerEm.close();

    readerFinished.countDown();

    assertEquals(
        500,
        balanceSeenByReader.get(),
        "READ UNCOMMITTED allows reading another transaction's uncommitted UPDATE");
    assertEquals(
        100,
        readCommittedBalance(walletId),
        "After rollback the database still holds the original balance");
  }

  /**
   * MySQL InnoDB default (REPEATABLE READ) does not allow dirty reads.
   * The reader sees the last committed value even while another TX holds an uncommitted
   * update.
   */
  @Test
  void noDirtyReadWithDefaultIsolation() throws Exception {
    final Long walletId = persistWallet("Bob", 100);

    CountDownLatch writerUpdated = new CountDownLatch(1);
    CountDownLatch readerFinished = new CountDownLatch(1);
    AtomicInteger balanceSeenByReader = new AtomicInteger(-1);

    Executors.newSingleThreadExecutor()
        .submit(
            () -> {
              EntityManager em = emf.createEntityManager();
              try {
                em.getTransaction().begin();
                Wallet wallet = em.find(Wallet.class, walletId);
                wallet.setBalance(500);
                em.flush();
                writerUpdated.countDown();
                readerFinished.await(5, TimeUnit.SECONDS);
                em.getTransaction().rollback();
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              } finally {
                em.close();
              }
              return null;
            });

    writerUpdated.await(5, TimeUnit.SECONDS);

    EntityManager readerEm = emf.createEntityManager();
    // No isolation override — uses the connection default (REPEATABLE READ on MySQL InnoDB)
    readerEm.getTransaction().begin();
    balanceSeenByReader.set(readerEm.find(Wallet.class, walletId).getBalance());
    readerEm.getTransaction().commit();
    readerEm.close();

    readerFinished.countDown();

    assertEquals(
        100,
        balanceSeenByReader.get(),
        "Default isolation must not expose uncommitted data from another transaction");
    assertEquals(100, readCommittedBalance(walletId));
  }
}
