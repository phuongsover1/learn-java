package com.manning.javapersistence.ch12.proxy;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * ItemForEntityGraph.seller is mapped FetchType.EAGER (simulating a mapping
 * someone changed later). bids is LAZY. The named graph "Item.withBids"
 * lists only bids - seller is deliberately left out.
 * <p>
 * Prediction:
 * - plain find(): follows the mapping -> 1 query for Item that already
 * joins seller (EAGER), bids stays an uninitialized lazy proxy.
 * - fetchgraph: graph is the WHOLE plan -> bids becomes EAGER (join fetch),
 * seller is forced LAZY even though its mapping says EAGER.
 * - loadgraph: graph only ADDS to the mapping -> bids becomes EAGER (join),
 * seller keeps following its own mapping -> stays EAGER too, same as plain
 * find().
 */
public class EntityGraphExperiment {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12");

    private Long setUpOneItemWithSellerAndBid() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        User seller = new User("johndoe");
        em.persist(seller);
        User bidder = new User("janeroe");
        em.persist(bidder);

        ItemForEntityGraph item = new ItemForEntityGraph("Item 1", LocalDate.now().plusDays(1), seller);
        em.persist(item);

        BidForEntityGraph bid = new BidForEntityGraph(item, bidder, new BigDecimal("15"));
        item.addBid(bid);
        em.persist(bid);

