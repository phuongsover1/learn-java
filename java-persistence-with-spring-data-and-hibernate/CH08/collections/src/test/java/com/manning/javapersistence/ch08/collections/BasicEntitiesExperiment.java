package com.manning.javapersistence.ch08.collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private Long itemId;
    private Long itemId2;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("ch08");
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();

        User seller = new User("frank");
        Item item = new Item("Foo", LocalDate.now().plusDays(7));
        item.getImages().add("a.jpg");
        item.getImages().add("b.jpg");
        Bid bid = new Bid(new BigDecimal("100.00"));

        em.persist(seller);
        em.persist(item);
        em.persist(bid);

        em.flush();

        itemId = item.getId();

        tx.commit();
        em.close();
    }

    @Test
    void retrieveItemWithImage() {
        System.out.println("--- RETRIVE ITEM WITH IMAGE ---");
        em = emf.createEntityManager();
        em.getTransaction().begin();
        Item item = em.find(Item.class, itemId);

        em.getTransaction().commit();
        assertEquals("Foo", item.getName());
        assertEquals(2, item.getImages().size());
        assertTrue(item.getImages().contains("a.jpg"));
        assertTrue(item.getImages().contains("b.jpg"));

    }

    @Test
    void retriveMultipleItemsWithImage() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        Item item2 = new Item("Bar", LocalDate.now().plusDays(7));
        item2.getImages().add("a.jpg");
        item2.getImages().add("b.jpg");
        em.persist(item2);
        itemId2 = item2.getId();
        em.getTransaction().commit();
        em.close();

        System.out.println("--- RETRIVE ITEMS WITH IMAGE ---");
        em = emf.createEntityManager();
        em.getTransaction().begin();
        Item retriveI1 = em.find(Item.class, itemId);
        Item retriveI2 = em.find(Item.class, itemId2);

        assertEquals("Foo", retriveI1.getName());
        assertEquals("Bar", retriveI2.getName());
        assertEquals(2, retriveI1.getImages().size());
        assertEquals(2, retriveI2.getImages().size());

        em.getTransaction().commit();
        em.close();
    }
}
