package com.semanticsquare.lambdas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class StreamOperations {
  static class Book implements Comparable<Book> {
    private long isbn;

    private String title;

    private double rating;
    private double price;
    private String source;

    public Book(long isbn, String title, double rating, double price, String source) {
      this.title = title;
      this.rating = rating;
      this.price = price;
      this.source = source;
    }

    @Override
    public String toString() {
      return "Book [isbn="
          + isbn
          + ", title="
          + title
          + ", rating="
          + rating
          + ", price="
          + price
          + ", source="
          + source
          + "]";
    }

    public long getIsbn() {
      return isbn;
    }

    public void setIsbn(long isbn) {
      this.isbn = isbn;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public double getRating() {
      return rating;
    }

    public void setRating(double rating) {
      this.rating = rating;
    }

    public double getPrice() {
      return price;
    }

    public void setPrice(double price) {
      this.price = price;
    }

    public String getSource() {
      return source;
    }

    public void setSource(String source) {
      this.source = source;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (isbn ^ (isbn >>> 32));
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      Book other = (Book) obj;
      if (isbn != other.isbn) return false;
      return true;
    }

    @Override
    public int compareTo(Book o) {
      return Long.valueOf(isbn).compareTo(Long.valueOf(o.getIsbn()));
    }
  }

  public static void main(String[] args) {
    List<Book> books = new ArrayList<>();

    books.addAll(DataExtractor.getFromAmazon("java"));
    books.addAll(DataExtractor.getFromBarnesAndNoble("java"));

    // slice(books);
    // match(books);

    // find(books);

    // reduce(books);

    // overloadedReductions();
    // mutableReduction();

    // collectToCollection(books);
    collectToMap(books);
  }

  /**
   * Print at most 5 DISTINCT books with rating >= 4.5 DB world: select distinct (ISBN) from book
   * where rating >= 4.5 limit 0, 5;
   */
  private static void slice(List<Book> books) {
    System.out.println("\nSlice ...");

    // List<String> bookTitle = books.stream()
    // .filter(b -> b.getRating() >= 4.5)
    // .distinct()
    // //.limit(5)
    // .map(b -> b.getTitle())
    // // .forEach(System.out::println);
    // .collect(Collectors.toList());

    // bookTitle.forEach(title -> System.out.println("title: " + title));

    Stream<Book> bookStream = books.stream().filter(b -> b.getRating() >= 4.5).distinct().limit(5);

    Stream<String> titleStream = bookStream.map(b -> b.getTitle());
    titleStream.forEach(t -> System.out.println("title: " + t));
  }

  /**
   * Queries: (a) Is there at least one highly rated book (>=4.8) that is inexpensive(<= $50) ? (b)
   * Do all the books have a rating >= 4.8 (c) Check if none of books have bad rating (2.0) ?
   */
  private static void match(List<Book> books) {
    System.out.println("\nMatching ...");
    boolean anyMatch = books.stream().anyMatch(b -> b.getRating() >= 4.8 && b.getPrice() <= 50);
    System.out.println("anyMatch? " + anyMatch);

    boolean allMatch = books.stream().noneMatch(b -> b.getRating() < 4.8);
    System.out.println("allMatch? " + allMatch);

    boolean noneMatch = books.stream().noneMatch(b -> b.getRating() <= 2.0);
    System.out.println("noneMatch? " + noneMatch);
  }

  /**
   * findFirst needs more work in parallel env. Use findAny if it does the job. java.util.Optional ~
   * (a) to avoid dealing with null -- in case of find. (b) to know if stream is empty -- in case of
   * reduction operation
   */
  private static void find(List<Book> books) {
    System.out.println("\nFinding ...");
    // Optional<Book> result =
    books.stream()
        .filter(b -> b.getRating() >= 4.8 && b.getPrice() <= 50.0)
        .findAny()
        .orElseGet(StreamOperations::getDefault);
    // .findFirst();

    Optional<Book> opt = Optional.of(books.get(0));
    System.out.println(opt.get());
  }

  private static Book getDefault() {
    System.out.println("in getDefault() ...");
    return new Book(0, "defaul", 2.0, 10.0, "Amazon");
  }

  /** Find the lowest priced book with a rating >= 4.5 */
  private static void reduce(List<Book> books) {
    System.out.println("\nin reduce() ...");
    books.stream()
        .filter(b -> b.getRating() >= 4.5)
        .reduce((b1, b2) -> b1.getPrice() <= b2.getPrice() ? b1 : b2)
        .ifPresent(b -> System.out.println("Lowest priced: " + b));
  }

  private static void overloadedReductions() {
    System.out.println("\noverloadedReductions ...");

    String[] grades = {"A", "A", "B"};

    String concat1 = Arrays.stream(grades).reduce("", (s1, s2) -> s1 + s2);
    System.out.println("concat1: " + concat1);

    StringBuilder concat2 =
        Arrays.stream(grades)
            .parallel()
            .reduce(new StringBuilder(), (sb, s) -> sb.append(s), (sb1, sb2) -> sb1.append(sb2));
    System.out.println("concat2: " + concat2);

    // Not efficient: Each accumulation step creates a new StringBuilder
    StringBuilder concat3 =
        Arrays.stream(grades)
            .parallel()
            .reduce(
                new StringBuilder(),
                (sb, s) -> new StringBuilder().append(sb).append(s),
                (sb1, sb2) -> sb1.append(sb2));
    System.out.println("concat3: " + concat3);
  }

  private static void collectToCollection(List<Book> books) {
    System.out.println("\nIn collectToCollection ...");
    List<Book> list1 =
        books.stream()
            .filter(b -> b.getRating() >= 4.5)
            .distinct()
            .collect(Collectors.toCollection(() -> new LinkedList<>()));
    System.out.println("list1.size: " + list1.size());

    Set<Book> set1 = books.stream().filter(b -> b.getRating() >= 4.5).collect(Collectors.toSet());
    System.out.println("SET 1: ");
    set1.forEach(System.out::println);

    Set<Book> set2 =
        books.stream()
            .filter(b -> b.getRating() >= 4.5)
            .collect(Collectors.toCollection(() -> new TreeSet<>()));

    System.out.println("SET 2: ");
    set2.forEach(System.out::println);
  }

  /** if accumulator mutabes, use collect(). Otherwise, use reduce() */
  private static void mutableReduction() {
    System.out.println("\nmutableReduction ...");
    String[] grades = {"A", "A", "B"};

    StringBuilder result =
        Arrays.stream(grades)
            .parallel()
            .collect(
                () -> new StringBuilder(), (sb, s) -> sb.append(s), (sb1, sb2) -> sb1.append(sb2));
    System.out.println("concat3: " + result);

    String concatWithJoining = Arrays.stream(grades).parallel().collect(Collectors.joining());

    System.out.println("\nconcatWithJoining: " + concatWithJoining);
  }

  private static void collectToMap(List<Book> books) {
    System.out.println("\nIn collecToMap: ....");
    Map<Long, Book> map =
        books.stream()
            .collect(
                Collectors.toMap(
                    b -> b.getIsbn(),
                    b -> b,
                    (b1, b2) -> b1.getPrice() <= b2.getPrice() ? b1 : b2));

    // map.forEach((k, b) -> System.out.println(map.get(k)));
    for (Entry<Long, Book> entry : map.entrySet()) {
      System.out.println("key: " + entry.getKey() + " , value: " + entry.getValue());
    }

    System.out.println("instance of HashMap: " + (map instanceof HashMap));

    Map<Long, Book> treeMap =
        books.stream()
            .collect(
                Collectors.toMap(
                    b -> b.getIsbn(),
                    b -> b,
                    (b1, b2) -> b1.getPrice() <= b2.getPrice() ? b1 : b2,
                    () -> new TreeMap<>()));

    System.out.println("TreeMap: .........");
    for (Entry<Long, Book> entry : treeMap.entrySet()) {
      System.out.println("key: " + entry.getKey() + " , value: " + entry.getValue());
    }

    System.out.println("TreeMap1: ............");
    Map<Long, Book> treeMap1 =
        books.stream()
            .collect(
                Collectors.toMap(
                    Book::getIsbn,
                    Function.identity(),
                    (b1, b2) -> b1.getPrice() <= b2.getPrice() ? b1 : b2,
                    TreeMap::new));
    for (Entry<Long, Book> entry : treeMap1.entrySet()) {
      System.out.println("key: " + entry.getKey() + " , value: " + entry.getValue());
    }

    Map<Double, List<Book>> map1 =
        treeMap1.values().stream()
            .collect(
                Collectors.toMap(
                    Book::getRating,
                    b -> Collections.singletonList(b),
                    (l1, l2) -> {
                      ArrayList<Book> l = new ArrayList<>(l1);
                      l.addAll(l2);
                      return l;
                    }));
    System.out.println("Map1 ..........");
    for (Entry<Double, List<Book>> entry : map1.entrySet()) {
      System.out.println("key: " + entry.getKey() + " , value: " + entry.getValue());
    }

    // Map<Double,List<Book>> map2 =
    treeMap1.values().stream().collect(Collectors.group);
    System.out.println("Map2 ..........");
    for (Entry<Double, List<Book>> entry : map2.entrySet()) {
      System.out.println("key: " + entry.getKey() + " , value: " + entry.getValue());
    }
  }
}
