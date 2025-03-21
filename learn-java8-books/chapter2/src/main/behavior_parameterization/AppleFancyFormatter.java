package main.behavior_parameterization;

import main.Apple;

public class AppleFancyFormatter implements AppleFormatter {
  @Override
  public String accept(Apple apple) {
    String characteristic = apple.getWeight() > 150 ? "heavy" : "light";
    return "A " + characteristic + " " + apple.getColor() + " apple";
  }
}
