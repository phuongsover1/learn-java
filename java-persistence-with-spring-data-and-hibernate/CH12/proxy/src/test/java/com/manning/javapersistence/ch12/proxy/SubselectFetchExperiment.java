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
 * 10 Items (all with auctionEnd in the future), each with 1 Bid.
 * ItemWithSubselect.bids has @Fetch(FetchMode.SUBSELECT), so touching
 * .getBids() on the FIRST item should re-run the original query's
 * WHERE clause as a subquery and fetch bids for ALL 10 items at once -
 * even though we only ever call .getBids() on the first 3.
 */
public class SubselectFetchExperiment {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12");

    private void setUpTenItemsWithBids() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        User seller = new User("johndoe");
        em.persist(seller);
        User bidder = new User("janeroe");
        em.persist(bidder);

        for (int i = 1; i <= 10; i++) {
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
    public void subselectFetchesAllParentsOnFirstAccess() {
        setUpTenItemsWithBids();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== LOADING 10 ITEMS WITH ONE QUERY (WHERE clause) ===");
        Query query = em.createQuery(
                "select i from ItemWithSubselect i where i.auctionEnd > :date");
        query.setParameter("date", LocalDate.now());

        @SuppressWarnings("unchecked")
        List<ItemWithSubselect> items = query.getResultList();
        System.out.println("Loaded " + items.size() + " items with a single SELECT.");

        System.out.println("\n=== ACCESSING .getBids() ON ONLY THE FIRST 3 ITEMS ===");
        System.out.println("Prediction: touching item #1's bids re-runs the original");
        System.out.println("WHERE auctionEnd > ? clause as a subquery, fetching bids");
        System.out.println("for ALL 10 items in one query - not just 3.\n");

        for (int i = 0; i < 3; i++) {
            ItemWithSubselect item = items.get(i);
            int count = item.getBids().size();
            System.out.println(item.getName() + " -> " + count + " bid(s)");
        }

        System.out.println("\nOnly ONE 'select ... from Bid ... where item_id in (select ...)' should appear above,");
        System.out.println("even though we only touched 3 of the 10 items.");

        em.getTransaction().commit();
        em.close();
    }
}
