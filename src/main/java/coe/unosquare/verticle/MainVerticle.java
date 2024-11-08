package coe.unosquare.verticle;

import coe.unosquare.model.ApiResponse;
import coe.unosquare.model.Order;
import coe.unosquare.model.OrderMatcher;
import coe.unosquare.service.OrderService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;


public class MainVerticle extends AbstractVerticle {

    private OrderService orderService;

    @Override
    public void start(Promise<Void> startPromise) { // Start the verticle
        orderService = new OrderService(vertx, new OrderMatcher());

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        // Define  endpoint to submit orders
        router.post("/orders/submit").handler(this::submitOrder);

        // Start server on port 8085
        vertx.createHttpServer().requestHandler(router).listen(8085, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("Server started on port 8085");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }

    private void submitOrder(RoutingContext context) {
        try {
            String orderTypeParam = context.request().getParam("ordertype");
            double price = Double.parseDouble(context.request().getParam("price"));
            int quantity = Integer.parseInt(context.request().getParam("quantity"));

            Order.OrderType orderType = Order.OrderType.valueOf(orderTypeParam.toUpperCase());
            Order order = new Order(orderType, price, quantity);

            orderService.processOrder(order).onSuccess(result -> {
                context.response()
                        .setStatusCode(201)
                        .putHeader("content-type", "application/json")
                        .end(new ApiResponse(true, "Order processed successfully", result).convertToJson());
            }).onFailure(error -> {
                context.response()
                        .setStatusCode(400)
                        .putHeader("content-type", "application/json")
                        .end(new ApiResponse(false, "Order processing failed", error.getMessage()).convertToJson());
            });

        } catch (Exception e) {
            context.response()
                    .setStatusCode(400)
                    .putHeader("content-type", "application/json")
                    .end(new ApiResponse(false, "Invalid request parameters", e.getMessage()).convertToJson());
        }
    }

}
