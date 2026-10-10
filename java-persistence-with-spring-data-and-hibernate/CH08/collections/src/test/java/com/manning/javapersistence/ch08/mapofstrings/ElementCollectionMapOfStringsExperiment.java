package com.manning.javapersistence.ch08.mapofstrings;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Experiment: @ElementCollection with Map<String, String>.
 * Item.images is a Map keyed by filename (FILENAME) with an image name value
 * (IMAGENAME). Hibernate stores each entry as a row (ITEM_ID, FILENAME, IMAGENAME)
 * with a composite primary key (ITEM_ID, FILENAME).
 *
 * setUp() seeds two entries: foo.jpg -> Foo Photo, bar.png -> Bar Photo.
 * Add your own test cases below.
 */
public class ElementCollectionMapOfStringsExperiment {

    private EntityManagerFactory emf;
    private Long itemId;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("ch08-map");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Item item = new Item("Foo", LocalDate.now().plusDays(7));
        item.getImages().put("foo.jpg", "Foo Photo");
        item.getImages().put("bar.png", "Bar Photo");

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
    void loadItemShowsMap() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        System.out.println(">>> LOADED: " + item.getImages());

        em.getTransaction().commit();
        em.close();
    }

    // TODO: put cung mot key hai lan (vi du "foo.jpg" -> "Other Photo").
    //  Du doan: bang IMAGE chi con MOT hang cho foo.jpg, value bi ghi de (note B).
    @Test
    void putSameKeyOverwritesValue() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        System.out.println(">>> BEFORE:" + item.getImages());
        System.out.println(">>> PUT IMAGE WITH DUPLICATE KEY");
        item.getImages().put("foo.jpg", "OTHER PHOTO");
        em.flush();

        em.clear();

        item = em.find(Item.class, itemId);        
        System.out.println(">>> AFTER PUT DUPLICATE KEY: " + item.getImages());
        em.getTransaction().commit();
        em.close();
    }

    // TODO: hai key khac nhau dung chung mot value (vi du "a.jpg" va "b.jpg" -> "Photo").
    //  Du doan: ca hai hang deu duoc giu, value trung nhau van OK.
    @Test
    void sameValueForDifferentKeysAllowed() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        System.out.println(">>> BEFORE:" + item.getImages());
        System.out.println(">>> PUT IMAGE WITH DIFFERENT KEY BUT SAME VALUE");
        item.getImages().put("other_foo.jpg", "Foo Photo");
        em.flush();

        em.clear();

        item = em.find(Item.class, itemId);
        System.out.println(">>> AFTER PUT DIFFERENT KEY, DUPLICATE VALUE: " + item.getImages());
        em.getTransaction().commit();
        em.close();
    }
}
