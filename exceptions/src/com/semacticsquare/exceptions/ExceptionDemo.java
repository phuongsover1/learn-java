package com.semacticsquare.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionDemo {
  public static void main(String[] args) {
    System.out.println("\nInside main ...");
    try {

      share();
      System.out.println("After invoking share ...");
    } catch (FileNotFoundException e) {
      System.out.println("main: filenotfoundexception ...");
    } finally {
      System.out.println("Inside main's finally ...");
    }
    System.out.println("\nEnd of main ...");
  }

  private static void share() throws FileNotFoundException {
    System.out.println("\nInside share ...");

    try {
      String response = HttpConnect.send(1, "hello", "https://www.goodsnips.com");
      System.out.println("After invoking send ...");

      APIParser.parseSendResponseCode(response, "hello", "http://www.goodsnips.com");
    } catch (FileNotFoundException e) {
      System.out.println("Share: filenotfoundexception ...");
      throw e;
    } catch (IOException e) {
      System.out.println("Connecting to a new server ...");
    } catch (APIFormatChangeException e) {
      // Item 65: Don't ignore exceptions
      // e.printStackTrace();

      // Item 63: Inlucde failure-capture information in detail messages
      // System.out.println("e.toString(): " + e);
      // System.out.println("e.getMessage(): " + e.getMessage());

      // Item 63
      // System.out.println("e.getElementName(): " + e.getElementName());

      // Item 61: Throw exceptions approriate to the abstraction
      e.getCause().printStackTrace();
    } finally {
      System.out.println("Inside share's finally ...");
    }
    System.out.println("\nEnd of share");
  }
}
