package com.ava.foodlogger.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodDayMapperTest {

    private FoodDayMapper foodDayMapper;

    @BeforeEach
    public void setUp() {
        foodDayMapper = new FoodDayMapperImpl();
    }
}
