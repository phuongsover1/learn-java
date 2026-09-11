package com.manning.javapersistence.ch08.collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * No mapping between User, Item, and Bid yet.
 * Each INSERT below is independent — nothing here joins them together.
 * This is the baseline to compare against once a collection mapping
 * (Set/Bag/List/Map) is added on top of Item and Bid.
 */
public class BasicEntitiesExperiment {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("ch08");
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    void tearDown() {
        tx.rollback();
        em.close();
        emf.close();
    }

    @Test
    void persistIndependentEntities() {
        User seller = new User("frank");
        Item item = new Item("Foo", LocalDate.now().plusDays(7));
        Bid bid = new Bid(new BigDecimal("100.00"));

        em.persist(seller);
        em.persist(item);
        em.persist(bid);

        em.flush();
    }
}
