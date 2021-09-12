package com.ava.foodlogger.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GoalWeightMapperTest {

    private GoalWeightMapper goalWeightMapper;

    @BeforeEach
    public void setUp() {
        goalWeightMapper = new GoalWeightMapperImpl();
    }
}
