package com.manning.javapersistence.ch11.concurrency;

import org.hibernate.PessimisticLockException;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Locking extends Versioning {

    @Test
    public void pessimisticReadWrite() throws ExecutionException, InterruptedException {
        final ConcurrencyTestData testData = storeCategoriesAndItems();
        Long[] CATEGORIES = testData.categories.identifiers;

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Long categoryId : CATEGORIES) {
            /*
                   For each <code>Category</code>, query all <code>Item</code> instances in
                   <code>PESSIMISTIC_READ</code> lock mode. Hibernate will lock the rows in
                   the database with the SQL query. If possible, wait for 5 seconds if some
                   other transaction already holds a conflicting lock. If the lock can't
                   be obtained, the query throws an exception.
             */
            List<Item> items = em.createQuery("Select i from Item i where i.category.id = :catId", Item.class)
                    .setLockMode(LockModeType.PESSIMISTIC_READ)
                    .setHint("javax.persistence.lock.timeout", 5000)
                    .setParameter("catId", categoryId)
                    .getResultList();
            /*
                   If the query returns successfully, you know that you hold an exclusive lock
                   on the data and no other transaction can access it with an exclusive lock or
                   modify it until this transaction commits.
             */
            for (Item item : items)
                totalPrice = totalPrice.add(item.getBuyNowPrice());

            // Now a concurrent transaction will try to obtain a write lock, it fails because
            // we hold a read lock on the data already. Note that on H2 there actually are no
            // read or write locks, only exclusive locks.
            if (categoryId.equals(testData.categories.getFirstId())) {
                Executors.newSingleThreadExecutor().submit(() -> {
                    try {
                        EntityManager em1 = emf.createEntityManager();
                        em1.getTransaction().begin();

                        em1.unwrap(Session.class).doWork(connection -> {
                            switch (connection.getMetaData().getDatabaseProductName()) {
                                // - MySQL also doesn't support query lock timeouts, but you
                                //   can set a timeout for the whole connection/session.
                                case "MySQL":
                                    connection.createStatement().execute("set innodb_lock_wait_timeout = 5;");
                                    break;
                                // - PostgreSQL doesn't timeout and just hangs indefinitely if
                                //   NOWAIT isn't specified for the query. One possible way to
                                //   wait for a lock is to set a statement timeout for the whole
                                //   connection/session.
                                case "PostgreSQL":
                                    connection.createStatement().execute("set statement_timeout = 5000");
                                    break;
                            }
                        });

                        // Moving the first item from the first category into the last category
                        // This query should fail as someone else holds a lock on the rows
                        List<Item> itemsFirstCategory = em1.createQuery("select i from Item i where i.category.id = :catId", Item.class)
                                .setLockMode(LockModeType.PESSIMISTIC_WRITE) // Prevent concurrent access
                                .setHint("javax.persistence.lock.timeout", 5000) // Only works on Oracle...
                                .setParameter("catId", categoryId)
                                .getResultList();

                        Category lastCategory = em1.getReference(Category.class, testData.categories.getLastId());

                        itemsFirstCategory.iterator().next().setCategory(lastCategory);

                        em1.getTransaction().commit();
                        em1.close();
                    } catch (Exception ex) {
                        // This should fail, as the data is already locked!
                        Session session = em.unwrap(Session.class);
                        session.doWork(connection -> {
                            switch (connection.getMetaData().getDatabaseProductName()) {
                                // On MYSQL we get a LockTimeoutException
                                case "MySQL":
                                    assertTrue(ex instanceof LockTimeoutException);
                                    break;
                                // A statement timeout on PostgreSQL doesn't produce a specific exception
                                case "PostgreSQL":
                                    assertTrue(ex instanceof PersistenceException);
                                    break;
                                default:
                                    assertTrue(ex instanceof PessimisticLockException);
                                    break;
                            }
                        });
                    }
                    return null;
                }).get();
            }
        }

        // Our locks will be released after commit, when the transaction completes
        em.getTransaction().commit();
        em.close();

        assertEquals(0, totalPrice.compareTo(new BigDecimal("108")));
    }
}
