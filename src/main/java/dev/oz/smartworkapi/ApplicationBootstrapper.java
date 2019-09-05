package dev.oz.smartworkapi;

import dev.oz.smartworkapi.openapi.RequestHandlerVerticle;
import dev.oz.smartworkapi.openapi.RequestHttpServerVerticle;
import dev.oz.smartworkapi.repository.RequestMysqlVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;


public class ApplicationBootstrapper {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Future<String> steps = prepareDatabaseVerticle(vertx)
      .compose(v -> startHttpVerticle(vertx))
      .compose(v -> startHandlerVerticle(vertx));

    Promise<String> promise = Promise.promise();

    steps.setHandler(ar -> {
      if (ar.succeeded()) {
        promise.complete();
        System.out.println("Bootstrap complete");
      } else {
        promise.fail(ar.cause());
        System.out.println("Bootstrap failed");
        System.out.println(ar.cause().toString());
      }
    });

  }

  static private Future<String> prepareDatabaseVerticle(Vertx vertx) {
    Promise<String> promise = Promise.promise();
    vertx.deployVerticle(new RequestMysqlVerticle(), res -> {
      if (res.succeeded()) {
        promise.complete(res.result());
      } else {
        promise.fail(res.cause());
      }
    });
    return promise.future();
  }

  static private Future<String> startHttpVerticle(Vertx vertx) {
    Promise<String> promise = Promise.promise();
    vertx.deployVerticle(new RequestHttpServerVerticle(), res -> {
      if (res.succeeded()) {
        promise.complete(res.result());
      } else {
        promise.fail(res.cause());
      }
    });
    return promise.future();
  }

  static private Future<String> startHandlerVerticle(Vertx vertx) {
    Promise<String> promise = Promise.promise();
    vertx.deployVerticle(new RequestHandlerVerticle(), res -> {
      if (res.succeeded()) {
        promise.complete(res.result());
      } else {
        promise.fail(res.cause());
      }
    });
    return promise.future();
  }


}
