package dev.oz.smartworkapi;

import dev.oz.smartworkapi.dto.Request;

public class RequestDAO {

  public Request get(int id) {
    String query = "SELECT id, user, approver, DATE_FORMAT(date,'%Y-%m-%dT%TZ'), status FROM requests";
    return null;
  }
}
