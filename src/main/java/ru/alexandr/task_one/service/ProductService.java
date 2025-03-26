package ru.alexandr.task_one.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.alexandr.task_one.model.Product;
import ru.alexandr.task_one.repository.ProductRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    /** Получить все товары с фильтрацией по категории и пагинацией */
    public Page<Product> getProducts(UUID categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return (categoryId == null)
                ? productRepository.findAll(pageable)
                : productRepository.findByCategoryId(categoryId, pageable);
    }

    /** Получить товар по ID */
    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }
}
