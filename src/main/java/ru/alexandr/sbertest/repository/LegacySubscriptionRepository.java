package ru.alexandr.sbertest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.alexandr.sbertest.model.LegacySubscription;

import java.util.List;

public interface LegacySubscriptionRepository extends JpaRepository<LegacySubscription, Long> {

    @Query(value = "SELECT * FROM legacy_subscription WHERE processed = false LIMIT :limit", nativeQuery = true)
    List<LegacySubscription> findFirstUnprocessed(Integer limit);

}
