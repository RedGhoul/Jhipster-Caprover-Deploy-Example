package com.ava.foodlogger.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ava.foodlogger.IntegrationTest;
import com.ava.foodlogger.domain.FoodEntry;
import com.ava.foodlogger.domain.enumeration.MealType;
import com.ava.foodlogger.repository.FoodEntryRepository;
import com.ava.foodlogger.service.dto.FoodEntryDTO;
import com.ava.foodlogger.service.mapper.FoodEntryMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FoodEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FoodEntryResourceIT {

    private static final MealType DEFAULT_MEALTYPE = MealType.Lunch;
    private static final MealType UPDATED_MEALTYPE = MealType.Dinner;

    private static final String ENTITY_API_URL = "/api/food-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FoodEntryRepository foodEntryRepository;

    @Autowired
    private FoodEntryMapper foodEntryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFoodEntryMockMvc;

    private FoodEntry foodEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodEntry createEntity(EntityManager em) {
        FoodEntry foodEntry = new FoodEntry().mealtype(DEFAULT_MEALTYPE);
        return foodEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodEntry createUpdatedEntity(EntityManager em) {
        FoodEntry foodEntry = new FoodEntry().mealtype(UPDATED_MEALTYPE);
        return foodEntry;
    }

    @BeforeEach
    public void initTest() {
        foodEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createFoodEntry() throws Exception {
        int databaseSizeBeforeCreate = foodEntryRepository.findAll().size();
        // Create the FoodEntry
        FoodEntryDTO foodEntryDTO = foodEntryMapper.toDto(foodEntry);
        restFoodEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodEntryDTO)))
            .andExpect(status().isCreated());

        // Validate the FoodEntry in the database
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeCreate + 1);
        FoodEntry testFoodEntry = foodEntryList.get(foodEntryList.size() - 1);
        assertThat(testFoodEntry.getMealtype()).isEqualTo(DEFAULT_MEALTYPE);
    }

    @Test
    @Transactional
    void createFoodEntryWithExistingId() throws Exception {
        // Create the FoodEntry with an existing ID
        foodEntry.setId(1L);
        FoodEntryDTO foodEntryDTO = foodEntryMapper.toDto(foodEntry);

        int databaseSizeBeforeCreate = foodEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FoodEntry in the database
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFoodEntries() throws Exception {
        // Initialize the database
        foodEntryRepository.saveAndFlush(foodEntry);

        // Get all the foodEntryList
        restFoodEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].mealtype").value(hasItem(DEFAULT_MEALTYPE.toString())));
    }

    @Test
    @Transactional
    void getFoodEntry() throws Exception {
        // Initialize the database
        foodEntryRepository.saveAndFlush(foodEntry);

        // Get the foodEntry
        restFoodEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, foodEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(foodEntry.getId().intValue()))
            .andExpect(jsonPath("$.mealtype").value(DEFAULT_MEALTYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFoodEntry() throws Exception {
        // Get the foodEntry
        restFoodEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFoodEntry() throws Exception {
        // Initialize the database
        foodEntryRepository.saveAndFlush(foodEntry);

        int databaseSizeBeforeUpdate = foodEntryRepository.findAll().size();

        // Update the foodEntry
        FoodEntry updatedFoodEntry = foodEntryRepository.findById(foodEntry.getId()).get();
        // Disconnect from session so that the updates on updatedFoodEntry are not directly saved in db
        em.detach(updatedFoodEntry);
        updatedFoodEntry.mealtype(UPDATED_MEALTYPE);
        FoodEntryDTO foodEntryDTO = foodEntryMapper.toDto(updatedFoodEntry);

        restFoodEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the FoodEntry in the database
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeUpdate);
        FoodEntry testFoodEntry = foodEntryList.get(foodEntryList.size() - 1);
        assertThat(testFoodEntry.getMealtype()).isEqualTo(UPDATED_MEALTYPE);
    }

    @Test
    @Transactional
    void putNonExistingFoodEntry() throws Exception {
        int databaseSizeBeforeUpdate = foodEntryRepository.findAll().size();
        foodEntry.setId(count.incrementAndGet());

        // Create the FoodEntry
        FoodEntryDTO foodEntryDTO = foodEntryMapper.toDto(foodEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodEntry in the database
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFoodEntry() throws Exception {
        int databaseSizeBeforeUpdate = foodEntryRepository.findAll().size();
        foodEntry.setId(count.incrementAndGet());

        // Create the FoodEntry
        FoodEntryDTO foodEntryDTO = foodEntryMapper.toDto(foodEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodEntry in the database
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFoodEntry() throws Exception {
        int databaseSizeBeforeUpdate = foodEntryRepository.findAll().size();
        foodEntry.setId(count.incrementAndGet());

        // Create the FoodEntry
        FoodEntryDTO foodEntryDTO = foodEntryMapper.toDto(foodEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodEntry in the database
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFoodEntryWithPatch() throws Exception {
        // Initialize the database
        foodEntryRepository.saveAndFlush(foodEntry);

        int databaseSizeBeforeUpdate = foodEntryRepository.findAll().size();

        // Update the foodEntry using partial update
        FoodEntry partialUpdatedFoodEntry = new FoodEntry();
        partialUpdatedFoodEntry.setId(foodEntry.getId());

        partialUpdatedFoodEntry.mealtype(UPDATED_MEALTYPE);

        restFoodEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodEntry))
            )
            .andExpect(status().isOk());

        // Validate the FoodEntry in the database
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeUpdate);
        FoodEntry testFoodEntry = foodEntryList.get(foodEntryList.size() - 1);
        assertThat(testFoodEntry.getMealtype()).isEqualTo(UPDATED_MEALTYPE);
    }

    @Test
    @Transactional
    void fullUpdateFoodEntryWithPatch() throws Exception {
        // Initialize the database
        foodEntryRepository.saveAndFlush(foodEntry);

        int databaseSizeBeforeUpdate = foodEntryRepository.findAll().size();

        // Update the foodEntry using partial update
        FoodEntry partialUpdatedFoodEntry = new FoodEntry();
        partialUpdatedFoodEntry.setId(foodEntry.getId());

        partialUpdatedFoodEntry.mealtype(UPDATED_MEALTYPE);

        restFoodEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodEntry))
            )
            .andExpect(status().isOk());

        // Validate the FoodEntry in the database
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeUpdate);
        FoodEntry testFoodEntry = foodEntryList.get(foodEntryList.size() - 1);
        assertThat(testFoodEntry.getMealtype()).isEqualTo(UPDATED_MEALTYPE);
    }

    @Test
    @Transactional
    void patchNonExistingFoodEntry() throws Exception {
        int databaseSizeBeforeUpdate = foodEntryRepository.findAll().size();
        foodEntry.setId(count.incrementAndGet());

        // Create the FoodEntry
        FoodEntryDTO foodEntryDTO = foodEntryMapper.toDto(foodEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, foodEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodEntry in the database
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFoodEntry() throws Exception {
        int databaseSizeBeforeUpdate = foodEntryRepository.findAll().size();
        foodEntry.setId(count.incrementAndGet());

        // Create the FoodEntry
        FoodEntryDTO foodEntryDTO = foodEntryMapper.toDto(foodEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodEntry in the database
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFoodEntry() throws Exception {
        int databaseSizeBeforeUpdate = foodEntryRepository.findAll().size();
        foodEntry.setId(count.incrementAndGet());

        // Create the FoodEntry
        FoodEntryDTO foodEntryDTO = foodEntryMapper.toDto(foodEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodEntryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(foodEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodEntry in the database
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFoodEntry() throws Exception {
        // Initialize the database
        foodEntryRepository.saveAndFlush(foodEntry);

        int databaseSizeBeforeDelete = foodEntryRepository.findAll().size();

        // Delete the foodEntry
        restFoodEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, foodEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FoodEntry> foodEntryList = foodEntryRepository.findAll();
        assertThat(foodEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
