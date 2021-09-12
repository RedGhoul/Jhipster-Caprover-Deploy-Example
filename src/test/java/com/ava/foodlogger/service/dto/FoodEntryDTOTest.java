package com.ava.foodlogger.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ava.foodlogger.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoodEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodEntryDTO.class);
        FoodEntryDTO foodEntryDTO1 = new FoodEntryDTO();
        foodEntryDTO1.setId(1L);
        FoodEntryDTO foodEntryDTO2 = new FoodEntryDTO();
        assertThat(foodEntryDTO1).isNotEqualTo(foodEntryDTO2);
        foodEntryDTO2.setId(foodEntryDTO1.getId());
        assertThat(foodEntryDTO1).isEqualTo(foodEntryDTO2);
        foodEntryDTO2.setId(2L);
        assertThat(foodEntryDTO1).isNotEqualTo(foodEntryDTO2);
        foodEntryDTO1.setId(null);
        assertThat(foodEntryDTO1).isNotEqualTo(foodEntryDTO2);
    }
}
