package main.behavior_parameterization;

import main.Apple;

@FunctionalInterface
public interface ApplePredicate {
  boolean test(Apple apple);
}
