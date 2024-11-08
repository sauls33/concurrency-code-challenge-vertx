package coe.unosquare.service;

import coe.unosquare.model.Order;
import coe.unosquare.model.OrderMatcher;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class OrderService {
    private final OrderMatcher orderMatcher;
    private final Vertx vertx;

    public OrderService(Vertx vertx, OrderMatcher orderMatcher) {
        this.vertx = vertx;
        this.orderMatcher = orderMatcher;
    }

    public Future<String> processOrder(Order order) {
        return vertx.executeBlocking(promise -> {
            try {
                // Process the order and return the result
                orderMatcher.addOrder(order);
                orderMatcher.matchOrders();
                promise.complete("Order processed: " + order);
            } catch (Exception e) {
                promise.fail(e);
            }
        });
    }
}
