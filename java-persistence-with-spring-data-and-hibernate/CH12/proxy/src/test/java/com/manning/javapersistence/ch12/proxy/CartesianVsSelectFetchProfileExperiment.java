package com.manning.javapersistence.ch12.proxy;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * ItemWithFetchProfile has two independent lazy collections: bids
 * (@FetchProfile-overridable to JOIN) and images (statically
 * @Fetch(FetchMode.SUBSELECT), because Hibernate 5.6's annotation-based
 * @FetchProfile.FetchOverride only supports FetchMode.JOIN - SELECT/SUBSELECT
 * there throws MappingException at bootstrap. Verified experimentally:
 * enabling a profile with mode = FetchMode.SELECT on this Hibernate version
 * fails EntityManagerFactory creation with
 * "Only FetchMode.JOIN is currently supported".
 *
 * Test A enables the "with-bids-joined" profile AND (accidentally, to show
 * why you would not do this) forces images eager too, joining both
 * collections into the same query - the Cartesian product this chapter
 * warns about.
 *
 * Test B is the actual recommended design: enable ONLY the bids JOIN
 * profile, and leave images on its static SUBSELECT mapping. Loading TWO
 * Items lets us see the real benefit of SUBSELECT: the first getImages()
 * call fires ONE query that loads images for every Item already in the
 * persistence context, not one query per Item (which is what plain
 * FetchMode.SELECT would do).
 */
public class CartesianVsSelectFetchProfileExperiment {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12");

    private Long[] setUpTwoItemsWithBidsAndImages() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        User seller = new User("johndoe");
        em.persist(seller);
        User bidder = new User("janeroe");
        em.persist(bidder);

        Item item1 = new Item("Item One", LocalDate.now().plusDays(1), seller);
        em.persist(item1);
        Item item2 = new Item("Item Two", LocalDate.now().plusDays(1), seller);
        em.persist(item2);

        for (Item item : new Item[]{item1, item2}) {
            for (int i = 1; i <= 3; i++) {
                Bid bid = new Bid(item, bidder, new BigDecimal(9 + i));
                item.addBid(bid);
                em.persist(bid);
            }
            for (int i = 1; i <= 2; i++) {
                em.persist(new ImageX(item, "photo" + i + ".jpg"));
            }
        }

        em.getTransaction().commit();
        Long[] ids = {item1.getId(), item2.getId()};
        em.close();
        return ids;
    }

    @Test
    public void joiningBothCollectionsAtOnceProducesCartesianProduct() {
        Long[] ids = setUpTwoItemsWithBidsAndImages();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== ENABLING \"with-bids-joined\", THEN forcing images eager too ===");
        System.out.println("Each Item has 3 Bids and 2 Images. Joining BOTH collections in the");
        System.out.println("same query multiplies rows PER ITEM: 3 x 2 = 6 raw rows for item1 alone.");
        System.out.println("Prediction: 2 items x 6 = 12 raw rows total (no distinct used).\n");

        Session session = em.unwrap(Session.class);
        session.enableFetchProfile("with-bids-joined");

        // join fetch images too, on top of the bids profile, to reproduce
        // the Cartesian product deliberately for comparison with Test B
        List<ItemWithFetchProfile> result = em.createQuery(
                        "select i from ItemWithFetchProfile i join fetch i.images where i.id in :ids",
                        ItemWithFetchProfile.class)
                .setParameter("ids", List.of(ids[0], ids[1]))
                .getResultList();

        System.out.println("\n=== RESULT ===");
        System.out.println("Raw rows returned by getResultList(): " + result.size());
        System.out.println("Expected 12 raw JDBC rows collapsed by Hibernate's result transformer.");

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void joiningBidsAndLeavingImagesOnSubselectAvoidsCartesianProduct() {
        Long[] ids = setUpTwoItemsWithBidsAndImages();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== ENABLING ONLY \"with-bids-joined\" (images stays SUBSELECT) ===");
        System.out.println("Prediction: main query returns exactly 3 rows PER item (6 total,");
        System.out.println("one per Bid row, matching the LEFT JOIN to Bid) - no multiplication");
        System.out.println("by images, since images is not part of this query at all.\n");

        Session session = em.unwrap(Session.class);
        session.enableFetchProfile("with-bids-joined");

        List<ItemWithFetchProfile> items = em.createQuery(
                        "select i from ItemWithFetchProfile i where i.id in :ids",
                        ItemWithFetchProfile.class)
                .setParameter("ids", List.of(ids[0], ids[1]))
                .getResultList();

        System.out.println("\n--- about to call getImages() on item 1: watch for ONE subselect ---\n");
        System.out.println("item1 images: " + items.get(0).getImages().size());

        System.out.println("\n--- now calling getImages() on item 2: expect NO further SQL ---\n");
        System.out.println("item2 images: " + items.get(1).getImages().size());

        em.getTransaction().commit();
        em.close();
    }
}