        em.getTransaction().commit();
        Long id = item.getId();
        em.close();
        return id;
    }

    @Test
    public void plainFindFollowsTheMapping() {
        Long itemId = setUpOneItemWithSellerAndBid();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== PLAIN find(), NO ENTITY GRAPH ===");
        System.out.println("Prediction: 1 query for Item, joining seller right away because");
        System.out.println("its mapping is EAGER. bids stays an uninitialized lazy proxy.\n");

        ItemForEntityGraph item = em.find(ItemForEntityGraph.class, itemId);

        System.out.println("\n=== DONE - seller already loaded, bids not touched yet ===");
        System.out.println("seller username: " + item.getSeller().getUsername());

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void fetchgraphMakesTheGraphTheWholePlan() {
        Long itemId = setUpOneItemWithSellerAndBid();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        EntityGraph<?> graph = em.getEntityGraph("Item.withBids");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);

        System.out.println("\n=== find() WITH fetchgraph hint ===");
        System.out.println("Prediction: ONE query, Item LEFT JOIN bids (forced EAGER by the graph).");
        System.out.println("seller is OUTSIDE the graph -> forced LAZY, no join for it at all,");
        System.out.println("even though its mapping says EAGER.\n");

        ItemForEntityGraph item = em.find(ItemForEntityGraph.class, itemId, hints);

        System.out.println("\n=== DONE - about to touch seller (should trigger a NEW query now) ===");
        System.out.println("seller username: " + item.getSeller().getUsername());

        em.getTransaction().commit();
        em.close();
    }

    private Long setUpOneItemWithTwoBidsAndTwoImages() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        User seller = new User("johndoe");
        em.persist(seller);
        User bidder = new User("janeroe");
        em.persist(bidder);

        ItemForEntityGraph item = new ItemForEntityGraph("Item 1", LocalDate.now().plusDays(1), seller);
        em.persist(item);

        item.addBid(new BidForEntityGraph(item, bidder, new BigDecimal("15")));
        item.addBid(new BidForEntityGraph(item, bidder, new BigDecimal("20")));
        item.getBids().forEach(em::persist);

        item.addImage(new ImageXForEntityGraph(item, "front.jpg"));
        item.addImage(new ImageXForEntityGraph(item, "back.jpg"));
        item.getImages().forEach(em::persist);

        em.getTransaction().commit();
        Long id = item.getId();
        em.close();
        return id;
    }

    @Test
    public void graphWithTwoCollectionsCausesCartesianProduct() {
        Long itemId = setUpOneItemWithTwoBidsAndTwoImages();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        EntityGraph<?> graph = em.getEntityGraph("Item.withBidsAndImages");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);

        System.out.println("\n=== find() WITH a graph listing BOTH bids AND images ===");
        System.out.println("This item has 2 bids and 2 images. Entity Graph attributes can");
        System.out.println("only be fetched via JOIN (no SUBSELECT/SELECT option like the");
        System.out.println("mapping-level @Fetch annotation has). Joining two collections at");
        System.out.println("once means the database returns the CROSS of both: 2 bids x 2");
        System.out.println("images = 4 rows for a single Item, all in ONE query.\n");

        ItemForEntityGraph item = em.find(ItemForEntityGraph.class, itemId, hints);

        System.out.println("\n=== DONE - a single find() already paid the cartesian cost ===");
        System.out.println("bids.size() = " + item.getBids().size()
                + " (correct, Set dedupes), images.size() = " + item.getImages().size()
                + " (correct too) - but look at the row count in the SQL above: it was 4, not 1.");

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void loadgraphOnlyAddsToTheMapping() {
        Long itemId = setUpOneItemWithSellerAndBid();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        EntityGraph<?> graph = em.getEntityGraph("Item.withBids");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);

        System.out.println("\n=== find() WITH loadgraph hint ===");
        System.out.println("Prediction: bids becomes EAGER because of the graph (join).");
        System.out.println("seller is OUTSIDE the graph -> falls back to its OWN mapping,");
        System.out.println("which is EAGER -> seller gets joined too, unlike fetchgraph.\n");

        ItemForEntityGraph item = em.find(ItemForEntityGraph.class, itemId, hints);

        System.out.println("\n=== DONE - seller should already be loaded, same as plain find() ===");
        System.out.println("seller username: " + item.getSeller().getUsername());

        em.getTransaction().commit();
        em.close();
    }

    /**
     * fetchgraph means "this graph IS the whole fetch plan". loadgraph means
     * "this graph only ADDS to the mapping". Putting both hints in the same
     * map at once asks Hibernate to treat the SAME find() as both "the only
     * source of truth" and "just an addition" - contradictory instructions.
     * Prediction: this should fail, not silently pick one or merge both.
     */
    @Test
    public void conflictingFetchgraphAndLoadgraphHintsFail() {
        Long itemId = setUpOneItemWithTwoBidsAndTwoImages();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        EntityGraph<?> bidsGraph = em.getEntityGraph("Item.withBids");
        EntityGraph<?> imagesGraph = em.getEntityGraph("Item.withImages");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", bidsGraph);
        hints.put("javax.persistence.loadgraph", imagesGraph);

        System.out.println("\n=== find() WITH BOTH fetchgraph AND loadgraph hints at once ===");
        System.out.println("Prediction: this should throw, since the two hints give");
        System.out.println("contradictory instructions for the same find() call.\n");

        try {
            em.find(ItemForEntityGraph.class, itemId, hints);
            System.out.println("=== NO EXCEPTION - Hibernate accepted both hints somehow ===");
        } catch (Exception e) {
            System.out.println("=== THREW AS PREDICTED: " + e.getClass().getName() + " - " + e.getMessage());
        }

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Same cartesian problem as Item.withBidsAndImages, but the graph is
     * built at runtime with EntityManager.createEntityGraph() instead of
     * being declared with @NamedEntityGraph. Whether a graph is static or
     * dynamic makes no difference to the SQL Hibernate generates - a graph
     * listing two collections still joins both directly to Item.
     */
    @Test
    public void dynamicEntityGraphCausesSameCartesianProduct() {
        Long itemId = setUpOneItemWithTwoBidsAndTwoImages();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        EntityGraph<ItemForEntityGraph> graph = em.createEntityGraph(ItemForEntityGraph.class);
        graph.addAttributeNodes("bids", "images");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);

        System.out.println("\n=== find() WITH a DYNAMIC graph (built via createEntityGraph()) ===");
        System.out.println("listing both bids and images, same 2 bids / 2 images data as before.");
        System.out.println("Prediction: identical SQL shape to the @NamedEntityGraph version -");
        System.out.println("both collections joined directly to Item -> 4 rows, cartesian product.\n");

        ItemForEntityGraph item = em.find(ItemForEntityGraph.class, itemId, hints);

        System.out.println("\n=== DONE ===");
        System.out.println("bids.size() = " + item.getBids().size() + ", images.size() = " + item.getImages().size()
                + " - correct after dedup, but the SQL above already paid the cartesian row count.");

        em.getTransaction().commit();
        em.close();
    }
}
