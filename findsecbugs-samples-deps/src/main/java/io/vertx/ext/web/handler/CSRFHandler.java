package io.vertx.ext.web.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

public interface CSRFHandler extends Handler<RoutingContext>  {

    static CSRFHandler create(Vertx vertx, String secret) {
        return null;
    }
}
