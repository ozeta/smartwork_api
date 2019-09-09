package dev.oz.smartworkapi;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class ConfigurationVerticle extends AbstractVerticle {
  static JsonObject configMap;
  private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void start(Promise<Void> future) {
    ConfigStoreOptions fileStore = new ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setOptional(true)
      .setConfig(new JsonObject().put("path", "application.yaml"));
    ConfigStoreOptions sysPropsStore = new ConfigStoreOptions().setType("sys");

    ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore).addStore(sysPropsStore);

    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    retriever.getConfig(ar -> {
      if (ar.failed()) {
        logger.error(ar.cause().toString());
        future.fail(ar.cause());
        // Failed to retrieve the configuration
      } else {
        logger.info("Parsing configuration file");
        JsonObject config = ar.result();
        configMap = config.getJsonObject("application");
        future.complete();
      }
    });
  }

  @Override
  public void stop() {
  }
}
