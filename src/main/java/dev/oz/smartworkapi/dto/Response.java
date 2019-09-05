package dev.oz.smartworkapi.dto;

public class Response<T> {
  private int code;
  private String message;
  private T body;

  public Response(int code, String message, T body) {
    this(code, message);
    this.body = body;
  }

  public Response(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public Response setCode(int code) {
    this.code = code;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public Response setMessage(String message) {
    this.message = message;
    return this;
  }

  public T getBody() {
    return body;
  }

  public Response setBody(T body) {
    this.body = body;
    return this;
  }
}
