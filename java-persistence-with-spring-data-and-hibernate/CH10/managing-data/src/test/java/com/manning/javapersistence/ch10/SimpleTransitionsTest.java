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
package com.manning.javapersistence.ch10;

import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceUnitUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


import static org.junit.jupiter.api.Assertions.*;

public class SimpleTransitionsTest {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch10");

    @Test
    void makePersistent() {
        Item item = new Item();
        item.setName("Some Item"); // Item#name is NOT NULL

        EntityManager em = emf.createEntityManager(); // Application-managed
        em.getTransaction().begin();

        em.persist(item);

        Long ITEM_ID = item.getId();

        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        assertEquals("Some Item", em.find(Item.class, ITEM_ID).getName());
        em.getTransaction().commit();;
        em.close();
    }

    @Test
    void retrievePersistent() {
        EntityManager em = emf.createEntityManager(); // Application-managed
        em.getTransaction().begin();
        Item someItem = new Item();
        someItem.setName("Some Item");
        em.persist(someItem);
        em.getTransaction().commit();
        em.close();
        Long ITEM_ID = someItem.getId();

        {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            // Hit the database if not already in persistence context
            Item item = em.find(Item.class, ITEM_ID);
            if (item != null)
                item.setName("New Name");
            em.getTransaction().commit(); // Flush: Dirty check with snapshot and SQL UPDATE
            em.close();
        }

        {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Item itemA = em.find(Item.class, ITEM_ID);
            Item itemB = em.find(Item.class, ITEM_ID); // No DB hit, already in persistence context

            assertTrue(itemA == itemB);
            assertTrue(itemA.equals(itemB));
            assertTrue(itemA.getId().equals(itemB.getId()));
            em.getTransaction().commit();
            em.close();
        }

        em = emf.createEntityManager();
        em.getTransaction().begin();
        assertEquals("New Name", em.find(Item.class, ITEM_ID).getName());
        em.getTransaction().commit();
        em.close();
    }

    @Test
    void retrievePersistenceReference() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Item someItem = new Item();
        someItem.setName("Some Item");
        em.persist(someItem);
        em.getTransaction().commit();
        em.close();
        Long ITEM_ID = someItem.getId();
        Long NON_EXISTENT_ID = ITEM_ID + 999;

        {
            em = emf.createEntityManager();
            em.getTransaction().begin();

              /*
               If the persistence context already contains an <code>Item</code> with the given identifier, that
               <code>Item</code> instance is returned by <code>getReference()</code> without hitting the database.
               Furthermore, if <em>no</em> persistent instance with that identifier is currently managed, a hollow
               placeholder will be produced by Hibernate, a proxy. This means <code>getReference()</code> will not
               access the database, and it doesn't return <code>null</code>, unlike <code>find()</code>.
             */
            Item item = em.getReference(Item.class, ITEM_ID);

             /*
               JPA offers <code>PersistenceUnitUtil</code> helper methods such as <code>isLoaded()</code> to
               detect if you are working with an uninitialized proxy.
            */
            PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();

            assertFalse(persistenceUnitUtil.isLoaded(item));

            /*
               As soon as you call any method such as <code>Item#getName()</code> on the proxy, a
               <code>SELECT</code> is executed to fully initialize the placeholder. The exception to this rule is
               a method that is a mapped database identifier getter method, such as <code>getId()</code>. A proxy
               might look like the real thing but it is only a placeholder carrying the identifier value of the
               entity instance it represents. If the database record doesn't exist anymore when the proxy is
               initialized, an <code>EntityNotFoundException</code> will be thrown.
             */
            // assertEquals("Some Item", item.getName());

            // We can initialize the proxy explicitly using Hibernate API like below
            /*
               Hibernate has a convenient static <code>initialize()</code> method, loading the proxy's data.
             */
//         Hibernate.initialize(item);

//            assertEquals("Some Item", item.getName()); // DB hit occurs here

            em.getTransaction().commit();
            em.close();


             /*
               After the persistence context is closed, <code>item</code> is in detached state. If you do
               not initialize the proxy while the persistence context is still open, you get a
               <code>LazyInitializationException</code> if you access the proxy. You can't load
               data on-demand once the persistence context is closed. The solution is simple: Load the
               data before you close the persistence context.
             */
            assertThrows(LazyInitializationException.class, () -> {
                item.getName(); // Outside of transaction and EntityManager: LazyInitializationException
            });
        }

        {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            EntityManager finalEm = em;
            assertThrows(javax.persistence.EntityNotFoundException.class, () -> {
                Item item = finalEm.getReference(Item.class, NON_EXISTENT_ID);
                item.getName(); // DB hit occurs here
            });

            em.getTransaction().commit();
            em.close();
        }

    }


}
