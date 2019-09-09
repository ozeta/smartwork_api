package dev.oz.smartworkapi;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class TestPromise {
  static Vertx vertx;

  @BeforeAll
  static public void before() {
    vertx = Vertx.vertx();
  }

  @Test
  public void test() throws Throwable {
    VertxTestContext testContext = new VertxTestContext();
    System.out.println("TEST");
    Future<String> future = anAsyncAction();
    future.compose(this::anotherAsyncAction)
      .setHandler(
        testContext.succeeding(res->{
          System.out.println("SUCCEEDING: " + res);
          testContext.completeNow();
        }))
      ;

    assertThat(testContext.awaitCompletion(5, TimeUnit.SECONDS)).isTrue();
    if (testContext.failed()) {
      throw testContext.causeOfFailure();
    } else {

      System.out.println("OK");
    }
  }

  private Future<String> anAsyncAction() {
    Future<String> future = Future.future();
    // mimic something that take times
    vertx.setTimer(100, l -> future.complete("world"));
    return future;
  }

  private Future<String> anotherAsyncAction(String name) {
    Future<String> future = Future.future();
    // mimic something that take times
    vertx.setTimer(100, l -> future.complete("hello " + name));
    return future;
  }


}
