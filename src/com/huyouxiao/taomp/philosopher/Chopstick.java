package com.huyouxiao.taomp.philosopher;

public interface Chopstick {
  boolean free(Philosopher user);
  boolean hold(Philosopher user, Long holdSeconds);
  int getId();
  Philosopher getHolder();
  Long getRemainSeconds();
}
