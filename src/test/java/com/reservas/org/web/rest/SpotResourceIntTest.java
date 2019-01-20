package com.reservas.org.web.rest;

import com.reservas.org.ReservasApp;

import com.reservas.org.domain.Spot;
import com.reservas.org.repository.SpotRepository;
import com.reservas.org.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.reservas.org.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SpotResource REST controller.
 *
 * @see SpotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReservasApp.class)
public class SpotResourceIntTest {

    private static final Integer DEFAULT_NUM_PERSONAS = 1;
    private static final Integer UPDATED_NUM_PERSONAS = 2;

    private static final Integer DEFAULT_NUM_MESA = 1;
    private static final Integer UPDATED_NUM_MESA = 2;

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSpotMockMvc;

    private Spot spot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SpotResource spotResource = new SpotResource(spotRepository);
        this.restSpotMockMvc = MockMvcBuilders.standaloneSetup(spotResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spot createEntity(EntityManager em) {
        Spot spot = new Spot()
            .numPersonas(DEFAULT_NUM_PERSONAS)
            .numMesa(DEFAULT_NUM_MESA);
        return spot;
    }

    @Before
    public void initTest() {
        spot = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpot() throws Exception {
        int databaseSizeBeforeCreate = spotRepository.findAll().size();

        // Create the Spot
        restSpotMockMvc.perform(post("/api/spots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spot)))
            .andExpect(status().isCreated());

        // Validate the Spot in the database
        List<Spot> spotList = spotRepository.findAll();
        assertThat(spotList).hasSize(databaseSizeBeforeCreate + 1);
        Spot testSpot = spotList.get(spotList.size() - 1);
        assertThat(testSpot.getNumPersonas()).isEqualTo(DEFAULT_NUM_PERSONAS);
        assertThat(testSpot.getNumMesa()).isEqualTo(DEFAULT_NUM_MESA);
    }

    @Test
    @Transactional
    public void createSpotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = spotRepository.findAll().size();

        // Create the Spot with an existing ID
        spot.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpotMockMvc.perform(post("/api/spots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spot)))
            .andExpect(status().isBadRequest());

        // Validate the Spot in the database
        List<Spot> spotList = spotRepository.findAll();
        assertThat(spotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSpots() throws Exception {
        // Initialize the database
        spotRepository.saveAndFlush(spot);

        // Get all the spotList
        restSpotMockMvc.perform(get("/api/spots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spot.getId().intValue())))
            .andExpect(jsonPath("$.[*].numPersonas").value(hasItem(DEFAULT_NUM_PERSONAS)))
            .andExpect(jsonPath("$.[*].numMesa").value(hasItem(DEFAULT_NUM_MESA)));
    }
    
    @Test
    @Transactional
    public void getSpot() throws Exception {
        // Initialize the database
        spotRepository.saveAndFlush(spot);

        // Get the spot
        restSpotMockMvc.perform(get("/api/spots/{id}", spot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spot.getId().intValue()))
            .andExpect(jsonPath("$.numPersonas").value(DEFAULT_NUM_PERSONAS))
            .andExpect(jsonPath("$.numMesa").value(DEFAULT_NUM_MESA));
    }

    @Test
    @Transactional
    public void getNonExistingSpot() throws Exception {
        // Get the spot
        restSpotMockMvc.perform(get("/api/spots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpot() throws Exception {
        // Initialize the database
        spotRepository.saveAndFlush(spot);

        int databaseSizeBeforeUpdate = spotRepository.findAll().size();

        // Update the spot
        Spot updatedSpot = spotRepository.findById(spot.getId()).get();
        // Disconnect from session so that the updates on updatedSpot are not directly saved in db
        em.detach(updatedSpot);
        updatedSpot
            .numPersonas(UPDATED_NUM_PERSONAS)
            .numMesa(UPDATED_NUM_MESA);

        restSpotMockMvc.perform(put("/api/spots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpot)))
            .andExpect(status().isOk());

        // Validate the Spot in the database
        List<Spot> spotList = spotRepository.findAll();
        assertThat(spotList).hasSize(databaseSizeBeforeUpdate);
        Spot testSpot = spotList.get(spotList.size() - 1);
        assertThat(testSpot.getNumPersonas()).isEqualTo(UPDATED_NUM_PERSONAS);
        assertThat(testSpot.getNumMesa()).isEqualTo(UPDATED_NUM_MESA);
    }

    @Test
    @Transactional
    public void updateNonExistingSpot() throws Exception {
        int databaseSizeBeforeUpdate = spotRepository.findAll().size();

        // Create the Spot

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpotMockMvc.perform(put("/api/spots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spot)))
            .andExpect(status().isBadRequest());

        // Validate the Spot in the database
        List<Spot> spotList = spotRepository.findAll();
        assertThat(spotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSpot() throws Exception {
        // Initialize the database
        spotRepository.saveAndFlush(spot);

        int databaseSizeBeforeDelete = spotRepository.findAll().size();

        // Get the spot
        restSpotMockMvc.perform(delete("/api/spots/{id}", spot.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Spot> spotList = spotRepository.findAll();
        assertThat(spotList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Spot.class);
        Spot spot1 = new Spot();
        spot1.setId(1L);
        Spot spot2 = new Spot();
        spot2.setId(spot1.getId());
        assertThat(spot1).isEqualTo(spot2);
        spot2.setId(2L);
        assertThat(spot1).isNotEqualTo(spot2);
        spot1.setId(null);
        assertThat(spot1).isNotEqualTo(spot2);
    }
}
