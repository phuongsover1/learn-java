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
 * Item.bids is plain @OneToMany(mappedBy = "item") -> default FetchType.LAZY,
 * no @Fetch annotation. Nothing on the mapping forces bids to be loaded.
 *
 * Two Items exist: one WITH a Bid, one WITHOUT any Bid.
 *
 * "Dynamic eager fetching" means the decision to eager-fetch bids is made
 * per QUERY (join fetch / left join fetch), not on the mapping. This
 * experiment shows the classic trap: inner join fetch silently drops
 * parent rows that have no matching child row.
 *
 * Each test sets up its own data inside its own transaction and rolls
 * that transaction back at the end, so the two tests never see each
 * other's rows regardless of run order.
 */
public class DynamicEagerFetchExperiment {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12");

    private void setUpOneItemWithBidAndOneWithout(EntityManager em) {
        User seller = new User("johndoe");
        em.persist(seller);
        User bidder = new User("janeroe");
        em.persist(bidder);

        Item itemWithBid = new Item("Item With Bid", LocalDate.now().plusDays(1), seller);
        em.persist(itemWithBid);
        Bid bid = new Bid(itemWithBid, bidder, new BigDecimal("15"));
        itemWithBid.addBid(bid);
        em.persist(bid);

        Item itemWithoutBid = new Item("Item Without Bid", LocalDate.now().plusDays(1), seller);
        em.persist(itemWithoutBid);

        em.flush();
    }

    @Test
    public void innerJoinFetchSilentlyDropsItemsWithNoBids() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        setUpOneItemWithBidAndOneWithout(em);

        System.out.println("\n=== join fetch i.bids (INNER JOIN FETCH) ===");
        System.out.println("2 Items exist in the DB: one with a Bid, one without.");
        System.out.println("Prediction: only 1 Item comes back - the one WITH a Bid.");
        System.out.println("The Item without a Bid has no matching Bid row, so INNER JOIN");
        System.out.println("eliminates it from the result set entirely.\n");

        Query query = em.createQuery(
                "select i from Item i join fetch i.bids");

        @SuppressWarnings("unchecked")
        List<Item> items = query.getResultList();

        System.out.println("\n=== RESULT ===");
        System.out.println("Items returned: " + items.size());
        for (Item item : items) {
            System.out.println(" - " + item.getName());
        }

        em.getTransaction().rollback();
        em.close();
    }

    @Test
    public void leftJoinFetchKeepsItemsWithNoBids() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        setUpOneItemWithBidAndOneWithout(em);

        System.out.println("\n=== left join fetch i.bids (LEFT OUTER JOIN FETCH) ===");
        System.out.println("Same 2 Items. Prediction: BOTH come back - LEFT JOIN keeps the");
        System.out.println("Item row even when there is no matching Bid row (bids = empty set).\n");

        Query query = em.createQuery(
                "select i from Item i left join fetch i.bids");

        @SuppressWarnings("unchecked")
        List<Item> items = query.getResultList();

        System.out.println("\n=== RESULT ===");
        System.out.println("Items returned: " + items.size());
        for (Item item : items) {
            System.out.println(" - " + item.getName() + " -> " + item.getBids().size() + " bid(s)");
        }

        em.getTransaction().rollback();
        em.close();
    }
}
