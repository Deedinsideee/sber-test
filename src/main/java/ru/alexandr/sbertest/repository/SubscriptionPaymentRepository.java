package ru.alexandr.sbertest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexandr.sbertest.model.SubscriptionPayment;

import java.util.UUID;

public interface SubscriptionPaymentRepository extends JpaRepository<SubscriptionPayment, UUID> {

}
