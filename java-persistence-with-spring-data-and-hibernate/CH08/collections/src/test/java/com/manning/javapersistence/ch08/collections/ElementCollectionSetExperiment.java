package com.manning.javapersistence.ch08.collections;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Experiment: @ElementCollection with Set<String>.
 * Item gains a collection of image filenames (values, not entities).
 * Compares against BasicEntitiesExperiment (no mapping).
 */
public class ElementCollectionSetExperiment {

  private EntityManagerFactory emf;
  private EntityManager em;
  private EntityTransaction tx;

  @BeforeEach
  void setUp() {
    emf = Persistence.createEntityManagerFactory("ch08");
    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();

    em.close();
  }

  @AfterEach
  void tearDown() {
    tx.rollback();
    em.close();
    emf.close();
  }

  @Test
  void persistItemWithImages() {
    Item item = new Item("Foo", LocalDate.now().plusDays(7));
    item.getImages().add("a.jpg");
    item.getImages().add("b.jpg");

    em.persist(item);

    System.out.println(">>> BEFORE FLUSH <<<");
    em.flush();
    System.out.println(">>> AFTER FLUSH <<<");
  }
}