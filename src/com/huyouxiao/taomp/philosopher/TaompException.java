package com.huyouxiao.taomp.philosopher;

import java.util.ArrayList;
import java.util.List;

public class TaompException extends RuntimeException {
  private String error;
  private List<String> parameters;

  public TaompException() {
  }

  public TaompException(String message) {
    super(message);
    this.error = message;
  }

  public TaompException(String message, List<String> parameters) {
    super(message);
    this.error = message;
    this.parameters = parameters;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public void setParameters(List<String> parameters) {
    this.parameters = parameters;
  }

  public List<String> getParameters() {
    return this.parameters;
  }

  public TaompException addParameter(Object parameter) {
    if (null == this.parameters) {
      this.parameters = new ArrayList<>();
    }
    this.parameters.add(String.valueOf(parameter));
    return this;
  }

  public static TaompException build(String... values) {
    String message = values[0];
    List<String> temp = null;
    if (values.length > 1) {
      temp = new ArrayList<>(values.length - 1);
      for (int i = 1; i < values.length; i++) {
        temp.add(values[i]);
      }
    }
    return new TaompException(message, temp);
  }
}
