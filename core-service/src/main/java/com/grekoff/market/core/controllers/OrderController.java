package com.grekoff.market.core.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.grekoff.market.core.converters.OrderConverter;
import com.grekoff.market.core.services.OrdersService;
import com.grekoff.market.api.OrderDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;
    private final OrderConverter orderConverter;

    // http://localhost:8189/market-core/api/v1/orders

    @GetMapping
    public List<OrderDto> getUserOrders(@RequestHeader String username) {

        return ordersService.findUserOrders(username).stream().map(orderConverter::entityToDto).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestHeader String username) {
        ordersService.createOrder(username);
    }
}
