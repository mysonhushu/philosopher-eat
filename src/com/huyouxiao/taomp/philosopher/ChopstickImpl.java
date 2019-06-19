package com.huyouxiao.taomp.philosopher;


import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class ChopstickImpl implements Chopstick {
  private static final Logger log = Logger.getLogger("ChopstickImpl");
  private int id;
  private Philosopher holder;
  private Long holdSeconds = 0L;
  private Date beginHoldTime = null;

  public ChopstickImpl(int value) {
    this.id = value;
  }

  @Override
  public synchronized boolean free(Philosopher user) {
    if(null == user) {
      log.warning("user is null, invalid call.!!!!!!!!!!!!!!!!!!!!");
      return false;
    }
    if(null == holder) {
      log.warning("holder is null, chopstick is already free.!!!!!!!!!!!!!!!!!!!!!!!!!! ");
      return false;
    }
    if(holder != user) {
      log.warning("holder is not user, forbidden call.!!!!!!!!!!!!!!!!!!!!!!!!");
      return false;
    }
    this.holder = null;
    this.holdSeconds = 0L;
    this.beginHoldTime = null;
    return true;
  }

  @Override
  public synchronized boolean hold(Philosopher user, Long holdSeconds) {
    if(null == user) {
      log.finest("calling user can't be null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      return false;
    }
    if(null != holder && user != holder) {
      log.finest("invalid call!! can't hold chopstick when other one # "+holder.getSeatNumber()+" use!!!!!!!!!!!!!!!!!!!");
      return false;
    }
    holder = user;
    this.holdSeconds = holdSeconds;
    this.beginHoldTime = Calendar.getInstance().getTime();
    return true;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public Philosopher getHolder() {
    return holder;
  }

  @Override
  public Long getRemainSeconds() {
    if(null != beginHoldTime && holdSeconds > 0) {
      Calendar calendar = Calendar.getInstance();
      Date now = calendar.getTime();
      calendar.setTime(beginHoldTime);
      calendar.add(Calendar.SECOND, holdSeconds.intValue());
      Date release = calendar.getTime();
      log.info("philosopher #" + holder.getSeatNumber()+" call getRemainSeconds. now:{}"+now.toLocaleString()+" beginHoldTime:"+beginHoldTime.toLocaleString()+" holdSeconds:"+holdSeconds);
      if(release.after(now)){
        long remainSeconds = (release.getTime() - now.getTime()) / 1000;
        if(remainSeconds > 100) {
          log.finest("philosopher #" + this.holder.getSeatNumber()+" is hold to long, seconds:"+remainSeconds);

        }
        return remainSeconds;
      }
    }
    return Long.valueOf(0);
  }
}
