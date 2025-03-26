package ru.alexandr.task_one.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexandr.task_one.model.Cart;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByUserId(UUID userId);
}