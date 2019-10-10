package dev.oz.vertx.eb;

import dev.oz.smartworkapi.RequestDAO;
import dev.oz.smartworkapi.dto.Request;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer extends AbstractVerticle {
  static final String TEST_CHANNEL = "test_channel";
  private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  @Override
  public void start(Promise<Void> future) {
    MessageCodec<Request, Request> codec = new MessageCodec<>();
    EventBus eb = vertx.eventBus();

    eb.registerCodec(codec);
    AtomicInteger i = new AtomicInteger(0);
    vertx.setPeriodic(1000, handler -> {

      DeliveryOptions dopt = new DeliveryOptions();
      dopt.setCodecName(codec.name());

      Request dao = new Request();
      dao.setId(i.getAndIncrement());

      this.publish(TEST_CHANNEL, dao, dopt);
    });
    future.complete();
  }


  private <T> void publish(String channel, T s, DeliveryOptions dopt) {
    EventBus eb = vertx.eventBus();
    eb.<T>publish(channel, s, dopt);

  }

  @Override
  public void stop() {
  }

}
