package com.manning.javapersistence.ch12.proxy;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 10 Items, each with a few Bids. ItemWithBatchSize.bids has
 * @BatchSize(size = 5), so accessing .getBids() on 10 items
 * should trigger 2 batched SELECT ... WHERE item_id IN (...) queries
 * instead of 10 separate ones (N+1).
 */
public class BatchSizeExperiment {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12");

    private List<Long> setUpTenItemsWithBids() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        User seller = new User("johndoe");
        em.persist(seller);
        User bidder = new User("janeroe");
        em.persist(bidder);

        List<Long> itemIds = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Item item = new Item("Item " + i, LocalDate.now().plusDays(1), seller);
            em.persist(item);
            Bid bid = new Bid(item, bidder, new BigDecimal(10 + i));
            item.addBid(bid);
            em.persist(bid);
            itemIds.add(item.getId());
        }

        em.getTransaction().commit();
        em.close();
        return itemIds;
    }

    @Test
    public void batchSizeGroupsLazyCollectionLoads() {
        List<Long> itemIds = setUpTenItemsWithBids();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== LOADING 10 ITEMS (no fetch join, bids stay lazy) ===");
        List<ItemWithBatchSize> items = new ArrayList<>();
        for (Long id : itemIds) {
            items.add(em.find(ItemWithBatchSize.class, id));
        }

        System.out.println("\n=== ACCESSING .getBids() ON EACH ITEM (@BatchSize(size = 5)) ===");
        System.out.println("Expect: batched 'WHERE item_id IN (...)' queries, 5 ids at a time\n");
        for (ItemWithBatchSize item : items) {
            int count = item.getBids().size();
            System.out.println(item.getName() + " -> " + count + " bid(s)");
        }

        System.out.println("\nCount the 'select ... from Bid ... where item_id in (...)' statements above:");
        System.out.println("Prediction: 2 batched queries (5 items each) instead of 10 individual ones.");

        em.getTransaction().commit();
        em.close();
    }
}
