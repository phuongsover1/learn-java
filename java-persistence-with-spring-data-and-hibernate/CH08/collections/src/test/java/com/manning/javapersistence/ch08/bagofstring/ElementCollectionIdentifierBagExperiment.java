package com.manning.javapersistence.ch08.bagofstring;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Experiment: identifier bag of strings.
 * Item.images is a Collection<String> (a bag: duplicates allowed, no order),
 * but this bag carries a synthetic identifier column IMAGE_ID via @CollectionId.
 */
public class ElementCollectionIdentifierBagExperiment {

    private EntityManagerFactory emf;
    private Long itemId;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("ch08-bag");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Item item = new Item("Foo", LocalDate.now().plusDays(7));
        item.getImages().add("a.jpg");
        item.getImages().add("a.jpg");
        item.getImages().add("b.jpg");

        em.persist(item);
        em.flush();
        itemId = item.getId();

        tx.commit();
        em.close();
    }

    @AfterEach
    void tearDown() {
        emf.close();
    }

    @Test
    void addOneImageToExistingItem() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        System.out.println(">>> LOADED: " + item.getImages());

        item.getImages().add("c.jpg");
        System.out.println(">>> FLUSH after adding c.jpg <<<");
        em.flush();

        System.out.println(">>> AFTER FLUSH <<<");
        em.getTransaction().commit();
        em.close();
    }
}
