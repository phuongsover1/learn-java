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
 * Same setup as SubselectFetchExperiment, but the owning query is now
 * paginated with ORDER BY auctionEnd + setMaxResults(5).
 *
 * Question: when we touch .getBids() on one of the 5 returned items,
 * does the SUBSELECT fetch profile correctly replay "the same 5 rows",
 * or does embedding ORDER BY/LIMIT inside IN (subquery) change the result?
 */
public class SubselectWithLimitExperiment {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12");

    private void setUpTenItemsWithBids() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        User seller = new User("johndoe");
        em.persist(seller);
        User bidder = new User("janeroe");
        em.persist(bidder);

        for (int i = 1; i <= 10; i++) {
            Item item = new Item("Item " + i, LocalDate.now().plusDays(i), seller);
            em.persist(item);
            Bid bid = new Bid(item, bidder, new BigDecimal(10 + i));
            item.addBid(bid);
            em.persist(bid);
        }

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void subselectWithPaginatedOwningQuery() {
        setUpTenItemsWithBids();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== LOADING ONLY 5 OF 10 ITEMS (ORDER BY auctionEnd, LIMIT 5) ===");
        Query query = em.createQuery(
                "select i from ItemWithSubselect i where i.auctionEnd > :date order by i.auctionEnd");
        query.setParameter("date", LocalDate.now());
        query.setMaxResults(5);

        @SuppressWarnings("unchecked")
        List<ItemWithSubselect> items = query.getResultList();
        System.out.println("Loaded " + items.size() + " items (expected 5, the earliest-ending ones).");
        for (ItemWithSubselect item : items) {
            System.out.println("  loaded: " + item.getName() + " auctionEnd=" + item.getAuctionEnd());
        }

        System.out.println("\n=== ACCESSING .getBids() ON JUST THE FIRST LOADED ITEM ===");
        System.out.println("Watch the subselect SQL below: does it contain ORDER BY / LIMIT,");
        System.out.println("and does bid count end up matching 5 items or all 10?\n");

        int totalBidsSeen = 0;
        for (ItemWithSubselect item : items) {
            totalBidsSeen += item.getBids().size();
        }
        System.out.println("\nTotal bids seen across the 5 loaded items: " + totalBidsSeen
                + " (expected 5 if subselect correctly scoped to just these 5 items).");

        em.getTransaction().commit();
        em.close();
    }
}
