package com.manning.javapersistence.ch08.persistentList;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Experiment: persistent list of strings with @OrderColumn.
 * Item.images is a List<String>, so Hibernate stores the element position in
 * an extra IMAGES_ORDER column and can read the list back in the same order.
 *
 * setUp() seeds the default records: a.jpg, b.jpg, c.jpg.
 * Add your own test cases below (e.g. remove "b.jpg" from the middle).
 */
public class ElementCollectionPersistentListExperiment {

    private EntityManagerFactory emf;
    private Long itemId;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("ch08-list");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Item item = new Item("Foo", LocalDate.now().plusDays(7));
        item.getImages().add("a.jpg");
        item.getImages().add("b.jpg");
        item.getImages().add("c.jpg");

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
    void loadItemKeepsOrder() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        System.out.println(">>> LOADED: " + item.getImages());

        em.getTransaction().commit();
        em.close();
    }

    // TODO: them cac truong hop con lai, vi du xoa "b.jpg" o giua list.
    @Test
    void deleteImageKeepOrder() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        System.out.println(">>> LOADED: " + item.getImages());

        em.getTransaction().commit();

        em = emf.createEntityManager();
        em.getTransaction().begin();

        item = em.find(Item.class, itemId);
        System.out.println(">>> DELETE IMAGES: ");
        item.getImages().remove("a.jpg");
        em.getTransaction().commit();
        em.close();
    }

    @Test 
    void giveNullValueIfHasGap() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Item item = em.find(Item.class, itemId);
        System.out.println(">>> LOADED: " + item.getImages());
        em.getTransaction().commit();

        System.out.println(">>> MAKE A GAP BETWEEN IMAGE");
        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery(
            "delete from IMAGE where ITEM_ID = ? and IMAGES_ORDER = 1"
        )
        .setParameter(1, itemId)
        .executeUpdate();
        em.clear(); // bỏ cache tầng 1 để buộc đọc lại từ DB

        item = em.find(Item.class, itemId);
        System.out.println(">>> -" + item.getImages());

        em.getTransaction().commit();
        em.close();


    }
}
