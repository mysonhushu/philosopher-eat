package com.huyouxiao.taomp.philosopher;


import java.util.Date;

public interface Philosopher {
  void think(Long thinkSeconds);
  void eat();
  void live();
  Integer getSeatNumber();
  void setStatus(PhilosopherStatusEnum statusEnum);
  PhilosopherStatusEnum getStatus();
  Date getStartWaitTime();
  Integer getEatTimeCounts();
}
