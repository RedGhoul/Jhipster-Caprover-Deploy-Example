package com.ava.foodlogger.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CurrentWeightMapperTest {

    private CurrentWeightMapper currentWeightMapper;

    @BeforeEach
    public void setUp() {
        currentWeightMapper = new CurrentWeightMapperImpl();
    }
}
