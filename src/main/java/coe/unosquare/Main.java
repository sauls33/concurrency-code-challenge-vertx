package coe.unosquare;

import coe.unosquare.verticle.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Main {
    public static void main(String[] args) {
        VertxOptions options = new VertxOptions().setEventLoopPoolSize(8);
        Vertx vertx = Vertx.vertx(options);
        vertx.deployVerticle(new MainVerticle(), res -> {
            if (res.succeeded()) {
                System.out.println("Deployment id is: " + res.result());
            } else {
                System.out.println("Deployment failed!");
            }
        });
    }
}

