package com.grekoff.market.api.cart.services;

import com.grekoff.market.api.ProductDto;
import com.grekoff.market.api.cart.integrations.ProductsServiceIntegration;
import com.grekoff.market.api.cart.utils.Cart;
import com.grekoff.market.api.cart.utils.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductsServiceIntegration productsServiceIntegration;
    private final RedisTemplate<String, Object> redisTemplate;

//    private Cart cart;
//
//    @PostConstruct
//    public void init() {
//        cart = new Cart();
//        cart.setItems(new ArrayList<>());
//    }

    public Cart getActualCart(String cartId, String guestCartId ) {
        if(!Objects.equals(cartId, guestCartId)){
            return addGuestCartToCurrentCart(cartId, guestCartId);
        } else {
            return getCurrentCart(cartId);
        }
    }

    public Cart getCurrentCart(String cartId) {
        if(!redisTemplate.hasKey(cartId)){
            Cart cart = new Cart();
            redisTemplate.opsForValue().set(cartId, cart);
        }
        return (Cart) redisTemplate.opsForValue().get(cartId);
    }
    public Cart addGuestCartToCurrentCart(String cartId, String guestCartId) {
        if(!Objects.equals(cartId, guestCartId)){
            Cart guestCart = (Cart) redisTemplate.opsForValue().get(guestCartId);
            Cart currentCart = (Cart) redisTemplate.opsForValue().get(cartId);
            List<CartItem> items = guestCart.getItems();
            for (CartItem i : items){
                currentCart.getItems().add(i);
            }
            redisTemplate.opsForValue().set(cartId, currentCart);
        }
        return (Cart) redisTemplate.opsForValue().get(cartId);
    }

    public void addToCart(String cartId, Long productId) {
        execute(cartId, cart -> {
            ProductDto p = productsServiceIntegration.findById(productId);
            cart.add(p);
        });
    }

    public void deleteFromCart(String cartId, Long productId) {

        execute(cartId, cart -> cart.delete(productId));
    }

    public void clearCart(String cartId) {
        execute(cartId, Cart::clear);
    }

    private void execute(String cartId, Consumer<Cart> action) {
        Cart cart = getCurrentCart(cartId);
        action.accept(cart);
        redisTemplate.opsForValue().set(cartId, cart);
    }

    public String selectCartId(String username, String guestCartId) {
        if (username != null) {
            return username;
        }
        return guestCartId;
    }
}
