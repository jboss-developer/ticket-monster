package org.jboss.jdf.example.ticketmonster.service;

import javax.inject.Inject;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.jdf.example.ticketmonster.model.Cart;

import java.util.concurrent.TimeUnit;

/**
 * A service for storing and retrieving carts.
 *
 * @author Marius Bogoevici
 */
public class CartStore {

    public static final String CARTS_CACHE = "TICKETMONSTER_CARTS";

    private final Cache<String, Cart> cartsCache;

    @Inject
    public CartStore(EmbeddedCacheManager manager) {
        this.cartsCache = manager.getCache(CARTS_CACHE);
    }

    public Cart getCart(String cartId) {
        return this.cartsCache.get(cartId);
    }

    /**
     * Saves or updates a cart, setting an expiration time.
     *
     * @param cart - the cart to be saved
     */
    public void saveCart(Cart cart) {
        this.cartsCache.put(cart.getId(), cart, 10, TimeUnit.MINUTES);
    }

    /**
     * Removes a cart
     *
     * @param cart - the cart to be removed
     */
    public void delete(Cart cart) {
        this.cartsCache.remove(cart.getId());
    }
}
