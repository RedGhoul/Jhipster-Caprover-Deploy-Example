package com.ava.foodlogger.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ava.foodlogger.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoodDayDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodDayDTO.class);
        FoodDayDTO foodDayDTO1 = new FoodDayDTO();
        foodDayDTO1.setId(1L);
        FoodDayDTO foodDayDTO2 = new FoodDayDTO();
        assertThat(foodDayDTO1).isNotEqualTo(foodDayDTO2);
        foodDayDTO2.setId(foodDayDTO1.getId());
        assertThat(foodDayDTO1).isEqualTo(foodDayDTO2);
        foodDayDTO2.setId(2L);
        assertThat(foodDayDTO1).isNotEqualTo(foodDayDTO2);
        foodDayDTO1.setId(null);
        assertThat(foodDayDTO1).isNotEqualTo(foodDayDTO2);
    }
}
