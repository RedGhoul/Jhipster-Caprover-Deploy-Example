package com.ava.foodlogger.repository;

import com.ava.foodlogger.domain.FoodEntry;
import com.ava.foodlogger.service.dto.FoodEntryDTO;
import java.nio.channels.FileChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FoodEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    @Query("select foodentry from FoodEntry foodentry where foodentry.user.user.login = ?#{principal.username}")
    Page<FoodEntry> findAllByCurrentUser(Pageable pageable);
}
