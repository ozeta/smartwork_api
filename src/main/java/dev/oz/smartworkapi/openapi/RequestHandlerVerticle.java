package dev.oz.smartworkapi.openapi;

import dev.oz.smartworkapi.dto.Event;
import dev.oz.smartworkapi.dto.Request;
import dev.oz.smartworkapi.dto.Response;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandlerVerticle extends AbstractVerticle {

  static EventBus eb;
  private static Logger logger = LoggerFactory.getLogger(RequestHandlerVerticle.class);

  private static String EB_MYSQL_ASYN = "smartworkapi.mysql";
  private static String EB_MYSQL_JDBC = "smartworkapi.mysql.jdbc";

  private static DeliveryOptions deliveryOptions(String action) {
    return new DeliveryOptions().addHeader("action", action);
  }

  static void getRequestById(RoutingContext rc) {
    logger.info("Received GET request");
    Integer request_id;
    try {
      request_id = Integer.valueOf(rc.pathParam("request_id"));
    } catch (NumberFormatException e) {
      Response<Void> httpResponse = new Response<>(400, "id is not valid");
      rc
        .response()
        .setStatusCode(400)
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .end(JsonObject.mapFrom(httpResponse).encode());
      return;
    }
    eb.<JsonObject>request(EB_MYSQL_ASYN, new JsonObject().put("id", request_id), deliveryOptions(Event.Action.GET.name()), reply -> {
      if (reply.succeeded()) {
        logger.info("Object Found");
        JsonObject body = reply.result().body();
        Response<Request> httpResponse = new Response<>(200, "", Request.fromJson(body));
        rc
          .response()
          .setStatusCode(200)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(JsonObject.mapFrom(httpResponse).encode());
      } else {
        logger.info("Object not Found");
        Response<Void> httpResponse = new Response<>(404, "");
        rc
          .response()
          .setStatusCode(404)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(JsonObject.mapFrom(httpResponse).encode());
      }
    });
  }

  static void deleteRequestById(RoutingContext rc) {
    logger.info("Received DELETE request");
    Integer request_id;
    try {
      request_id = Integer.valueOf(rc.pathParam("request_id"));
    } catch (NumberFormatException e) {
      Response<Void> httpResponse = new Response<>(400, "id is not valid");
      rc
        .response()
        .setStatusCode(400)
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .end(JsonObject.mapFrom(httpResponse).encode());
      return;
    }

    eb.<JsonObject>request(EB_MYSQL_ASYN, new JsonObject().put("id", request_id), deliveryOptions(Event.Action.DELETE.name()), reply -> {
      if (reply.succeeded()) {
        logger.info("Object Deleted");
        Integer deleted = reply.result().body().getInteger("deleted");
        Response<Integer> httpResponse;
        if (deleted > 0) {
          httpResponse = new Response<>(200, "Object deleted");
          rc
            .response()
            .setStatusCode(200);
        } else {
          httpResponse = new Response<>(404, "Object not found");
          rc
            .response()
            .setStatusCode(404);
        }
        rc
          .response()
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(JsonObject.mapFrom(httpResponse).encode());
      } else {
        logger.info("Object not deleted");
        Response<Void> httpResponse = new Response<>(405, reply.cause().toString());
        rc
          .response()
          .setStatusCode(405)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(JsonObject.mapFrom(httpResponse).encode());
      }
    });
  }

  static void postRequest(RoutingContext rc) {
    JsonObject bodyAsJson = rc.getBodyAsJson();

    eb.<JsonObject>request(EB_MYSQL_ASYN, JsonObject.mapFrom(bodyAsJson), deliveryOptions(Event.Action.POST.name()), reply -> {
      if (reply.succeeded()) {
        int CREATED = 201;
        logger.info("Object Found");
        JsonObject body = reply.result().body();
        Response<Request> httpResponse = new Response<>(CREATED, "", Request.fromJson(body));
        rc
          .response()
          .setStatusCode(CREATED)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(JsonObject.mapFrom(httpResponse).encode());
      } else {
        int ERROR = 400;
        logger.info("Object not created");
        Response<Void> httpResponse = new Response<>(ERROR, reply.cause().toString());
        rc
          .response()
          .setStatusCode(ERROR)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(JsonObject.mapFrom(httpResponse).encode());
      }
    });
  }

  static void putRequestById(RoutingContext rc) {
    JsonObject bodyAsJson = rc.getBodyAsJson();

    eb.<JsonObject>request(EB_MYSQL_ASYN, JsonObject.mapFrom(bodyAsJson), deliveryOptions(Event.Action.POST.name()), reply -> {
      if (reply.succeeded()) {
        int CREATED = 201;
        logger.info("Object Found");
        JsonObject body = reply.result().body();
        Response<Request> httpResponse = new Response<>(CREATED, "", Request.fromJson(body));
        rc
          .response()
          .setStatusCode(CREATED)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(JsonObject.mapFrom(httpResponse).encode());
      } else {
        int ERROR = 400;
        logger.info("Object not created");
        Response<Void> httpResponse = new Response<>(ERROR, reply.cause().toString());
        rc
          .response()
          .setStatusCode(ERROR)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(JsonObject.mapFrom(httpResponse).encode());
      }
    });

  }


  @Override
  public void start(Promise<Void> future) {
    eb = vertx.eventBus();
    future.complete();
  }

  @Override
  public void stop() {
  }
}
