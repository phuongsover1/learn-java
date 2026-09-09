/*
 * ========================================================================
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ========================================================================
 */
package com.manning.javapersistence.ch12.proxy;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Hibernate;

public class LazyProxyCollections {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12");

    private FetchTestData storeTestData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Long[] categoryIds = new Long[3];
        Long[] itemIds = new Long[3];
        Long[] userIds = new Long[3];

        User johndoe = new User("johndoe");
        em.persist(johndoe);
        userIds[0] = johndoe.getId();

        User janeroe = new User("janeroe");
        em.persist(janeroe);
        userIds[1] = janeroe.getId();

        User robertdoe = new User("robertdoe");
        em.persist(robertdoe);
        userIds[2] = robertdoe.getId();

        Category category = new Category("Category One");
        em.persist(category);
        categoryIds[0] = category.getId();

        Item item = new Item("Item One", LocalDate.now().plusDays(1), johndoe);
        em.persist(item);
        itemIds[0] = item.getId();
        category.addItem(item);
        item.addCategory(category);
        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(item, robertdoe, new BigDecimal(9 + i));
            item.addBid(bid);
            em.persist(bid);
        }

        category = new Category("Category Two");
        em.persist(category);
        categoryIds[1] = category.getId();

        item = new Item("Item Two", LocalDate.now().plusDays(1), johndoe);
        em.persist(item);
        itemIds[1] = item.getId();
        category.addItem(item);
        item.addCategory(category);
        for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(item, janeroe, new BigDecimal(2 + i));
            item.addBid(bid);
            em.persist(bid);
        }

        item = new Item("Item Three", LocalDate.now().plusDays(2), janeroe);
        em.persist(item);
        itemIds[2] = item.getId();
        category.addItem(item);
        item.addCategory(category);

        category = new Category("Category Three");
        em.persist(category);
        categoryIds[2] = category.getId();

        em.getTransaction().commit();
        em.close();

        FetchTestData testData = new FetchTestData();
        testData.items = new TestData(itemIds);
        testData.users = new TestData(userIds);
        return testData;
    }

    @Test
    public void observeProxyInAction() {
        FetchTestData testData = storeTestData();
        Long itemId = testData.items.getFirstId();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== LOADING ITEM ===");
        Item item = em.find(Item.class, itemId);
        System.out.println("Item loaded: " + item.getName());

        System.out.println("\n=== OBSERVING THE BIDS FIELD (lazy-loaded collection) ===");
        Set<Bid> bids = item.getBids();
        System.out.println("Type of bids field: " + bids.getClass().getName());
        System.out.println("Is it a real HashSet? " + (bids instanceof HashSet));
        System.out.println("Is it initialized? " + Hibernate.isInitialized(bids));

        System.out.println("\n=== ACCESSING THE COLLECTION (this triggers the query) ===");
        int bidCount = bids.size();
        System.out.println("Number of bids: " + bidCount);
        System.out.println("Is it initialized NOW? " + Hibernate.isInitialized(bids));
        System.out.println("Type is still: " + bids.getClass().getName());

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void lazyVsEagerComparison() {
        FetchTestData testData = storeTestData();
        Long itemId = testData.items.getFirstId();

        System.out.println("\n====== LAZY LOADING ======");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("Loading Item (with LAZY seller)...");
        Item item = em.find(Item.class, itemId);
        System.out.println("Item loaded: " + item.getName());
        System.out.println("Is seller initialized? " + Hibernate.isInitialized(item.getSeller()));

        System.out.println("\nAccessing seller.getUsername()...");
        String username = item.getSeller().getUsername();
        System.out.println("Seller username: " + username);
        System.out.println("Result: 2 queries (1 for Item, 1 for User when accessed)");

        em.getTransaction().commit();
        em.close();

        System.out.println("\n====== EAGER LOADING ======");
        em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("Loading ItemEager (with EAGER seller)...");
        ItemEager itemEager = em.find(ItemEager.class, itemId);
        System.out.println("ItemEager loaded: " + itemEager.getName());
        System.out.println("Is seller initialized? " + Hibernate.isInitialized(itemEager.getSeller()));

        System.out.println("\nAccessing seller.getUsername()...");
        String usernameEager = itemEager.getSeller().getUsername();
        System.out.println("Seller username: " + usernameEager);
        System.out.println("Result: 1 query with JOIN (User already loaded)");

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void lazyInitializationException() {
        FetchTestData testData = storeTestData();
        Long itemId = testData.items.getFirstId();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== LAZY INITIALIZATION EXCEPTION ===");
        System.out.println("Loading Item with lazy seller...");
        Item item = em.find(Item.class, itemId);
        System.out.println("Item loaded: " + item.getName());
        System.out.println("Seller is a proxy: " + item.getSeller().getClass().getName());

        em.getTransaction().commit();
        em.close(); // <-- Closed the persistence context

        System.out.println("\nPersistence context is now CLOSED");
        System.out.println("Trying to access seller.getUsername()...");

        try {
            item.getSeller().getUsername();
            System.out.println("ERROR: Should have thrown LazyInitializationException!");
        } catch (Exception e) {
            System.out.println("✓ Got expected error: " + e.getClass().getSimpleName());
            System.out.println("  Message: " + e.getMessage());
            System.out.println("\nWhy? The proxy tried to load the User, but:");
            System.out.println("  - The EntityManager is closed");
            System.out.println("  - There's no database session to execute the query");
            System.out.println("  - Hibernate can't fetch the data it promised");
        }
    }

    @Test
    public void proxyValidatesRelationship() {
        System.out.println("\n=== WHY PROXIES INSTEAD OF PLAIN OBJECTS? ===");
        System.out.println("Proxy: Validates that the related entity exists (when you use it)");
        System.out.println("Plain object: No validation - you might have a User that doesn't exist in DB");
        System.out.println("\nExample scenario:");
        System.out.println("- Item has seller_id=1");
        System.out.println("- But User#1 was deleted from database");
        System.out.println("- Proxy: Error when you access it");
        System.out.println("- Plain User(id=1): Silent bug - no error until later");
        System.out.println("\nProxies catch broken relationships early!");
    }

    @Test
    public void understandingMultipleLazyAssociations() {
        FetchTestData testData = storeTestData();
        Long itemId = testData.items.getFirstId();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== UNDERSTANDING MULTIPLE LAZY ASSOCIATIONS ===");
        System.out.println("Item has two lazy associations:");
        System.out.println("  1. seller (ManyToOne lazy)");
        System.out.println("  2. bids (OneToMany - default is lazy)");

        Item item = em.find(Item.class, itemId);
        System.out.println("\nAfter loading Item:");
        System.out.println("  seller initialized? " + Hibernate.isInitialized(item.getSeller()));
        System.out.println("  bids initialized? " + Hibernate.isInitialized(item.getBids()));
        System.out.println("  Seller class: " + item.getSeller().getClass().getSimpleName());
        System.out.println("  Bids class: " + item.getBids().getClass().getSimpleName());

        System.out.println("\nAccessing seller triggers first query:");
        item.getSeller().getUsername();
        System.out.println("  seller initialized? " + Hibernate.isInitialized(item.getSeller()));

        System.out.println("\nAccessing bids triggers second query:");
        int bidCount = item.getBids().size();
        System.out.println("  bids initialized? " + Hibernate.isInitialized(item.getBids()));
        System.out.println("  Bid count: " + bidCount);

        System.out.println("\nTotal: 3 queries (1 Item + 1 seller + 1 bids)");

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void lazyCollectionOptionExtra() {
        FetchTestData testData = storeTestData();
        Long itemId = testData.items.getFirstId();

        System.out.println("\n====== DEFAULT LAZY (no EXTRA) ======");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        System.out.println("Item loaded");
        System.out.println("Calling item.getBids().size()...");
        int count1 = item.getBids().size();
        System.out.println("Result: " + count1 + " bids");
        System.out.println("What happened? Loaded ALL bids, then counted them");

        em.getTransaction().commit();
        em.close();

        System.out.println("\n====== WITH LazyCollectionOption.EXTRA ======");
        em = emf.createEntityManager();
        em.getTransaction().begin();

        ItemWithExtra itemExtra = em.find(ItemWithExtra.class, itemId);
        System.out.println("ItemWithExtra loaded");
        System.out.println("Calling itemExtra.getBids().size()...");
        int count2 = itemExtra.getBids().size();
        System.out.println("Result: " + count2 + " bids");
        System.out.println("What happened? Executed COUNT(*) query instead!");

        System.out.println("\n=== KEY DIFFERENCES ===");
        System.out.println("Default LAZY:       SELECT bid.* ... WHERE item_id=?  (loads all rows)");
        System.out.println("EXTRA:              SELECT COUNT(*) ... WHERE item_id=?  (count only)");
        System.out.println("\nEXTRA also optimizes:");
        System.out.println("  - .contains(bid)  → SELECT COUNT(*) ... WHERE item_id=? AND id=?");
        System.out.println("  - .isEmpty()      → SELECT COUNT(*) ... LIMIT 1");

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void extraDoesntHelpWithIteration() {
        FetchTestData testData = storeTestData();
        Long itemId = testData.items.getFirstId();

        System.out.println("\n====== EXTRA WITH ITERATION ======");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        ItemWithExtra itemExtra = em.find(ItemWithExtra.class, itemId);
        System.out.println("ItemWithExtra loaded");

        System.out.println("\nFirst: Call .size() (EXTRA optimizes this)");
        int count = itemExtra.getBids().size();
        System.out.println("Count: " + count);
        System.out.println("Query executed: COUNT(*) only");

        System.out.println("\nNow: Iterate the collection (EXTRA can't help)");
        System.out.println("Looping through bids...");
        for (Bid bid : itemExtra.getBids()) {
            System.out.println("  Bid amount: " + bid.getAmount());
        }
        System.out.println("What happened? Full SELECT * query executed");
        System.out.println("Why? Need actual Bid objects to iterate");

        System.out.println("\n=== SUMMARY ===");
        System.out.println("EXTRA optimizes operations that DON'T need data:");
        System.out.println("  ✓ .size()");
        System.out.println("  ✓ .isEmpty()");
        System.out.println("  ✓ .contains()");
        System.out.println("\nBut CAN'T optimize when you need actual objects:");
        System.out.println("  ✗ .iterator() / for loop");
        System.out.println("  ✗ .get(index)");
        System.out.println("  ✗ .stream()");

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void observeProxyForEntity() {
        FetchTestData testData = storeTestData();
        Long itemId = testData.items.getFirstId();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== LOADING ITEM (which has lazy-loaded seller) ===");
        Item item = em.find(Item.class, itemId);
        System.out.println("Item loaded: " + item.getName());

        System.out.println("\n=== OBSERVING THE SELLER FIELD (lazy-loaded entity) ===");
        User seller = item.getSeller();
        System.out.println("Type of seller: " + seller.getClass().getName());
        System.out.println("Proxy class name contains 'HibernateProxy'? "
                + seller.getClass().getName().contains("HibernateProxy"));
        System.out.println("Is it initialized? " + Hibernate.isInitialized(seller));

        System.out.println("\n=== ACCESSING THE ENTITY (triggers lazy load) ===");
        String username = seller.getUsername();
        System.out.println("Seller username: " + username);
        System.out.println("Is it initialized NOW? " + Hibernate.isInitialized(seller));

        em.getTransaction().commit();
        em.close();
    }
}
