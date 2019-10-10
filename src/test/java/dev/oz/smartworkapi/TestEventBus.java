package dev.oz.smartworkapi;

import dev.oz.vertx.eb.Consumer;
import dev.oz.vertx.eb.Producer;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class TestEventBus {

  private static final String TEST_BUS = "test_bus";


  @Test
  public void testEventBus(Vertx vertx, VertxTestContext testContext) throws InterruptedException {
    var eb = vertx.eventBus();
    eb.publish(TEST_BUS, "ciao");
    eb.<String>consumer(TEST_BUS, ev -> {
      System.out.println("RECEIVING MESSAGE");
      System.out.println(ev.body());
    });
    System.out.println("CIAO");
    testContext.completeNow();
  }

  @Test
  void deployTest(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new Producer(), testContext.succeeding(id -> {
      System.out.println("ID " + id);
    }));
    vertx.deployVerticle(new Consumer(), testContext.succeeding(id -> {
      System.out.println("ID " + id);
    }));
  }

}
