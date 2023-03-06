package com.grekoff.market.core.controllers;

import com.grekoff.market.core.services.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;

    // http://localhost:8189/market-core/api/v1/orders
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestHeader String username) {

        ordersService.createOrder(username);
    }
}
