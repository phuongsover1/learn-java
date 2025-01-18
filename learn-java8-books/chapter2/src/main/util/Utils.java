package main.util;

import main.Apple;
import main.behavior_parameterization.AppleFormatter;
import main.behavior_parameterization.ApplePredicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static main.util.AppleColor.GREEN;
import static main.util.AppleColor.RED;

public class Utils {
  public static <T> List<T> filter(List<T> inventory, Predicate<T> predicate) {
    List<T> result = new ArrayList<>();
    for (T e : inventory) {
      if (predicate.test(e)) result.add(e);
    }
    return result;
  }

  public static void prettyPrintApple(List<Apple> inventory, AppleFormatter formatter) {
    for (Apple apple : inventory) {
      String output = formatter.accept(apple);
      System.out.println(output);
    }
  }

  public static List<Apple> createApples() {
    return Arrays.asList(
        new Apple(RED.name(), 100),
        new Apple(GREEN.name(), 100),
        new Apple(RED.name(), 300),
        new Apple(GREEN.name(), 160));
  }
}
