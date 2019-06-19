package com.huyouxiao.taomp.philosopher;



public class ChopstickPool {
  public static final Chopstick[] CHOPSTICK_POOL = new Chopstick[]{
     new ChopstickImpl(1), new ChopstickImpl(2), new ChopstickImpl(3), new ChopstickImpl(4), new ChopstickImpl(5)
  };

  public static Chopstick getByIndex(Integer index) {
    if(index >= CHOPSTICK_POOL.length) {
      return CHOPSTICK_POOL[0];
    }
    return CHOPSTICK_POOL[index];
  }
}
