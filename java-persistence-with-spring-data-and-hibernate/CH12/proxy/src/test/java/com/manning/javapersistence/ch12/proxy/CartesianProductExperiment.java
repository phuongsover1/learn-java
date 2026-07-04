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
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * One Item, 3 Bids, 2 Categories.
 * A single JOIN FETCH on both collections should return
 * 3 x 2 = 6 raw rows for that one Item, even though the
 * object graph only ever has 3 Bids and 2 Categories.
 */
public class CartesianProductExperiment {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12");

    private Long setUpOneItemWithBidsAndCategories() {
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

        Category cat1 = new Category("Category One");
        em.persist(cat1);
        cat1.addItem(item);
        item.addCategory(cat1);

        Category cat2 = new Category("Category Two");
        em.persist(cat2);
        cat2.addItem(item);
        item.addCategory(cat2);

        em.getTransaction().commit();
        Long itemId = item.getId();
        em.close();
        return itemId;
    }

    @Test
    public void singleQueryTwoJoinsCausesCartesianProduct() {
        Long itemId = setUpOneItemWithBidsAndCategories();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== SINGLE QUERY, JOIN FETCH BOTH COLLECTIONS ===");
        System.out.println("Item has 3 Bids and 2 Categories.");
        System.out.println("Expected raw row count: 3 x 2 = 6 (Cartesian product)\n");

        Query query = em.createQuery(
                "select distinct i from Item i "
                        + "join fetch i.bids "
                        + "join fetch i.categories "
                        + "where i.id = :id");
        query.setParameter("id", itemId);

        @SuppressWarnings("unchecked")
        List<Item> resultAfterDistinct = query.getResultList();

        System.out.println("\n=== RESULT AFTER HIBERNATE ASSEMBLES THE OBJECT GRAPH ===");
        System.out.println("Distinct Items returned to Java: " + resultAfterDistinct.size());
        Item item = resultAfterDistinct.get(0);
        System.out.println("Bids on that Item: " + item.getBids().size());
        System.out.println("Categories on that Item: " + item.getCategories().size());
        System.out.println("\nLook at the SQL above: count the rows in the ResultSet before 'distinct'");
        System.out.println("collapses them back down to 1 Item / 3 Bids / 2 Categories.");

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void twoSeparateQueriesAvoidCartesianProduct() {
        Long itemId = setUpOneItemWithBidsAndCategories();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== TWO SEPARATE QUERIES INSTEAD ===");

        System.out.println("\n-- Query 1: fetch Item + Bids only --");
        Query bidsQuery = em.createQuery(
                "select distinct i from Item i join fetch i.bids where i.id = :id");
        bidsQuery.setParameter("id", itemId);
        Item itemWithBids = (Item) bidsQuery.getSingleResult();
        System.out.println("Rows for this query: 1 Item x 3 Bids = 3 rows (no multiplication)");

        System.out.println("\n-- Query 2: fetch Item + Categories only --");
        Query categoriesQuery = em.createQuery(
                "select distinct i from Item i join fetch i.categories where i.id = :id");
        categoriesQuery.setParameter("id", itemId);
        Item itemWithCategories = (Item) categoriesQuery.getSingleResult();
        System.out.println("Rows for this query: 1 Item x 2 Categories = 2 rows (no multiplication)");

        System.out.println("\nTotal rows across both queries: 3 + 2 = 5, vs 6 for the single-query Cartesian version.");
        System.out.println("Bids: " + itemWithBids.getBids().size());
        System.out.println("Categories: " + itemWithCategories.getCategories().size());

        em.getTransaction().commit();
        em.close();
    }
}
