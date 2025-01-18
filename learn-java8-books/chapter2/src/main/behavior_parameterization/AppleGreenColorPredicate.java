package main.behavior_parameterization;

import main.Apple;
import main.util.AppleColor;

public class AppleGreenColorPredicate implements ApplePredicate {
  @Override
  public boolean test(Apple apple) {
    return apple.getColor().equals(AppleColor.GREEN.toString());
  }
}
