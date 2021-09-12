package com.ava.foodlogger.service.mapper;

import com.ava.foodlogger.domain.*;
import com.ava.foodlogger.service.dto.GoalWeightDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GoalWeight} and its DTO {@link GoalWeightDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class })
public interface GoalWeightMapper extends EntityMapper<GoalWeightDTO, GoalWeight> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    GoalWeightDTO toDto(GoalWeight s);
}
