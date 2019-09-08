package dev.oz.smartworkapi.integration;

import dev.oz.smartworkapi.MainVerticle;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class TestRequestAPI {
  @BeforeAll
  public static void before(VertxTestContext context) {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = Integer.getInteger("http.port", 8080);
  }

  @AfterAll
  public static void tearDown(VertxTestContext context) {
    //vertx.close(context.asyncAssertSuccess());
    RestAssured.reset();
  }

  @Test
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(verticleId -> {

      RestAssured.get("/request/1").then()
        .assertThat()
        .statusCode(200);

      System.out.println("TEST PASSED");
      testContext.completeNow();
    }));

  }

}
