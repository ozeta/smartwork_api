//open keyword enable reflection access inside the module, used by some frameworks
open module  dev.oz {
  exports dev.oz.smartworkapi;
  requires vertx.core;
  requires vertx.web;
  requires vertx.sql.common;
  requires slf4j.api;
  requires vertx.mysql.postgresql.client.jasync;
  requires vertx.web.api.contract;
}
