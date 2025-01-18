package main.util;

import main.Apple;
import main.behavior_parameterization.AppleFormatter;
import main.behavior_parameterization.ApplePredicate;

import java.util.ArrayList;
import java.util.List;

public class AppleUtils {
  public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate predicate) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
      if (predicate.test(apple))
        result.add(apple);
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
    List<Apple> inventory = new ArrayList<>();
    inventory.add(new Apple("RED", 100));
    inventory.add(new Apple("GREEN", 100));
    inventory.add(new Apple("RED", 160));
    inventory.add(new Apple("GREEN", 160));
    return inventory;
  }
}
