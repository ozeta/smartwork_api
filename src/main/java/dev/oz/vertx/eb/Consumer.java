package dev.oz.vertx.eb;

import dev.oz.smartworkapi.dto.Request;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class Consumer extends AbstractVerticle {
  static final String TEST_CHANNEL = "test_channel";
  private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  @Override
  public void start(Promise<Void> future) {
//    MessageCodec<RequestDAO, RequestDAO> codec = new MessageCodec<>();
//    eb.registerCodec(codec);

    this.<Request>consume(TEST_CHANNEL, h -> {
      System.out.println("CONSUMING: " + h.body());
    });
    future.complete();
  }


  private <T> void consume(String channel, Handler<Message<T>> handler) {
    EventBus eb = vertx.eventBus();

    eb.consumer(channel, handler);

  }

  @Override
  public void stop() {
  }

}
