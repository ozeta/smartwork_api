package dev.oz.smartworkapi;

import dev.oz.smartworkapi.openapi.RequestHandlerVerticle;
import dev.oz.smartworkapi.openapi.RequestHttpServerVerticle;
import dev.oz.smartworkapi.repository.RequestMysqlVerticle;
import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  HttpServer server;
  private static Logger logger = LoggerFactory.getLogger(MainVerticle.class);
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ApplicationBootstrapper.start(startPromise);
  }

  @Override
  public void stop() {
    //vertx.undeploy();
  }
}
