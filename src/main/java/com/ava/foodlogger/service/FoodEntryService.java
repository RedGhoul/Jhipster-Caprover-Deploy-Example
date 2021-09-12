package com.ava.foodlogger.service;

import com.ava.foodlogger.domain.FoodEntry;
import com.ava.foodlogger.repository.FoodEntryRepository;
import com.ava.foodlogger.service.dto.FoodEntryDTO;
import com.ava.foodlogger.service.mapper.FoodEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FoodEntry}.
 */
@Service
@Transactional
public class FoodEntryService {

    private final Logger log = LoggerFactory.getLogger(FoodEntryService.class);

    private final FoodEntryRepository foodEntryRepository;

    private final FoodEntryMapper foodEntryMapper;

    public FoodEntryService(FoodEntryRepository foodEntryRepository, FoodEntryMapper foodEntryMapper) {
        this.foodEntryRepository = foodEntryRepository;
        this.foodEntryMapper = foodEntryMapper;
    }

    /**
     * Save a foodEntry.
     *
     * @param foodEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public FoodEntryDTO save(FoodEntryDTO foodEntryDTO) {
        log.debug("Request to save FoodEntry : {}", foodEntryDTO);
        FoodEntry foodEntry = foodEntryMapper.toEntity(foodEntryDTO);
        foodEntry = foodEntryRepository.save(foodEntry);
        return foodEntryMapper.toDto(foodEntry);
    }

    /**
     * Partially update a foodEntry.
     *
     * @param foodEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FoodEntryDTO> partialUpdate(FoodEntryDTO foodEntryDTO) {
        log.debug("Request to partially update FoodEntry : {}", foodEntryDTO);

        return foodEntryRepository
            .findById(foodEntryDTO.getId())
            .map(
                existingFoodEntry -> {
                    foodEntryMapper.partialUpdate(existingFoodEntry, foodEntryDTO);
                    return existingFoodEntry;
                }
            )
            .map(foodEntryRepository::save)
            .map(foodEntryMapper::toDto);
    }

    /**
     * Get all the foodEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FoodEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FoodEntries");
        return foodEntryRepository.findAll(pageable).map(foodEntryMapper::toDto);
    }

    /**
     * Get all the foodEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FoodEntryDTO> findAllByCurrentUser(Pageable pageable) {
        log.debug("Request to get all FoodEntries");
        return foodEntryRepository.findAllByCurrentUser(pageable).map(foodEntryMapper::toDto);
    }

    /**
     * Get one foodEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FoodEntryDTO> findOne(Long id) {
        log.debug("Request to get FoodEntry : {}", id);
        return foodEntryRepository.findById(id).map(foodEntryMapper::toDto);
    }

    /**
     * Delete the foodEntry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FoodEntry : {}", id);
        foodEntryRepository.deleteById(id);
    }
}
