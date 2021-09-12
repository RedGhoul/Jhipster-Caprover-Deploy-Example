package com.ava.foodlogger.repository;

import com.ava.foodlogger.domain.FoodDay;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FoodDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodDayRepository extends JpaRepository<FoodDay, Long> {
    List<FoodDay> findByUser_User_Login_OrderByCreatedDateDesc(String currentUserLogin);
}
