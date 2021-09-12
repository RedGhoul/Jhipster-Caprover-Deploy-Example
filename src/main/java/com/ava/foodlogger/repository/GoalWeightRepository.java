package com.ava.foodlogger.repository;

import com.ava.foodlogger.domain.GoalWeight;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GoalWeight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoalWeightRepository extends JpaRepository<GoalWeight, Long> {}
