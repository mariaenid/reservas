package com.reservas.org.web.rest;

import com.reservas.org.ReservasApp;

import com.reservas.org.config.ApplicationProperties;
import com.reservas.org.domain.Reserva;
import com.reservas.org.repository.ReservaRepository;
import com.reservas.org.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.reservas.org.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReservaResource REST controller.
 *
 * @see ReservaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReservasApp.class)
@EnableConfigurationProperties({ApplicationProperties.class})
public class ReservaResourceIntTest {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ReservaRepository reservaRepository;

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

    private MockMvc restReservaMockMvc;

    private Reserva reserva;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservaResource reservaResource = new ReservaResource(reservaRepository);
        this.restReservaMockMvc = MockMvcBuilders.standaloneSetup(reservaResource)
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
    public static Reserva createEntity(EntityManager em) {
        Reserva reserva = new Reserva()
            .fecha(DEFAULT_FECHA);
        return reserva;
    }

    @Before
    public void initTest() {
        reserva = createEntity(em);
    }

    @Test
    @Transactional
    public void createReserva() throws Exception {
        int databaseSizeBeforeCreate = reservaRepository.findAll().size();
        // Create the Reserva
        restReservaMockMvc.perform(post("/api/reservas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isCreated());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeCreate + 1);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    public void createReservaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservaRepository.findAll().size();

        // Create the Reserva with an existing ID
        reserva.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservaMockMvc.perform(post("/api/reservas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReservas() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList
        restReservaMockMvc.perform(get("/api/reservas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reserva.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }
    
    @Test
    @Transactional
    public void getReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get the reserva
        restReservaMockMvc.perform(get("/api/reservas/{id}", reserva.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reserva.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReserva() throws Exception {
        // Get the reserva
        restReservaMockMvc.perform(get("/api/reservas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();

        // Update the reserva
        Reserva updatedReserva = reservaRepository.findById(reserva.getId()).get();
        // Disconnect from session so that the updates on updatedReserva are not directly saved in db
        em.detach(updatedReserva);
        updatedReserva
            .fecha(UPDATED_FECHA);

        restReservaMockMvc.perform(put("/api/reservas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReserva)))
            .andExpect(status().isOk());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void updateNonExistingReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();

        // Create the Reserva

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservaMockMvc.perform(put("/api/reservas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserva)))
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeDelete = reservaRepository.findAll().size();

        // Get the reserva
        restReservaMockMvc.perform(delete("/api/reservas/{id}", reserva.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reserva.class);
        Reserva reserva1 = new Reserva();
        reserva1.setId(1L);
        Reserva reserva2 = new Reserva();
        reserva2.setId(reserva1.getId());
        assertThat(reserva1).isEqualTo(reserva2);
        reserva2.setId(2L);
        assertThat(reserva1).isNotEqualTo(reserva2);
        reserva1.setId(null);
        assertThat(reserva1).isNotEqualTo(reserva2);
    }
}
