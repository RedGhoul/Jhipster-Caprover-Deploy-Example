package com.ava.foodlogger.web.rest;

import com.ava.foodlogger.repository.FoodDayRepository;
import com.ava.foodlogger.service.FoodDayService;
import com.ava.foodlogger.service.dto.FoodDayDTO;
import com.ava.foodlogger.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ava.foodlogger.domain.FoodDay}.
 */
@RestController
@RequestMapping("/api")
public class FoodDayResource {

    private final Logger log = LoggerFactory.getLogger(FoodDayResource.class);

    private static final String ENTITY_NAME = "foodDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FoodDayService foodDayService;

    private final FoodDayRepository foodDayRepository;

    public FoodDayResource(FoodDayService foodDayService, FoodDayRepository foodDayRepository) {
        this.foodDayService = foodDayService;
        this.foodDayRepository = foodDayRepository;
    }

    /**
     * {@code POST  /food-days} : Create a new foodDay.
     *
     * @param foodDayDTO the foodDayDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new foodDayDTO, or with status {@code 400 (Bad Request)} if the foodDay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/food-days")
    public ResponseEntity<FoodDayDTO> createFoodDay(@RequestBody FoodDayDTO foodDayDTO) throws URISyntaxException {
        log.debug("REST request to save FoodDay : {}", foodDayDTO);
        if (foodDayDTO.getId() != null) {
            throw new BadRequestAlertException("A new foodDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FoodDayDTO result = foodDayService.save(foodDayDTO);
        return ResponseEntity
            .created(new URI("/api/food-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /food-days/:id} : Updates an existing foodDay.
     *
     * @param id the id of the foodDayDTO to save.
     * @param foodDayDTO the foodDayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodDayDTO,
     * or with status {@code 400 (Bad Request)} if the foodDayDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the foodDayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/food-days/{id}")
    public ResponseEntity<FoodDayDTO> updateFoodDay(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FoodDayDTO foodDayDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FoodDay : {}, {}", id, foodDayDTO);
        if (foodDayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodDayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FoodDayDTO result = foodDayService.save(foodDayDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, foodDayDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /food-days/:id} : Partial updates given fields of an existing foodDay, field will ignore if it is null
     *
     * @param id the id of the foodDayDTO to save.
     * @param foodDayDTO the foodDayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodDayDTO,
     * or with status {@code 400 (Bad Request)} if the foodDayDTO is not valid,
     * or with status {@code 404 (Not Found)} if the foodDayDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the foodDayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/food-days/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FoodDayDTO> partialUpdateFoodDay(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FoodDayDTO foodDayDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FoodDay partially : {}, {}", id, foodDayDTO);
        if (foodDayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodDayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FoodDayDTO> result = foodDayService.partialUpdate(foodDayDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, foodDayDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /food-days} : get all the foodDays.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of foodDays in body.
     */
    @GetMapping("/food-days")
    public List<FoodDayDTO> getAllFoodDays() {
        log.debug("REST request to get all FoodDays");
        return foodDayService.findAll();
    }

    /**
     * {@code GET  /food-days/:id} : get the "id" foodDay.
     *
     * @param id the id of the foodDayDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the foodDayDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/food-days/{id}")
    public ResponseEntity<FoodDayDTO> getFoodDay(@PathVariable Long id) {
        log.debug("REST request to get FoodDay : {}", id);
        Optional<FoodDayDTO> foodDayDTO = foodDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(foodDayDTO);
    }

    /**
     * {@code DELETE  /food-days/:id} : delete the "id" foodDay.
     *
     * @param id the id of the foodDayDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/food-days/{id}")
    public ResponseEntity<Void> deleteFoodDay(@PathVariable Long id) {
        log.debug("REST request to delete FoodDay : {}", id);
        foodDayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
