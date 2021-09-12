package com.ava.foodlogger.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodEntryMapperTest {

    private FoodEntryMapper foodEntryMapper;

    @BeforeEach
    public void setUp() {
        foodEntryMapper = new FoodEntryMapperImpl();
    }
}
