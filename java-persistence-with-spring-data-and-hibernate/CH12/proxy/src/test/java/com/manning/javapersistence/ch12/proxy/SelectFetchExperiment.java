package com.manning.javapersistence.ch12.proxy;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 3 Items, each with 1 Bid.
 * ItemWithSelectFetch.bids has FetchType.EAGER + @Fetch(FetchMode.SELECT), so
 * Hibernate must fetch bids for every item as soon as the item is loaded -
 * no lazy proxy, no access needed. We never call .getBids() below; the
 * SQL for bids should still appear right after the item query, one
 * "select ... from Bid where item_id = ?" per item (N+1), fired while
 * Hibernate is still processing the first query's result set.
 */
public class SelectFetchExperiment {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12");

    private void setUpThreeItemsWithBids() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        User seller = new User("johndoe");
        em.persist(seller);
        User bidder = new User("janeroe");
        em.persist(bidder);

        for (int i = 1; i <= 3; i++) {
            Item item = new Item("Item " + i, LocalDate.now().plusDays(1), seller);
            em.persist(item);
            Bid bid = new Bid(item, bidder, new BigDecimal(10 + i));
            item.addBid(bid);
            em.persist(bid);
        }

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void eagerSelectFetchesEachParentsBidsImmediately() {
        setUpThreeItemsWithBids();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== LOADING 3 ITEMS WITH ONE QUERY ===");
        System.out.println("Prediction: for EACH item row returned, Hibernate immediately");
        System.out.println("fires 'select ... from Bid where item_id = ?' - BEFORE control");
        System.out.println("returns to this test. Total: 1 (items) + 3 (bids) = 4 queries.");
        System.out.println("We do NOT call .getBids() below - eager loading happens anyway.\n");

        Query query = em.createQuery(
                "select i from ItemWithSelectFetch i where i.auctionEnd > :date");
        query.setParameter("date", LocalDate.now());

        @SuppressWarnings("unchecked")
        List<ItemWithSelectFetch> items = query.getResultList();

        System.out.println("\n=== BACK IN THE TEST - bids should ALREADY be initialized ===");
        for (ItemWithSelectFetch item : items) {
            System.out.println(item.getName() + " -> " + item.getBids().size() + " bid(s), "
                    + "isInitialized should be true since fetch is EAGER");
        }

        em.getTransaction().commit();
        em.close();
    }
}
