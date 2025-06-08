package com.manning.javapersistence.ch06;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.manning.javapersistence.ch06.model.Item;

public class SavingBlobTest {
  Long ITEM_ID;

  @Test
  @Order(1)
  void testSavingBlob() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch06.mapping_value_types");
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      Item item = new Item();
      item.setName("New Item");

      String imageResourcePath = "/images/ring.jpg";
      InputStream inputStream = getClass().getResourceAsStream(imageResourcePath);

      // Assert that the input stream was found
      assertNotNull(inputStream, "Image resource not found: " + imageResourcePath);

      org.hibernate.Session session = em.unwrap(org.hibernate.Session.class);
      byte[] bytes = inputStream.readAllBytes();
      final int length = bytes.length;
      System.out.println("Length is: " + length);
      Blob imageBlob = session.getLobHelper().createBlob(bytes);
      System.out.println(imageBlob);
      item.setImageblob(imageBlob);
      System.out.println(item.getImageblob().length());

      em.persist(item);
      em.getTransaction().commit();

      ITEM_ID = item.getId();

    } catch (SQLException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      em.close();
      emf.close();
    }

  }

  @Test
  @Order(2)
  void testLoadingBlob() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch06.mapping_value_types");
    EntityManager em = emf.createEntityManager();

    try {
      em.getTransaction().begin();

      String imageResourcePath = "/images/ring.jpg";
      InputStream imageStream = getClass().getResourceAsStream(imageResourcePath);

      Item item = em.find(Item.class, 1000L);
      em.getTransaction().commit();
      InputStream savedImageInputStream = item.getImageblob().getBinaryStream();
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      org.springframework.util.StreamUtils.copy(savedImageInputStream, outStream);
      byte[] imageBytes = outStream.toByteArray();

      assertArrayEquals(imageStream.readAllBytes(), imageBytes);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      em.close();
      emf.close();
    }
  }
}
