package main.behavior_parameterization;

import main.Apple;

public class AppleGreenColorPredicate implements ApplePredicate {
  @Override
  public boolean test(Apple apple) {
    return apple.getColor().equals("GREEN");
  }
}
