package com.grekoff.market.api.cart.controllers;


import com.grekoff.market.api.StringResponse;
import com.grekoff.market.api.cart.converters.CartConverter;
import com.grekoff.market.api.cart.services.CartService;
import com.grekoff.market.api.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
//@CrossOrigin("*")
public class CartController {
    private final CartService cartService;
    private final CartConverter cartConverter;

    // http://localhost:8190/market-cart/api/v1/cart

    @GetMapping("/generate_id")
    public StringResponse generateGuestCartId() {
        return new StringResponse(UUID.randomUUID().toString());
    }

//    @GetMapping
//    public CartDto getCurrentCart() {
//        return cartConverter.entityToDto(cartService.getCurrentCart());
//    }

    @GetMapping("/{guestCartId}")
    public CartDto getCurrentCart(@RequestHeader(required = false) String username, @PathVariable String guestCartId) {
        String currentCartId = cartService.selectCartId(username, guestCartId);
        return cartConverter.entityToDto(cartService.getActualCart(currentCartId, guestCartId));
    }

//    @GetMapping("/add/{productId}")
//    public void addProductToCart(@PathVariable Long productId) {
//        cartService.addToCart(productId);
//    }

    @GetMapping("/{guestCartId}/add/{productId}")
    public void addProductToCart(@RequestHeader(required = false) String username, @PathVariable String guestCartId, @PathVariable Long productId) {
        String currentCartId = cartService.selectCartId(username, guestCartId);
        cartService.addToCart(currentCartId, productId);
    }

//    @DeleteMapping("/delete/{productId}")
//    public void deleteProductById(@PathVariable(name = "productId") Long productId){
//        cartService.deleteFromCart(productId);
//    }

    @DeleteMapping("/{guestCartId}/delete/{productId}")
    public void deleteProductById(@RequestHeader(required = false) String username, @PathVariable String guestCartId,@PathVariable(name = "productId") Long productId){
        String currentCartId = cartService.selectCartId(username, guestCartId);
        cartService.deleteFromCart(currentCartId, productId);
    }

//    @DeleteMapping("/clear")
//    public void deleteAllProducts(){
//        cartService.clearCart();
//    }

    @DeleteMapping("/{guestCartId}/clear")
    public void deleteAllProducts(@RequestHeader(required = false) String username, @PathVariable String guestCartId) {
        String currentCartId = cartService.selectCartId(username, guestCartId);
        cartService.clearCart(currentCartId);
    }

}

