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

    @Test
    void makeTransient() {
       EntityManager em = emf.createEntityManager();
       em.getTransaction().begin();
       Item someItem  = new Item();
       someItem.setName("Some Item");
       em.persist(someItem);
       em.getTransaction().commit();
       em.close();
       Long ITEM_ID = someItem.getId();

       em = emf.createEntityManager();
       em.getTransaction().begin();

       /*
               If you call <code>find()</code>, Hibernate will execute a <code>SELECT</code> to
               load the <code>Item</code>. If you call <code>getReference()</code>, Hibernate
               will attempt to avoid the <code>SELECT</code> and return a proxy.
       */
        Item item = em.find(Item.class, ITEM_ID);
        /*
               Calling <code>remove()</code> will queue the entity instance for deletion when
               the unit of work completes, it is now in <em>removed</em> state. If <code>remove()</code>
               is called on a proxy, Hibernate will execute a <code>SELECT</code> to load the data.
               An entity instance has to be fully initialized during life cycle transitions. You may
               have life cycle callback methods or an entity listener enabled
               (see <a href="#EventListenersInterceptors"/>), and the instance must pass through these
               interceptors to complete its full life cycle.
        */
        em.remove(item);


        /*
                An entity in removed state is no longer in persistent state, this can be
                checked with the <code>contains()</code> operation.
        */
        assertFalse(em.contains(item));

        /*
               You can make the removed instance persistent again, cancelling the deletion.
        */
//         em.persist(item);

        // hibernate.use_identifier_rollback was enabled, it now looks like a transient instance
        assertNull(item.getId());

        /*
               When the transaction commits, Hibernate synchronizes the state transitions with the
               database and executes the SQL <code>DELETE</code>. The JVM garbage collector detects that the
               <code>item</code> is no longer referenced by anyone and finally deletes the last trace of
               the data.
        */
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();

        em.persist(item); // Primary key is now 2


        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();

        item = em.find(Item.class, ITEM_ID);
        assertNull(item);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    void refresh() throws ExecutionException, InterruptedException {
       EntityManager em = emf.createEntityManager();
       em.getTransaction().begin();
       Item someItem = new Item();
       someItem.setName("Some Item");
       em.persist(someItem);
       em.getTransaction().commit();
       em.close();
       Long ITEM_ID = someItem.getId();

       em = emf.createEntityManager();
       em.getTransaction().begin();

       Item item = em.find(Item.class, ITEM_ID);

       // Someone updates this row in the databbase!
        Executors.newSingleThreadExecutor().submit(() -> {
            EntityManager em1 = emf.createEntityManager();
            try {
                em1.getTransaction().begin();

                Session session = em1.unwrap(Session.class);
                session.doWork(con -> {
                    Item item1 = em1.find(Item.class, ITEM_ID);
                    item1.setName("Concurrent Update Name");
                    em1.persist(item1);
                });

                em1.getTransaction().commit();
                em1.close();
            } catch (Exception ex) {
                throw new RuntimeException("Concurrent operation failure: " + ex, ex);
            }
            return null;
        }).get();

        /*
        * The `em.refresh(item)` at line 302 ensures that the `item` entity is synchronized with the database.
        * However, the transaction is still active, and the persistence context is managing the entity.
        * The actual database state is not fully committed or visible until the transaction is committed at line 303.

        At line 305, after the transaction is committed, the second `em.refresh(item)` ensures that the `item` entity reflects the most up-to-date state from the database.
        *  This is necessary because the database changes made by the concurrent thread (e.g., "Concurrent Update Name") become visible only after the transaction is committed.
        *
        * */
        em.refresh(item);
        em.getTransaction().commit(); // Flush: Dirty check with snapshot and SQL UPDATE

        em.refresh(item); // Database changes are now visible after commit
        em.close();
        assertEquals("Concurrent Update Name", item.getName());
    }


}
