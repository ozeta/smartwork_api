package dev.oz.smartworkapi;

import dev.oz.smartworkapi.openapi.RequestHandlerVerticle;
import dev.oz.smartworkapi.openapi.RequestHttpServerVerticle;
import dev.oz.smartworkapi.repository.RequestMysqlVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Set;

import static java.lang.System.exit;


public class ApplicationBootstrapper {
  private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static void main(String[] args) {
    start(Promise.promise());
  }

  static private Future<String> prepareVerticle(Vertx vertx, Verticle verticle) {
    Promise<String> promise = Promise.promise();
    vertx.deployVerticle(verticle, res -> {
      if (res.succeeded()) {
        promise.complete(res.result());
      } else {
        promise.fail(res.cause());
      }
    });
    return promise.future();
  }

  public static void start(Promise<Void> startPromise) {
    Vertx vertx = Vertx.vertx();
    Future<String> steps =
      //prepareVerticle(vertx, new ConfigurationVerticle())
      prepareVerticle(vertx, new ConfigurationVerticle())
      .compose(v -> prepareVerticle(vertx, new RequestMysqlVerticle()))
      .compose(v -> prepareVerticle(vertx, new RequestHttpServerVerticle()))
      .compose(v -> prepareVerticle(vertx, new RequestHandlerVerticle()));

    steps.setHandler(ar -> {
      if (ar.succeeded()) {
        logger.info("Promise Bootstrap complete \uD83D\uDEC2");
        startPromise.complete();
      } else {
        logger.error("Bootstrap failed");
        logger.error(ar.cause().toString());
        startPromise.fail(ar.cause().toString());
        Set<String> strings = vertx.deploymentIDs();
        strings.forEach(str->{
          logger.info("UNdeploying verticle " + str);
          vertx.undeploy(str);
        });
        exit(1);
      }
    });
  }


}
