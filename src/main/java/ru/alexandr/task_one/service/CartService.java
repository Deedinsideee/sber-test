package ru.alexandr.task_one.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.task_one.model.Cart;
import ru.alexandr.task_one.model.Product;
import ru.alexandr.task_one.repository.CartRepository;
import ru.alexandr.task_one.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    /**
     * Получить корзину пользователя
     */
    public Cart getCart(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Корзина для пользователя с id " + userId + " не найдена"));
    }

    /**
     * Добавить товар в корзину
     */
    @Transactional
    public Cart addToCart(UUID userId, UUID productId) {
        Cart cart = getCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        List<Product> products = cart.getProducts();
        products.add(product);
        cart.setProducts(products);
        cart.setTotalPrice(cart.getTotalPrice().add(product.getPrice()));

        return cartRepository.save(cart);
    }

    /**
     * Удалить товар из корзины
     */
    @Transactional
    public Cart removeFromCart(UUID userId, UUID productId) {
        Cart cart = getCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        List<Product> products = cart.getProducts();
        if (products.remove(product)) {
            cart.setTotalPrice(cart.getTotalPrice().subtract(product.getPrice()));
        }

        cart.setProducts(products);
        return cartRepository.save(cart);
    }
}
