package com.ava.foodlogger.service.mapper;

import com.ava.foodlogger.domain.*;
import com.ava.foodlogger.service.dto.FoodEntryDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FoodEntry} and its DTO {@link FoodEntryDTO}.
 */
@Mapper(componentModel = "spring", uses = { FoodDayMapper.class, AppUserMapper.class })
public interface FoodEntryMapper extends EntityMapper<FoodEntryDTO, FoodEntry> {
    @Mapping(target = "foodDay", source = "foodDay", qualifiedByName = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    FoodEntryDTO toDto(FoodEntry s);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<FoodEntryDTO> toDtoIdSet(Set<FoodEntry> foodEntry);
}
