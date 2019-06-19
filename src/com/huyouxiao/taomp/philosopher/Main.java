package com.huyouxiao.taomp.philosopher;

public class Main {
  public static void main(String args[]) {
    Philosopher a = new PhilosopherImpl(1);
    a.live();
    Philosopher b = new PhilosopherImpl(2);
    b.live();
    Philosopher c = new PhilosopherImpl(3);
    c.live();
    Philosopher d = new PhilosopherImpl(4);
    d.live();
    Philosopher e = new PhilosopherImpl(5);
    e.live();
  }
}
