package com.ava.foodlogger.service;

import com.ava.foodlogger.domain.CurrentWeight;
import com.ava.foodlogger.repository.CurrentWeightRepository;
import com.ava.foodlogger.service.dto.CurrentWeightDTO;
import com.ava.foodlogger.service.mapper.CurrentWeightMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CurrentWeight}.
 */
@Service
@Transactional
public class CurrentWeightService {

    private final Logger log = LoggerFactory.getLogger(CurrentWeightService.class);

    private final CurrentWeightRepository currentWeightRepository;

    private final CurrentWeightMapper currentWeightMapper;

    public CurrentWeightService(CurrentWeightRepository currentWeightRepository, CurrentWeightMapper currentWeightMapper) {
        this.currentWeightRepository = currentWeightRepository;
        this.currentWeightMapper = currentWeightMapper;
    }

    /**
     * Save a currentWeight.
     *
     * @param currentWeightDTO the entity to save.
     * @return the persisted entity.
     */
    public CurrentWeightDTO save(CurrentWeightDTO currentWeightDTO) {
        log.debug("Request to save CurrentWeight : {}", currentWeightDTO);
        CurrentWeight currentWeight = currentWeightMapper.toEntity(currentWeightDTO);
        currentWeight = currentWeightRepository.save(currentWeight);
        return currentWeightMapper.toDto(currentWeight);
    }

    /**
     * Partially update a currentWeight.
     *
     * @param currentWeightDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CurrentWeightDTO> partialUpdate(CurrentWeightDTO currentWeightDTO) {
        log.debug("Request to partially update CurrentWeight : {}", currentWeightDTO);

        return currentWeightRepository
            .findById(currentWeightDTO.getId())
            .map(
                existingCurrentWeight -> {
                    currentWeightMapper.partialUpdate(existingCurrentWeight, currentWeightDTO);
                    return existingCurrentWeight;
                }
            )
            .map(currentWeightRepository::save)
            .map(currentWeightMapper::toDto);
    }

    /**
     * Get all the currentWeights.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CurrentWeightDTO> findAll() {
        log.debug("Request to get all CurrentWeights");
        return currentWeightRepository.findAll().stream().map(currentWeightMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one currentWeight by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CurrentWeightDTO> findOne(Long id) {
        log.debug("Request to get CurrentWeight : {}", id);
        return currentWeightRepository.findById(id).map(currentWeightMapper::toDto);
    }

    /**
     * Delete the currentWeight by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CurrentWeight : {}", id);
        currentWeightRepository.deleteById(id);
    }
}
