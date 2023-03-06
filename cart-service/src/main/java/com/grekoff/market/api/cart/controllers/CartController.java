package com.grekoff.market.api.cart.controllers;


import com.grekoff.market.api.cart.converters.CartConverter;
import com.grekoff.market.api.cart.services.CartService;
import com.grekoff.market.api.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
//@CrossOrigin("*")
public class CartController {
    private final CartService cartService;
    private final CartConverter cartConverter;

    // http://localhost:8190/market-cart/api/v1/cart
    @GetMapping
    public CartDto getCurrentCart() {
        return cartConverter.entityToDto(cartService.getCurrentCart());
    }

    @GetMapping("/add/{productId}")
    public void addProductToCart(@PathVariable Long productId) {
        cartService.addToCart(productId);
    }

    @DeleteMapping("/delete/{productId}")
    public void deleteProductById(@PathVariable(name = "productId") Long productId){
        cartService.deleteFromCart(productId);
    }

    @DeleteMapping("/clear")
    public void deleteAllProducts(){
        cartService.clearCart();
    }

}

