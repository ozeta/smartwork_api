package dev.oz.smartworkapi.openapi;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;

public class RequestHttpServerVerticle extends AbstractVerticle {

  private HttpServer server;


  @Override
  public void start(Promise<Void> future) {

    OpenAPI3RouterFactory.create(this.vertx, "api.yaml", ar -> {
      if (ar.succeeded()) {
        OpenAPI3RouterFactory routerFactory = ar.result();

        routerFactory.addHandlerByOperationId("getRequestById", RequestHandlerVerticle::getRequestById);
        routerFactory.addHandlerByOperationId("putRequestById", RequestHandlerVerticle::putRequestById);
        routerFactory.addHandlerByOperationId("deleteRequestById", RequestHandlerVerticle::deleteRequestById);
        routerFactory.addHandlerByOperationId("postRequest", RequestHandlerVerticle::postRequest);

        Router router = routerFactory.getRouter();
        router.errorHandler(404, routingContext -> {
          JsonObject errorObject = new JsonObject()
            .put("code", 404)
            .put("message",
              (routingContext.failure() != null) ?
                routingContext.failure().getMessage() :
                "Not Found"
            );
          routingContext
            .response()
            .setStatusCode(404)
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .end(errorObject.encode());
        });
        router.errorHandler(400, routingContext -> {
          JsonObject errorObject = new JsonObject()
            .put("code", 400)
            .put("message",
              (routingContext.failure() != null) ?
                routingContext.failure().getMessage() :
                "Validation Exception"
            );
          routingContext
            .response()
            .setStatusCode(400)
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .end(errorObject.encode());
        });

        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"));
        server.requestHandler(router).listen();
        future.complete();
      } else {
        future.fail(ar.cause());
      }
    });
  }

  @Override
  public void stop() {
    this.server.close();
  }
}
