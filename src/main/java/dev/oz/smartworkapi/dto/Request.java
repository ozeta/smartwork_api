package dev.oz.smartworkapi.dto;

import io.vertx.core.json.JsonObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;


public class Request {

  private int id;
  private String user;
  private String approver;
  private Instant date;
  private Status status;

  public Request() {
  }

  public Request(String user, String approver, Instant date, Status status) {
    this.user = user;
    this.approver = approver;
    this.date = date;
    this.status = status;
  }

  public Request(int id, String user, String approver, Instant date, Status status) {
    this(user, approver, date, status);
    this.id = id;
  }

  public static JsonObject toJson(Request o) {
    return JsonObject.mapFrom(o);
  }

  public static Request fromJson(JsonObject o) {

    return new Request()
      .setId(o.getInteger("id") != null ? o.getInteger("id") : -1)
      .setUser(o.getString("user"))
      .setApprover(o.getString("approver"))
      .setStatus(Status.get(o.getString("status")))
      .setDate(o.getInstant("date"));
  }

  public int getId() {
    return id;
  }

  public Request setId(int id) {
    this.id = id;
    return this;
  }

  public String getUser() {
    return user;
  }

  public Request setUser(String user) {
    this.user = user;
    return this;
  }

  public String getApprover() {
    return approver;
  }

  public Request setApprover(String approver) {
    this.approver = approver;
    return this;
  }

  public Instant getDate() {
    return date;
  }

  public Request setDate(Instant date) {
    this.date = date;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public Request setStatus(Status status) {
    this.status = status;
    return this;
  }

  public enum Status {
    PENDING("pending"), APPROVED("approved"), REJECTED("rejected");

    private String value;

    Status(String str) {
      value = str;
    }


    static public Status get(String str) {
      for (Status status : Status.values()) {
        if (str.equalsIgnoreCase(status.value)) return status;
      }
      throw new RuntimeException("status" + str + "not found");
    }

  }
}
