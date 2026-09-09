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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Question: if item.fetch("bids", LEFT) is called TWICE while building
 * the same CriteriaQuery, does Hibernate deduplicate it into one LEFT JOIN,
 * or does it produce two LEFT JOINs (and therefore duplicate/multiplied rows)?
 */
public class CriteriaDynamicFetchExperiment {

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
    public void callingFetchTwiceOnSameAssociation() {
        Long itemId = setUpOneItemWithBids();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== CALLING item.fetch(\"bids\") TWICE ===");
        System.out.println("Item has 3 Bids. Watch the SQL below: one LEFT JOIN or two?\n");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        Root<Item> item = cq.from(Item.class);

        item.fetch("bids", JoinType.LEFT);
        item.fetch("bids", JoinType.LEFT); // called again, e.g. by accident

        cq.select(item).distinct(true);
        cq.where(cb.equal(item.get("id"), itemId));

        List<Item> result = em.createQuery(cq).getResultList();

        System.out.println("\n=== RESULT ===");
        System.out.println("Items returned: " + result.size());
        System.out.println("Bids on that Item: " + result.get(0).getBids().size());

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void countRawRowsBeforeDistinctCollapsesThem() {
        Long itemId = setUpOneItemWithBids();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== SAME QUERY, NO distinct(true), TO SEE RAW ROW COUNT ===");
        System.out.println("Item has 3 Bids, fetched via TWO separate LEFT JOINs to Bid.");
        System.out.println("Prediction: 3 x 3 = 9 raw rows.\n");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        Root<Item> item = cq.from(Item.class);

        item.fetch("bids", JoinType.LEFT);
        item.fetch("bids", JoinType.LEFT);

        cq.select(item); // no distinct this time
        cq.where(cb.equal(item.get("id"), itemId));

        List<Item> result = em.createQuery(cq).getResultList();

        System.out.println("\n=== RESULT ===");
        System.out.println("Raw rows returned by getResultList(): " + result.size());

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void fixUsingSetOfAssociationNamesAvoidsDuplicateFetch() {
        Long itemId = setUpOneItemWithBids();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("\n=== FIX: COLLECT ASSOCIATION NAMES INTO A SET FIRST ===");
        System.out.println("Two separate 'reasons' both want bids fetched, but the Set");
        System.out.println("only stores the name \"bids\" once, so fetch() is called once.\n");

        boolean fetchBidsRequestedByCaller = true;
        boolean fetchBidsRequestedBySorting = true; // a second, independent reason

        Set<String> associationsToFetch = new HashSet<>();
        if (fetchBidsRequestedByCaller) {
            associationsToFetch.add("bids");
        }
        if (fetchBidsRequestedBySorting) {
            associationsToFetch.add("bids"); // duplicate intent, Set absorbs it
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        Root<Item> item = cq.from(Item.class);

        for (String association : associationsToFetch) {
            item.fetch(association, JoinType.LEFT);
        }

        cq.select(item);
        cq.where(cb.equal(item.get("id"), itemId));

        List<Item> result = em.createQuery(cq).getResultList();

        System.out.println("\n=== RESULT ===");
        System.out.println("Raw rows returned by getResultList(): " + result.size());
        System.out.println("Expected: 3 (one LEFT JOIN, no Cartesian multiplication).");

        em.getTransaction().commit();
        em.close();
    }
}
