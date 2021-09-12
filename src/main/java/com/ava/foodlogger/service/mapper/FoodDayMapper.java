package com.ava.foodlogger.service.mapper;

import com.ava.foodlogger.domain.*;
import com.ava.foodlogger.service.dto.FoodDayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FoodDay} and its DTO {@link FoodDayDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class })
public interface FoodDayMapper extends EntityMapper<FoodDayDTO, FoodDay> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    FoodDayDTO toDto(FoodDay s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FoodDayDTO toDtoId(FoodDay foodDay);
}
