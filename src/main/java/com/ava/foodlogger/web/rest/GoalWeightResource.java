package com.ava.foodlogger.web.rest;

import com.ava.foodlogger.repository.GoalWeightRepository;
import com.ava.foodlogger.service.GoalWeightService;
import com.ava.foodlogger.service.dto.GoalWeightDTO;
import com.ava.foodlogger.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ava.foodlogger.domain.GoalWeight}.
 */
@RestController
@RequestMapping("/api")
public class GoalWeightResource {

    private final Logger log = LoggerFactory.getLogger(GoalWeightResource.class);

    private static final String ENTITY_NAME = "goalWeight";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoalWeightService goalWeightService;

    private final GoalWeightRepository goalWeightRepository;

    public GoalWeightResource(GoalWeightService goalWeightService, GoalWeightRepository goalWeightRepository) {
        this.goalWeightService = goalWeightService;
        this.goalWeightRepository = goalWeightRepository;
    }

    /**
     * {@code POST  /goal-weights} : Create a new goalWeight.
     *
     * @param goalWeightDTO the goalWeightDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new goalWeightDTO, or with status {@code 400 (Bad Request)} if the goalWeight has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/goal-weights")
    public ResponseEntity<GoalWeightDTO> createGoalWeight(@Valid @RequestBody GoalWeightDTO goalWeightDTO) throws URISyntaxException {
        log.debug("REST request to save GoalWeight : {}", goalWeightDTO);
        if (goalWeightDTO.getId() != null) {
            throw new BadRequestAlertException("A new goalWeight cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoalWeightDTO result = goalWeightService.save(goalWeightDTO);
        return ResponseEntity
            .created(new URI("/api/goal-weights/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /goal-weights/:id} : Updates an existing goalWeight.
     *
     * @param id the id of the goalWeightDTO to save.
     * @param goalWeightDTO the goalWeightDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goalWeightDTO,
     * or with status {@code 400 (Bad Request)} if the goalWeightDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the goalWeightDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/goal-weights/{id}")
    public ResponseEntity<GoalWeightDTO> updateGoalWeight(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GoalWeightDTO goalWeightDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GoalWeight : {}, {}", id, goalWeightDTO);
        if (goalWeightDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, goalWeightDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!goalWeightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GoalWeightDTO result = goalWeightService.save(goalWeightDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, goalWeightDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /goal-weights/:id} : Partial updates given fields of an existing goalWeight, field will ignore if it is null
     *
     * @param id the id of the goalWeightDTO to save.
     * @param goalWeightDTO the goalWeightDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goalWeightDTO,
     * or with status {@code 400 (Bad Request)} if the goalWeightDTO is not valid,
     * or with status {@code 404 (Not Found)} if the goalWeightDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the goalWeightDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/goal-weights/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GoalWeightDTO> partialUpdateGoalWeight(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GoalWeightDTO goalWeightDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GoalWeight partially : {}, {}", id, goalWeightDTO);
        if (goalWeightDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, goalWeightDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!goalWeightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GoalWeightDTO> result = goalWeightService.partialUpdate(goalWeightDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, goalWeightDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /goal-weights} : get all the goalWeights.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of goalWeights in body.
     */
    @GetMapping("/goal-weights")
    public List<GoalWeightDTO> getAllGoalWeights() {
        log.debug("REST request to get all GoalWeights");
        return goalWeightService.findAll();
    }

    /**
     * {@code GET  /goal-weights/:id} : get the "id" goalWeight.
     *
     * @param id the id of the goalWeightDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the goalWeightDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/goal-weights/{id}")
    public ResponseEntity<GoalWeightDTO> getGoalWeight(@PathVariable Long id) {
        log.debug("REST request to get GoalWeight : {}", id);
        Optional<GoalWeightDTO> goalWeightDTO = goalWeightService.findOne(id);
        return ResponseUtil.wrapOrNotFound(goalWeightDTO);
    }

    /**
     * {@code DELETE  /goal-weights/:id} : delete the "id" goalWeight.
     *
     * @param id the id of the goalWeightDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/goal-weights/{id}")
    public ResponseEntity<Void> deleteGoalWeight(@PathVariable Long id) {
        log.debug("REST request to delete GoalWeight : {}", id);
        goalWeightService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
