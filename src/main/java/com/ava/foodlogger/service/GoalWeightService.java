package com.ava.foodlogger.service;

import com.ava.foodlogger.domain.GoalWeight;
import com.ava.foodlogger.repository.GoalWeightRepository;
import com.ava.foodlogger.service.dto.GoalWeightDTO;
import com.ava.foodlogger.service.mapper.GoalWeightMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GoalWeight}.
 */
@Service
@Transactional
public class GoalWeightService {

    private final Logger log = LoggerFactory.getLogger(GoalWeightService.class);

    private final GoalWeightRepository goalWeightRepository;

    private final GoalWeightMapper goalWeightMapper;

    public GoalWeightService(GoalWeightRepository goalWeightRepository, GoalWeightMapper goalWeightMapper) {
        this.goalWeightRepository = goalWeightRepository;
        this.goalWeightMapper = goalWeightMapper;
    }

    /**
     * Save a goalWeight.
     *
     * @param goalWeightDTO the entity to save.
     * @return the persisted entity.
     */
    public GoalWeightDTO save(GoalWeightDTO goalWeightDTO) {
        log.debug("Request to save GoalWeight : {}", goalWeightDTO);
        GoalWeight goalWeight = goalWeightMapper.toEntity(goalWeightDTO);
        goalWeight = goalWeightRepository.save(goalWeight);
        return goalWeightMapper.toDto(goalWeight);
    }

    /**
     * Partially update a goalWeight.
     *
     * @param goalWeightDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GoalWeightDTO> partialUpdate(GoalWeightDTO goalWeightDTO) {
        log.debug("Request to partially update GoalWeight : {}", goalWeightDTO);

        return goalWeightRepository
            .findById(goalWeightDTO.getId())
            .map(
                existingGoalWeight -> {
                    goalWeightMapper.partialUpdate(existingGoalWeight, goalWeightDTO);
                    return existingGoalWeight;
                }
            )
            .map(goalWeightRepository::save)
            .map(goalWeightMapper::toDto);
    }

    /**
     * Get all the goalWeights.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GoalWeightDTO> findAll() {
        log.debug("Request to get all GoalWeights");
        return goalWeightRepository.findAll().stream().map(goalWeightMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one goalWeight by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GoalWeightDTO> findOne(Long id) {
        log.debug("Request to get GoalWeight : {}", id);
        return goalWeightRepository.findById(id).map(goalWeightMapper::toDto);
    }

    /**
     * Delete the goalWeight by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GoalWeight : {}", id);
        goalWeightRepository.deleteById(id);
    }
}
