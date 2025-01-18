package main.behavior_parameterization;

import main.Apple;

@FunctionalInterface
public interface AppleFormatter {
  String accept(Apple apple);
}
