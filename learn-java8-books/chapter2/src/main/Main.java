package main;

import static main.util.AppleColor.GREEN;

import java.util.List;
import main.behavior_parameterization.AppleFancyFormatter;
import main.behavior_parameterization.AppleSimpleFormatter;
import main.util.Utils;

public class Main {
  public static void main(String[] args) {
    List<Apple> inventory = Utils.createApples();
    List<Apple> greenApples =
        Utils.filter(inventory, (Apple apple) -> GREEN.name().equals(apple.getColor()));
    List<Apple> heavyApples = Utils.filter(inventory, (Apple apple) -> apple.getWeight() > 150);

    System.out.println("Green apples: ");
    Utils.prettyPrintApple(greenApples, new AppleFancyFormatter());
    System.out.println("Heavy apples: ");
    Utils.prettyPrintApple(heavyApples, new AppleSimpleFormatter());
  }
}
