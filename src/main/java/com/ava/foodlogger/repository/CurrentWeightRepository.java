package com.ava.foodlogger.repository;

import com.ava.foodlogger.domain.CurrentWeight;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CurrentWeight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrentWeightRepository extends JpaRepository<CurrentWeight, Long> {}
