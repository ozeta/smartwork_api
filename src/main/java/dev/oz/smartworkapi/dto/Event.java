package dev.oz.smartworkapi.dto;

public class Event<T> {

  public enum Action {GET, POST, PUT, DELETE}

  class Header {
    private Action action;
    private Object source;
    private Object dest;

  }
  private Header header;
  private T body;

}
