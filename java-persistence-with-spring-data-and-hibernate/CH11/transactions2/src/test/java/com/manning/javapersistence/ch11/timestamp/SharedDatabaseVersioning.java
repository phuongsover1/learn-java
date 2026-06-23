package com.manning.javapersistence.ch11.timestamp;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Illustrates "versioning with a shared database" from the book.
 *
 * <p>
 * Imagine two applications using the same {@code SHARED_ITEM} table:
 * <ul>
 * <li><b>Hibernate app</b> – uses {@code @Version}; every UPDATE includes
 * {@code WHERE version = ?} and increments version.</li>
 * <li><b>Legacy app</b> – plain JDBC/SQL; updates {@code name} only and
 * knows nothing about the version column.</li>
 * </ul>
 *
 * <p>
 * Without a database trigger, the legacy update leaves version unchanged.
 * Hibernate still believes it has the current version, its UPDATE matches, and
 * you get a silent lost update. With the trigger, any UPDATE that does not
 * advance version is corrected in the database, and Hibernate detects the
 * conflict.
 */
public class SharedDatabaseVersioning {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch11");

    @BeforeEach
    @AfterEach
    void removeTrigger() {
        runJdbc(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DROP TRIGGER IF EXISTS shared_item_version_trigger");
            }
        });
    }

    @Test
    void withoutTrigger_legacyUpdateBypassesOptimisticLocking() {
        Long itemId = persistSharedItem("Original");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        SharedItem item = em.find(SharedItem.class, itemId);
        assertEquals(0L, item.getVersion());

        // Legacy application: no version column in the UPDATE
        legacyAppUpdateName(itemId, "Changed by legacy app");

        item.setName("Changed by Hibernate app");
        em.getTransaction().commit();
        em.close();

        SharedItem result = reload(itemId);
        // Hibernate "won" even though legacy changed the row first — lost update
        assertEquals("Changed by Hibernate app", result.getName());
        assertEquals(1L, result.getVersion());
    }

    @Test
    void withTrigger_legacyUpdateIncrementsVersionAndHibernateFails() throws IOException {
        installVersionTrigger();

        Long itemId = persistSharedItem("Original");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        SharedItem item = em.find(SharedItem.class, itemId);
        assertEquals(0L, item.getVersion());

        legacyAppUpdateName(itemId, "Changed by legacy app");

        // Legacy change bumped version via trigger; Hibernate still holds version 0
        assertEquals(1L, currentVersion(itemId));

        item.setName("Changed by Hibernate app");
        assertThrows(OptimisticLockException.class, em::flush);

        em.getTransaction().rollback();
        em.close();

        SharedItem result = reload(itemId);
        assertEquals("Changed by legacy app", result.getName());
        assertEquals(1L, result.getVersion());
    }

    private Long persistSharedItem(String name) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        SharedItem item = new SharedItem(name);
        em.persist(item);
        em.getTransaction().commit();
        Long id = item.getId();
        em.close();
        return id;
    }

    private SharedItem reload(Long id) {
        EntityManager em = emf.createEntityManager();
        SharedItem item = em.find(SharedItem.class, id);
        em.close();
        return item;
    }

    private long currentVersion(Long id) {
        return runJdbc(connection -> {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT version FROM SHARED_ITEM WHERE id = ?")) {
                ps.setLong(1, id);
                try (var rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getLong(1);
                }
            }
        });
    }

    /** Simulates a second application that does not use Hibernate versioning. */
    private void legacyAppUpdateName(Long id, String name) {
        runJdbc(connection -> {
            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE SHARED_ITEM SET name = ? WHERE id = ?")) {
                ps.setString(1, name);
                ps.setLong(2, id);
                ps.executeUpdate();
            }
        });
    }

    private void installVersionTrigger() throws IOException {
        String createTriggerSql = loadCreateTriggerSql();
        runJdbc(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DROP TRIGGER IF EXISTS shared_item_version_trigger");
                // Must be one statement: the trigger body contains its own semicolons.
                statement.execute(createTriggerSql);
            }
        });
    }

    private String loadCreateTriggerSql() throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/db/shared_item_version_trigger.sql")) {
            if (in == null) {
                throw new IllegalStateException("Missing /db/shared_item_version_trigger.sql");
            }
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            String upper = sql.toUpperCase();
            int start = upper.indexOf("CREATE TRIGGER");
            if (start < 0) {
                throw new IllegalStateException("No CREATE TRIGGER in shared_item_version_trigger.sql");
            }
            return sql.substring(start).trim().replaceFirst(";\\s*$", "");
        }
    }

    private void runJdbc(JdbcWork work) {
        EntityManager em = emf.createEntityManager();
        try {
            em.unwrap(Session.class).doWork(work::execute);
        } finally {
            em.close();
        }
    }

    private <T> T runJdbc(JdbcReturningWork<T> work) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.unwrap(Session.class).doReturningWork(work::execute);
        } finally {
            em.close();
        }
    }

    @FunctionalInterface
    private interface JdbcWork {
        void execute(Connection connection) throws SQLException;
    }

    @FunctionalInterface
    private interface JdbcReturningWork<T> {
        T execute(Connection connection) throws SQLException;
    }
}
