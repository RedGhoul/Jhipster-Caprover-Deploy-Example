package com.ava.foodlogger.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ava.foodlogger.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GoalWeightDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoalWeightDTO.class);
        GoalWeightDTO goalWeightDTO1 = new GoalWeightDTO();
        goalWeightDTO1.setId(1L);
        GoalWeightDTO goalWeightDTO2 = new GoalWeightDTO();
        assertThat(goalWeightDTO1).isNotEqualTo(goalWeightDTO2);
        goalWeightDTO2.setId(goalWeightDTO1.getId());
        assertThat(goalWeightDTO1).isEqualTo(goalWeightDTO2);
        goalWeightDTO2.setId(2L);
        assertThat(goalWeightDTO1).isNotEqualTo(goalWeightDTO2);
        goalWeightDTO1.setId(null);
        assertThat(goalWeightDTO1).isNotEqualTo(goalWeightDTO2);
    }
}
