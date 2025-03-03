package ru.alexandr.sbertest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.alexandr.sbertest.model.Subscription;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    @Query("SELECT s FROM Subscription s WHERE LOWER(s.userFullName) = LOWER(:userFullName)")
    Optional<Subscription> findByUserFullNameLike(String userFullName);


    @Query("SELECT s FROM Subscription s WHERE s.username IN :usernames")
    List<Subscription> findByUsernameIn(Set<String> usernames);
}
