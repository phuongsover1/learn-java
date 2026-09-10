package com.manning.javapersistence.ch12.proxy;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Question: does enabling a named @FetchProfile on a Session collapse the
 * lazy "bids" association into a single LEFT OUTER JOIN, while a Session
 * that never enables it keeps behaving as plain LAZY (two SELECTs)?
 */
public class FetchProfileExperiment {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12");

    private Long setUpOneItemWithBids() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        User seller = new User("johndoe");
        em.persist(seller);
        User bidder = new User("janeroe");
        em.persist(bidder);

        Item item = new Item("Item One", LocalDate.now().plusDays(1), seller);
        em.persist(item);

        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(item, bidder, new BigDecimal(9 + i));
            item.addBid(bid);
            em.persist(bid);
        }

        em.getTransaction().commit();
        Long itemId = item.getId();
        em.close();
        return itemId;
    }

    @Test
    public void withoutFetchProfileStaysLazy() {
        Long itemId = setUpOneItemWithBids();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== NO enableFetchProfile() CALLED ===");
        System.out.println("Prediction: SELECT ItemWithFetchProfile alone, then a SEPARATE");
        System.out.println("SELECT Bid only when getBids() is accessed (2 SQL statements).\n");

        ItemWithFetchProfile item = em.find(ItemWithFetchProfile.class, itemId);

        System.out.println("\n--- about to access getBids(), watch for a second SELECT ---\n");
        System.out.println("Bids: " + item.getBids().size());

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void withFetchProfileJoinsBidsInOneSelect() {
        Long itemId = setUpOneItemWithBids();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== enableFetchProfile(\"item-with-bids\") CALLED ===");
        System.out.println("Prediction: ONE SELECT with a LEFT OUTER JOIN to Bid, bids already");
        System.out.println("initialized, no second SELECT when getBids() is accessed.\n");

        Session session = em.unwrap(Session.class);
        session.enableFetchProfile("item-with-bids");

        ItemWithFetchProfile item = session.get(ItemWithFetchProfile.class, itemId);

        System.out.println("\n--- about to access getBids(), watch for NO further SELECT ---\n");
        System.out.println("Bids: " + item.getBids().size());

        em.getTransaction().commit();
        em.close();
    }
}
