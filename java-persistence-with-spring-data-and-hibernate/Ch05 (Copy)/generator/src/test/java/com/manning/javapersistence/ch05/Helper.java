package com.manning.javapersistence.ch05;

import java.util.Date;

public class Helper {
  static Date tomorrow() {
    return new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
  }
}
