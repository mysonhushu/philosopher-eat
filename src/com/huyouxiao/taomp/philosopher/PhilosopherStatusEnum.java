package com.huyouxiao.taomp.philosopher;


public enum PhilosopherStatusEnum {
  THINKING(0,"THINKING"), WAITING(1, "WAITING"), EATING(2, "EATING");
  private int priority;
  private String label;
  private PhilosopherStatusEnum(int priority, String label) {
    this.priority = priority;
    this.label = label;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
}
