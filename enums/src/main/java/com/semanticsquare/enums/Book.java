package com.semanticsquare.enums;

public class Book {

  public enum BookGenre {
    BIORAPHY(12) {

      @Override
      public boolean isKidFriendly(int age) {
        for (int i = 0; i < 10; i++) {
          System.out.println("ffjdkfj");
        }
        return age >= minAgeToRead;
      }
    },
    HORROR(15) {
      @Override
      public boolean isKidFriendly(int age) {
        return false;
      }
    };

    // private int minAgeToRead;
    protected int minAgeToRead;

    private BookGenre(int minAgeToRead) {
      this.minAgeToRead = minAgeToRead;
    }

    public int getMinAgeToRead() {
      return minAgeToRead;
    }

    public abstract boolean isKidFriendly(int age);

    // public boolean isKidFriendly(int age) {
    // switch (this) {
    // case BIORAPHY:
    // return age >= minAgeToRead;
    // case HORROR:
    // return false;
    // }
    // return false;
    // }

  }

  class Student {
    int id;
    String name;
    boolean isMale;

    public Student(int id, String name, boolean isMale) {
      this.id = id;
      this.name = name;
      this.isMale = isMale;
    }

    public Student(int id, String name) {
      this(id, name, true);
    }
  }

  // public static void main(String[] args) {
  // for (BookGenre bookGenre : BookGenre.values()) {
  // System.out.println("\nName : " + bookGenre); // toString
  // System.out.println(", name(): " + bookGenre.name());
  // String name = "Le Nguyen Duy Phuong";
  // System.out.println(", Ordinal: " + bookGenre.ordinal() + name);
  // System.out.println(", Declaring Class: " + bookGenre.getClass());
  // System.out.println(", compareTo(HORROR): " +
  // bookGenre.compareTo(BookGenre.HORROR));
  // System.out.println(", equals(HORROR): " +
  // bookGenre.equals(BookGenre.HORROR));
  // System.out.println(" minAgeToRead: " + bookGenre.getMinAgeToRead());
  // System.out.println(" isKidFriendly: " + bookGenre.isKidFriendly(22));
  // }
  //
  // }
}
