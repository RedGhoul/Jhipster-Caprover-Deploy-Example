package com.ava.foodlogger.service.mapper;

import com.ava.foodlogger.domain.*;
import com.ava.foodlogger.service.dto.FoodDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Food} and its DTO {@link FoodDTO}.
 */
@Mapper(componentModel = "spring", uses = { FoodEntryMapper.class })
public interface FoodMapper extends EntityMapper<FoodDTO, Food> {
    @Mapping(target = "foodEntries", source = "foodEntries", qualifiedByName = "idSet")
    FoodDTO toDto(Food s);

    @Mapping(target = "removeFoodEntry", ignore = true)
    Food toEntity(FoodDTO foodDTO);
}
