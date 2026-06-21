package com.manning.javapersistence.ch10;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that show when and why {@code EntityManager.merge()} is needed.
 *
 * Rule of thumb: merge() is for re-attaching a <em>detached</em> entity
 * (one that was loaded in a previous persistence context) so its changes
 * can be written to the database.
 */
public class MergeExplainedTest {

  EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch10");

  private Long persistSampleUser() {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();

    User user = new User();
    user.setUsername("johndoe");
    user.setHomeAddress(new Address("Some Street", "1234", "Some City"));
    em.persist(user);

    Long userId = user.getId();
    em.getTransaction().commit();
    em.close();
    return userId;
  }

  private String loadUsername(Long userId) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    String username = em.find(User.class, userId).getUsername();
    em.getTransaction().commit();
    em.close();
    return username;
  }

  /**
   * When the EntityManager is still open and the entity is managed,
   * you just change fields — Hibernate dirty-checks on commit. No merge().
   */
  @Test
  void managedEntityDoesNotNeedMerge() {
    Long userId = persistSampleUser();

    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();

    User managedUser = em.find(User.class, userId);
    assertTrue(em.contains(managedUser));

    managedUser.setUsername("updated in same session");

    em.getTransaction().commit();
    em.close();

    assertEquals("updated in same session", loadUsername(userId));
  }

  /**
   * After the EntityManager closes, the entity is detached.
   * Changing it does nothing to the database until you merge() it
   * into a new persistence context.
   */
  @Test
  void detachedChangesAreLostWithoutMerge() {
    Long userId = persistSampleUser();

    User detachedUser;
    {
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();
      detachedUser = em.find(User.class, userId);
      em.getTransaction().commit();
      em.close();
      // detachedUser is now detached — em.contains(detachedUser) would be false
    }

    detachedUser.setUsername("this change is never saved");

    {
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();
      // We open a transaction but never merge or find+modify the detached instance
      em.getTransaction().commit();
      em.close();
    }

    assertEquals("johndoe", loadUsername(userId));
  }

  /**
   * merge() copies state from the detached instance into a managed one
   * and returns that managed copy. The original reference stays detached.
   */
  @Test
  void mergeReturnsManagedCopyNotSameReference() {
    Long userId = persistSampleUser();

    User detachedUser;
    {
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();
      detachedUser = em.find(User.class, userId);
      em.getTransaction().commit();
      em.close();
    }

    detachedUser.setUsername("merged username");

    User managedCopy;
    {
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();
      managedCopy = em.merge(detachedUser);

      assertTrue(em.contains(managedCopy));
      assertFalse(em.contains(detachedUser));
      assertNotSame(detachedUser, managedCopy);

      em.getTransaction().commit();
      em.close();
    }

    assertEquals("merged username", loadUsername(userId));
    assertEquals("merged username", managedCopy.getUsername());
  }

  /**
   * Typical web-app flow: request 1 loads data, request 2 saves edits.
   * Each HTTP request gets its own EntityManager (and then closes it),
   * so the object you edit in between is always detached when you save.
   */
  @Test
  void webApplicationEditFormScenario() {
    Long userId = persistSampleUser();

    // --- Request 1: "GET /users/1/edit" — load form data, then close EM ---
    User userFromRequest1 = loadUserForEditing(userId);
    assertEquals("johndoe", userFromRequest1.getUsername());

    // --- User edits the form in the browser (no database involved) ---
    userFromRequest1.setUsername("janedoe");
    userFromRequest1.setHomeAddress(new Address("New Street", "9999", "New City"));

    // --- Request 2: "POST /users/1" — save submitted form ---
    saveEditedUser(userFromRequest1);

    User reloaded = loadUserForEditing(userId);
    assertEquals("janedoe", reloaded.getUsername());
    assertEquals("New Street", reloaded.getHomeAddress().getStreet());
  }

  /**
   * If you already loaded the row in the current persistence context,
   * merge() still works but is redundant — just modify the managed instance.
   */
  @Test
  void mergeIsRedundantWhenEntityAlreadyManaged() {
    Long userId = persistSampleUser();

    User detachedCopy;
    {
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();
      detachedCopy = em.find(User.class, userId);
      em.getTransaction().commit();
      em.close();
    }
    detachedCopy.setUsername("from detached copy");

    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();

    User alreadyManaged = em.find(User.class, userId);
    alreadyManaged.setUsername("from managed instance");

    // merge() would overwrite managed state with detached state on flush —
    // here the detached copy says "from detached copy", so that wins if we merge:
    em.merge(detachedCopy);

    em.getTransaction().commit();
    em.close();

    assertEquals("from detached copy", loadUsername(userId));
  }

  /** Simulates a read-only service method that returns a detached entity. */
  private User loadUserForEditing(Long userId) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    User user = em.find(User.class, userId);
    em.getTransaction().commit();
    em.close();
    return user;
  }

  /** Simulates a save handler that receives a detached entity from the web tier. */
  private void saveEditedUser(User editedUser) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    em.merge(editedUser);
    em.getTransaction().commit();
    em.close();
  }
}
