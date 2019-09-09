package dev.oz.smartworkapi.repository;

import dev.oz.smartworkapi.dto.Request;
import dev.oz.smartworkapi.repository.mysql.RequestRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class RequestMysqlVerticle extends AbstractVerticle {
  private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static String ebAddress = "smartworkapi.mysql";
  MessageConsumer<JsonObject> consumer;
  RequestRepository requestRepository;
  private SQLConnection connection;

  private void startConsumer() {
    consumer = vertx.eventBus().consumer(ebAddress, message -> {
      JsonObject body = message.body();
      Future<Request> res;
      switch (message.headers().get("action")) {
        case "GET":
          res = requestRepository.getRequest(body.getInteger("id"));
          res.setHandler(asyn -> {
            if (asyn.succeeded()) {
              Request result = asyn.result();
              message.reply(Request.toJson(result));
            } else {
              message.fail(404, "object not found for id " + body.getInteger("id"));
            }
          });
          break;
        case "PUT":
          break;

        case "POST":
          res = requestRepository.postRequest(Request.fromJson(body));
          res.setHandler(asyn -> {
            if (asyn.succeeded()) {
              Request result = asyn.result();
              message.reply(Request.toJson(result));
            } else {
              message.fail(404, "object not found for id " + body.getInteger("id"));
            }
          });
          break;
        case "DELETE":
          Future<Integer> deleted = requestRepository.deleteRequest(body.getInteger("id"));
          deleted.setHandler(asyn -> {
            if (asyn.succeeded()) {
              Integer result = asyn.result();
              message.reply(new JsonObject().put("deleted", result));
            } else {
              message.fail(404, "object not found for id " + body.getInteger("id"));
            }
          });
          break;

      }
    });
  }


  @Override
  public void start(Promise<Void> future) {
    JsonObject mySQLClientConfig = new JsonObject()
      .put("host", "192.168.90.110")
      .put("username", "dbuser")
      .put("password", "dbpassword")
      .put("database", "devdb")
      .put("port", 3306);
    SQLClient client = MySQLClient.createShared(vertx, mySQLClientConfig, "RequestMySQLPool");
    client.getConnection(ar -> {
      if (ar.succeeded()) {
        connection = ar.result();
        requestRepository = new RequestRepository(connection);
        startConsumer();
        future.complete();
      } else {
        logger.error("Cannot connect to DB");
        future.fail(ar.cause());
      }
    });
  }

  @Override
  public void stop() {
  }
}
