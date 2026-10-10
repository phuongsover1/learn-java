package com.manning.javapersistence.ch08.sortedmapofstrings;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Experiment: @ElementCollection with SortedMap<String, String> and
 * @SortComparator(ReverseStringComparator.class) — Listing 8.7.
 *
 * Item.images is a SortedMap keyed by filename (FILENAME) with an image name
 * value (IMAGENAME). The mapping to the IMAGE table is EXACTLY the same as the
 * plain Map experiment in ch08.mapofstrings — no extra column, no ORDER BY.
 * The ordering is applied IN MEMORY by Hibernate after loading the rows.
 *
 * setUp() seeds two entries: foo.jpg -> Foo Photo, bar.png -> Bar Photo.
 * Add your own test cases below.
 */
public class ElementCollectionSortedMapOfStringsExperiment {

    private EntityManagerFactory emf;
    private Long itemId;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("ch08-sortedmap");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Item item = new Item("Foo", LocalDate.now().plusDays(7));
        item.getImages().put("bar.png", "Bar Photo");
        item.getImages().put("foo.jpg", "Foo Photo");

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
    void loadItemShowsSortedMap() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        System.out.println(">>> LOADED (reverse order by key): " + item.getImages());

        em.getTransaction().commit();
        assertAll(
            () -> assertEquals("foo.jpg", item.getImages().firstKey()),
            () -> assertEquals("bar.png", item.getImages().lastKey())
        );
        em.close();
    }

    // TODO: Xoa @SortComparator khoi Item, chay lai, so sanh thu tu. -> loi, phai them vao @naturalOrder
    //  Du doan: comparator khong con -> thu tu phu thuoc TreeMap natural order
    //  (bar.png truoc foo.jpg) hoac DB neu khai bao Map thuong.
    @Test
    void orderWithoutComparator() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        System.out.println(">>> LOADED: " + item.getImages());
        System.out.println(">>> FIRST KEY VISIBLE TO CALLER: " + item.getImages().firstKey());

        em.getTransaction().commit();
        em.close();
    }
}
