package com.ava.foodlogger.service.mapper;

import com.ava.foodlogger.domain.*;
import com.ava.foodlogger.service.dto.CurrentWeightDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CurrentWeight} and its DTO {@link CurrentWeightDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class })
public interface CurrentWeightMapper extends EntityMapper<CurrentWeightDTO, CurrentWeight> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    CurrentWeightDTO toDto(CurrentWeight s);
}
