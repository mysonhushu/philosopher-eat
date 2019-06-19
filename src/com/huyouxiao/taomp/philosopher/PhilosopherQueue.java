package com.huyouxiao.taomp.philosopher;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class PhilosopherQueue {
  private static final Logger log = Logger.getLogger("PhilosopherQueue");

  public static final Executor executor =  Executors.newFixedThreadPool(8);
  public static transient List<Philosopher> PHILOSOPHER_LIST = new ArrayList();


  public static void queue(Philosopher user) {
    synchronized (PHILOSOPHER_LIST) {
      if(PHILOSOPHER_LIST.contains(user)) {
        log.warning("philosopher #"+user.getSeatNumber()+" is already in queue." );
        System.exit(-1);
      }
      PHILOSOPHER_LIST.add(user);
    }
  }

  public static void remove(Philosopher user) {
    synchronized (PHILOSOPHER_LIST) {
      if(!PHILOSOPHER_LIST.contains(user)) {
        log.warning("philosopher #"+user.getSeatNumber()+" is already in queue." );
        System.exit(-1);
      }
      PHILOSOPHER_LIST.remove(user);
    }
  }


  public static void tryEat(Philosopher user) {
    synchronized (PHILOSOPHER_LIST) {
      Collections.sort(PHILOSOPHER_LIST, new PhilosopherComparator());
      log.info("-------PHILOSOPHER_LIST sequence-----");
      for(Philosopher item : PHILOSOPHER_LIST) {
        log.info("--- philosopher #"+item.getSeatNumber()+" startWaitingTime:"+item.getStartWaitTime()+" eat time counts:"+item.getEatTimeCounts());
      }

      Philosopher next = PHILOSOPHER_LIST.get(0);
      // if my turn, start to eat.
      if(user.getSeatNumber().intValue() == next.getSeatNumber().intValue()) {
        log.info("Philosopher #"+user.getSeatNumber()+" is the first one in queue. start to eat.. ");
        user.setStatus(PhilosopherStatusEnum.EATING);
        return;
      }

      // if only two philosopher seat.
      if(PHILOSOPHER_LIST.size() == 2 || PHILOSOPHER_LIST.size() == 3) {
        user.setStatus(PhilosopherStatusEnum.WAITING);
        return;
      }

      // seat at the end of position.
      if(user.getSeatNumber().intValue() == PHILOSOPHER_LIST.size()) {
        if(next.getSeatNumber() == 1 || next.getSeatNumber() == PHILOSOPHER_LIST.size()-1) {
          log.info("philosopher #"+user.getSeatNumber()+" is blocked by philosopher #"+next.getSeatNumber()+" please wait.");
          user.setStatus(PhilosopherStatusEnum.THINKING);
          return;
        }
        log.info("philosopher # "+user.getSeatNumber()+"is not the first one. and the first one is not seat nealy by ");
        Philosopher secondOne = PHILOSOPHER_LIST.get(1);
        if(secondOne.getSeatNumber().intValue() == user.getSeatNumber().intValue()) {
          log.info("philosopher # "+user.getSeatNumber()+"is the second one. allowed to eat.");
          user.setStatus(PhilosopherStatusEnum.EATING);
          return;
        }
        // seat at the first position
      } else if(user.getSeatNumber().intValue() == 1) {
        if(next.getSeatNumber() == 2 || next.getSeatNumber() == PHILOSOPHER_LIST.size()) {
          log.info("philosopher #"+user.getSeatNumber()+" is blocked by philosopher #"+next.getSeatNumber()+" please wait.");
          user.setStatus(PhilosopherStatusEnum.THINKING);
          return;
        }
        log.info("philosopher # "+user.getSeatNumber()+" is not the first one. and the first one is not set nealy by ");
        Philosopher secondOne = PHILOSOPHER_LIST.get(1);
        if(secondOne.getSeatNumber().intValue() == user.getSeatNumber().intValue()) {
          log.info("philosopher # "+user.getSeatNumber()+"is the second one. allowed to eat.");
          user.setStatus(PhilosopherStatusEnum.EATING);
          return;
        }
      } else { // if not seat nealy by. start to eat.
        Philosopher secondOne = PHILOSOPHER_LIST.get(1);
        if(Math.abs(next.getSeatNumber() - user.getSeatNumber()) > 1 && Math.abs(user.getSeatNumber() - secondOne.getSeatNumber()) > 1) {
          log.info("philosopher # "+user.getSeatNumber()+" is not the first one, but seat nealy by is not hungry. start to eat..");
          user.setStatus(PhilosopherStatusEnum.EATING);
          return;
        }
        log.info("philosopher #"+user.getSeatNumber()+" should let first philosopher #" + next.getSeatNumber() +" and second philosopher #"+secondOne.getSeatNumber()+" eat first!");
        user.setStatus(PhilosopherStatusEnum.THINKING);
      }
    }
  }
}
