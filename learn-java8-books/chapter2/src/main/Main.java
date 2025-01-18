package main;

import main.behavior_parameterization.AppleGreenColorPredicate;
import main.behavior_parameterization.AppleHeavyWeightPredicate;
import main.util.AppleUtils;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    List<Apple> inventory = AppleUtils.createApples();
    List<Apple> greenApples = AppleUtils.filterApples(inventory, new AppleGreenColorPredicate());
    List<Apple> heavyApples = AppleUtils.filterApples(inventory, new AppleHeavyWeightPredicate());

    System.out.println("Green apples: ");
    greenApples.forEach(System.out::println);
    System.out.println("Heavy apples: ");
    heavyApples.forEach(System.out::println);
  }
}
