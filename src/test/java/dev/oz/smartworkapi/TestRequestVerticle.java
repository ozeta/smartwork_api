package dev.oz.smartworkapi;

import dev.oz.smartworkapi.openapi.RequestHandlerVerticle;
import dev.oz.smartworkapi.openapi.RequestHttpServerVerticle;
import dev.oz.smartworkapi.repository.RequestMysqlVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class TestRequestVerticle {

  @BeforeAll
  @DisplayName("Initialization")
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
/*
    vertx.deployVerticle(new RequestMysqlVerticle());
    vertx.deployVerticle(new RequestHttpServerVerticle());
    vertx.deployVerticle(new RequestHandlerVerticle());*/
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

  @Test
  @DisplayName("🛂 Make a HTTP client request to SampleVerticle")
  void httpRequest(Vertx vertx, VertxTestContext testContext) {

    Future<String> steps = prepareVerticle(vertx, new RequestMysqlVerticle())
      .compose(v -> prepareVerticle(vertx, new RequestHttpServerVerticle()))
      .compose(v -> prepareVerticle(vertx, new RequestHandlerVerticle()));


    WebClient webClient = WebClient.create(vertx);
    webClient.get(11981, "localhost", "/request/1")
      .as(BodyCodec.string())
      .send(testContext.succeeding(resp -> {
        testContext.verify(() -> {
          assertThat(resp.statusCode()).isEqualTo(200);
          System.out.println(resp.body());
          testContext.completeNow();
        });
      }));

  /*  vertx.deployVerticle(new SampleVerticle(), testContext.succeeding(id -> {
      webClient.get(11981, "localhost", "/yo")
        .as(BodyCodec.string())
        .send(testContext.succeeding(resp -> {
          testContext.verify(() -> {
            assertThat(resp.statusCode()).isEqualTo(200);
            assertThat(resp.body()).contains("Yo!");
            testContext.completeNow();
          });
        }));
    }));*/
  }
  @AfterAll
  @DisplayName("Later Cleanup")
  void lastChecks(Vertx vertx, VertxTestContext testContext) {

    testContext.completeNow();
  }
}
