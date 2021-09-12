package com.ava.foodlogger.service;

import com.ava.foodlogger.domain.FoodDay;
import com.ava.foodlogger.repository.FoodDayRepository;
import com.ava.foodlogger.security.SecurityUtils;
import com.ava.foodlogger.service.dto.FoodDayDTO;
import com.ava.foodlogger.service.mapper.FoodDayMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FoodDay}.
 */
@Service
@Transactional
public class FoodDayService {

    private final Logger log = LoggerFactory.getLogger(FoodDayService.class);

    private final FoodDayRepository foodDayRepository;

    private final FoodDayMapper foodDayMapper;

    public FoodDayService(FoodDayRepository foodDayRepository, FoodDayMapper foodDayMapper) {
        this.foodDayRepository = foodDayRepository;
        this.foodDayMapper = foodDayMapper;
    }

    /**
     * Save a foodDay.
     *
     * @param foodDayDTO the entity to save.
     * @return the persisted entity.
     */
    public FoodDayDTO save(FoodDayDTO foodDayDTO) {
        log.debug("Request to save FoodDay : {}", foodDayDTO);
        FoodDay foodDay = foodDayMapper.toEntity(foodDayDTO);
        foodDay = foodDayRepository.save(foodDay);
        return foodDayMapper.toDto(foodDay);
    }

    /**
     * Partially update a foodDay.
     *
     * @param foodDayDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FoodDayDTO> partialUpdate(FoodDayDTO foodDayDTO) {
        log.debug("Request to partially update FoodDay : {}", foodDayDTO);

        return foodDayRepository
            .findById(foodDayDTO.getId())
            .map(
                existingFoodDay -> {
                    foodDayMapper.partialUpdate(existingFoodDay, foodDayDTO);
                    return existingFoodDay;
                }
            )
            .map(foodDayRepository::save)
            .map(foodDayMapper::toDto);
    }

    /**
     * Get all the foodDays.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FoodDayDTO> findAll() {
        log.debug("Request to get all FoodDays");
        return foodDayRepository
            .findByUser_User_Login_OrderByCreatedDateDesc(SecurityUtils.getCurrentUserLogin().orElse(null))
            .stream()
            .map(foodDayMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one foodDay by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FoodDayDTO> findOne(Long id) {
        log.debug("Request to get FoodDay : {}", id);
        return foodDayRepository.findById(id).map(foodDayMapper::toDto);
    }

    /**
     * Delete the foodDay by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FoodDay : {}", id);
        foodDayRepository.deleteById(id);
    }
}
