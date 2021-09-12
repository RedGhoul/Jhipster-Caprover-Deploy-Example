package com.ava.foodlogger.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ava.foodlogger.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CurrentWeightDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrentWeightDTO.class);
        CurrentWeightDTO currentWeightDTO1 = new CurrentWeightDTO();
        currentWeightDTO1.setId(1L);
        CurrentWeightDTO currentWeightDTO2 = new CurrentWeightDTO();
        assertThat(currentWeightDTO1).isNotEqualTo(currentWeightDTO2);
        currentWeightDTO2.setId(currentWeightDTO1.getId());
        assertThat(currentWeightDTO1).isEqualTo(currentWeightDTO2);
        currentWeightDTO2.setId(2L);
        assertThat(currentWeightDTO1).isNotEqualTo(currentWeightDTO2);
        currentWeightDTO1.setId(null);
        assertThat(currentWeightDTO1).isNotEqualTo(currentWeightDTO2);
    }
}
