package ru.alexandr.sbertest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.alexandr.sbertest.model.Subscription;
import ru.alexandr.sbertest.model.SubscriptionBook;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionBookRepository extends JpaRepository<SubscriptionBook, Long> {

    @Query("""
                SELECT sb.subscription
                FROM SubscriptionBook sb
                WHERE sb.issueDate <= :cutoffDate
                AND sb.returnDate IS NULL
            """)
    List<Subscription> findOverdueSubscriptions(LocalDate cutoffDate);

}
