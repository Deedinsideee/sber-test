package ru.alexandr.task_one.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alexandr.task_one.model.Cart;
import ru.alexandr.task_one.service.CartService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /** Получить корзину */
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable UUID userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    /** Добавить товар в корзину */
    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addToCart(@PathVariable UUID userId, @RequestParam UUID productId) {
        return ResponseEntity.ok(cartService.addToCart(userId, productId));
    }

    /** Удалить товар из корзины */
    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Cart> removeFromCart(@PathVariable UUID userId, @RequestParam UUID productId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }
}
