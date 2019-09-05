package dev.oz.smartworkapi.repository.mysql;

import dev.oz.smartworkapi.dto.Request;
import dev.oz.smartworkapi.repository.IRequest;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RequestRepository implements IRequest {
  private SQLConnection connection;

  public RequestRepository(SQLConnection connection) {
    this.connection = connection;
  }

  public Future<Request> getRequest(int result_id) {
    String query = "SELECT id, user, approver, DATE_FORMAT(date,'%Y-%m-%dT%TZ'), status FROM devdb.requests WHERE id = ?;";
    JsonArray params = new JsonArray().add(result_id);
    Promise<Request> promise = Promise.promise();

    connection.queryWithParams(query, params, res -> {
      if (res.succeeded()) {
        ResultSet resultSet = res.result();
        List<JsonArray> results = resultSet.getResults();
        JsonArray row = results.get(0);
        int id = row.getInteger(0);
        String user = row.getString(1);
        String approver = row.getString(2);
        Instant date = row.getInstant(3);
        Request.Status status = Request.Status.get(row.getString(4));
        Request r = new Request(id, user, approver, date, status);
        promise.complete(r);
      } else {
        promise.fail(res.cause());
      }
    }).close();
    return promise.future();
  }

  public Future<Integer> deleteRequest(int id) {
    Promise<Integer> promise = Promise.promise();
    String update = "DELETE FROM devdb.requests WHERE id = ?";
    JsonArray params = new JsonArray().add(id);

    connection.updateWithParams(update, params, res -> {
      if (res.succeeded()) promise.complete(res.result().getUpdated());
      if (res.failed()) promise.fail(res.cause());
    }).close();
    return promise.future();
  }

  public Future<Request> putRequest(Request request) {
    String insert = "UPDATE  devdb.requests SET user = ?, approver = ?, date = ?, status = ? WHERE id = ?;";
    JsonArray params = new JsonArray()
      .add(request.getUser())
      .add(request.getApprover())
      .add(request.getDate())
      .add(request.getStatus())
      .add(request.getId());
    Promise<Request> promise = Promise.promise();
    connection.updateWithParams(insert, params, res -> {
      if (res.succeeded()) promise.complete(request);
      if (res.failed()) promise.fail(res.cause());
    }).close();
    return promise.future();
  }

  public Future<Request> postRequest(Request request) {
    String insert = "INSERT into devdb.requests (user, approver, date, status) VALUES (?, ?, ?, ?);";

    String date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
      .withZone(ZoneId.systemDefault())
      .format(request.getDate());
    JsonArray params = new JsonArray()
      .add(request.getUser())
      .add(request.getApprover())
      .add(date)
      .add(request.getStatus());
    Promise<Request> promise = Promise.promise();
    connection.updateWithParams(insert, params, res -> {

      UpdateResult result = res.result();
      request.setId(result.getKeys().getInteger(0));
      if (res.succeeded()) promise.complete(request);
      if (res.failed()) promise.fail(res.cause());
    }).close();
    return promise.future();
  }
}
