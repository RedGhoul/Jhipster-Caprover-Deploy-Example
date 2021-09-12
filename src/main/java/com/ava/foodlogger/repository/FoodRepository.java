package com.ava.foodlogger.repository;

import com.ava.foodlogger.domain.Food;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Food entity.
 */
@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    @Query(
        value = "select distinct food from Food food left join fetch food.foodEntries",
        countQuery = "select count(distinct food) from Food food"
    )
    Page<Food> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct food from Food food left join fetch food.foodEntries")
    List<Food> findAllWithEagerRelationships();

    @Query("select food from Food food left join fetch food.foodEntries where food.id =:id")
    Optional<Food> findOneWithEagerRelationships(@Param("id") Long id);
}
