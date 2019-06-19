package com.huyouxiao.taomp.philosopher;



import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;


public class PhilosopherImpl implements Philosopher {
  private static final Logger log = Logger.getLogger("PhilosopherImpl");

  private static Random RANDOM = new Random(47);
  private static int SLEEP_MAX_SECONDS = 10;

  private transient PhilosopherStatusEnum status = PhilosopherStatusEnum.THINKING;
  private Integer seatNumber;
  private Chopstick leftChopstick;
  private Chopstick rightChopstick;
  private transient Integer eatCount = 0;
  private transient Date startWaitTime = null;

  public PhilosopherImpl(Integer value) {
    this.seatNumber = value;
    leftChopstick = ChopstickPool.getByIndex(this.seatNumber);
    rightChopstick = ChopstickPool.getByIndex(this.seatNumber -1);
    PhilosopherQueue.queue(this);
  }


  @Override
  public void think(Long thinkSeconds) {
    if(null == startWaitTime) {
      startWaitTime = Calendar.getInstance().getTime();
    }
    if(null == thinkSeconds || thinkSeconds.intValue() == 0) {
      log.info("philosopher #"+seatNumber+" start to think with random time.");
      thinkSeconds =Long.valueOf(RANDOM.nextInt(3));
    }
    log.info("philosopher #"+seatNumber+" think "+thinkSeconds+" seconds");
    if(thinkSeconds>0) {
      waitMillis(thinkSeconds * 1000L);
    }
    status = PhilosopherStatusEnum.WAITING;
  }

  private synchronized boolean freeChopsticks() {
    if( this == leftChopstick.getHolder() && this == rightChopstick.getHolder()) {
      leftChopstick.free(this);
      rightChopstick.free(this);
      status = PhilosopherStatusEnum.THINKING;
      log.warning("philosopher #"+seatNumber+" free left # "+leftChopstick.getId()+" and right # "+rightChopstick.getId()+". start to think." ) ;
      return true;
    }
    return false;
  }

  @Override
  public synchronized void eat() {
    // assert no duplicate calling.
    if(this == leftChopstick.getHolder() || this == rightChopstick.getHolder()) {
      log.finest("philosopher #"+seatNumber+" is eating now, skip eat operation.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      return;
    }
    // generate eating seconds.
    Long eatSeconds = Long.valueOf(RANDOM.nextInt(SLEEP_MAX_SECONDS));

    // hold chopsticks
    leftChopstick.hold(this, eatSeconds);
    rightChopstick.hold(this, eatSeconds);

    // clean start wait time
    startWaitTime = null;

    log.info("philosopher #"+seatNumber+" use left # "+leftChopstick.getId()+" and right # "+rightChopstick.getId()+". need "+eatSeconds+" seconds");
    waitMillis(eatSeconds*1000);
    freeChopsticks();
    eatCount = eatCount + 1;
    status = PhilosopherStatusEnum.THINKING;
  }

  @Override
  public void live() {
    Runnable asyncLive = () -> {
      try {
            while(true) {
              if(null != startWaitTime) {
                Date now = Calendar.getInstance().getTime();
                long waitSeconds = (now.getTime() - startWaitTime.getTime()) / 1000;
                if(waitSeconds>100) {
                  log.finest("philosopher #"+seatNumber+" is hungry to die. wait "+waitSeconds+" seconds.!!!!!!!!!!!!!!!!!!!!!!!!!");
                  PhilosopherQueue.remove(this);
                  return;
                }
              }
              // is already eating. just do nothing.
              if(status == PhilosopherStatusEnum.THINKING) {
                Long thinkSeconds = Math.max(this.leftChopstick.getRemainSeconds(), this.rightChopstick.getRemainSeconds());
                this.think(thinkSeconds);
              } else if(status == PhilosopherStatusEnum.WAITING) {
                PhilosopherQueue.tryEat(this);
              } else if(status == PhilosopherStatusEnum.EATING) {
                this.eat();
              }
            }
      } catch (Exception e) {
        log.info("philosopher #"+seatNumber+" is already dead.");
        e.printStackTrace();
        throw TaompException.build(e.getMessage());
      }
    };
    PhilosopherQueue.executor.execute(asyncLive);
  }

  @Override
  public Integer getSeatNumber() {
    return seatNumber;
  }

  @Override
  public void setStatus(PhilosopherStatusEnum statusEnum) {
    this.status = statusEnum;
  }

  @Override
  public PhilosopherStatusEnum getStatus() {
    return status;
  }

  @Override
  public Date getStartWaitTime() {
    return this.startWaitTime;
  }

  @Override
  public Integer getEatTimeCounts() {
    return this.eatCount;
  }


  private void waitMillis(Long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
