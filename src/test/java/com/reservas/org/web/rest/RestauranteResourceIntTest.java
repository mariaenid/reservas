package com.reservas.org.web.rest;

import com.reservas.org.ReservasApp;

import com.reservas.org.domain.Restaurante;
import com.reservas.org.repository.RestauranteRepository;
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
import org.springframework.util.Base64Utils;
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
 * Test class for the RestauranteResource REST controller.
 *
 * @see RestauranteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReservasApp.class)
public class RestauranteResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PROPIETARIO = "AAAAAAAAAA";
    private static final String UPDATED_PROPIETARIO = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGEN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGEN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_CONTENT_TYPE = "image/png";

    @Autowired
    private RestauranteRepository restauranteRepository;

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

    private MockMvc restRestauranteMockMvc;

    private Restaurante restaurante;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RestauranteResource restauranteResource = new RestauranteResource(restauranteRepository);
        this.restRestauranteMockMvc = MockMvcBuilders.standaloneSetup(restauranteResource)
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
    public static Restaurante createEntity(EntityManager em) {
        Restaurante restaurante = new Restaurante()
            .nombre(DEFAULT_NOMBRE)
            .fecha(DEFAULT_FECHA)
            .propietario(DEFAULT_PROPIETARIO)
            .direccion(DEFAULT_DIRECCION)
            .imagen(DEFAULT_IMAGEN)
            .imagenContentType(DEFAULT_IMAGEN_CONTENT_TYPE);
        return restaurante;
    }

    @Before
    public void initTest() {
        restaurante = createEntity(em);
    }

    @Test
    @Transactional
    public void createRestaurante() throws Exception {
        int databaseSizeBeforeCreate = restauranteRepository.findAll().size();

        // Create the Restaurante
        restRestauranteMockMvc.perform(post("/api/restaurantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurante)))
            .andExpect(status().isCreated());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurante testRestaurante = restauranteList.get(restauranteList.size() - 1);
        assertThat(testRestaurante.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testRestaurante.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testRestaurante.getPropietario()).isEqualTo(DEFAULT_PROPIETARIO);
        assertThat(testRestaurante.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testRestaurante.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testRestaurante.getImagenContentType()).isEqualTo(DEFAULT_IMAGEN_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createRestauranteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = restauranteRepository.findAll().size();

        // Create the Restaurante with an existing ID
        restaurante.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestauranteMockMvc.perform(post("/api/restaurantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurante)))
            .andExpect(status().isBadRequest());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRestaurantes() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        // Get all the restauranteList
        restRestauranteMockMvc.perform(get("/api/restaurantes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].propietario").value(hasItem(DEFAULT_PROPIETARIO.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))));
    }
    
    @Test
    @Transactional
    public void getRestaurante() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        // Get the restaurante
        restRestauranteMockMvc.perform(get("/api/restaurantes/{id}", restaurante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(restaurante.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.propietario").value(DEFAULT_PROPIETARIO.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION.toString()))
            .andExpect(jsonPath("$.imagenContentType").value(DEFAULT_IMAGEN_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagen").value(Base64Utils.encodeToString(DEFAULT_IMAGEN)));
    }

    @Test
    @Transactional
    public void getNonExistingRestaurante() throws Exception {
        // Get the restaurante
        restRestauranteMockMvc.perform(get("/api/restaurantes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRestaurante() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();

        // Update the restaurante
        Restaurante updatedRestaurante = restauranteRepository.findById(restaurante.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurante are not directly saved in db
        em.detach(updatedRestaurante);
        updatedRestaurante
            .nombre(UPDATED_NOMBRE)
            .fecha(UPDATED_FECHA)
            .propietario(UPDATED_PROPIETARIO)
            .direccion(UPDATED_DIRECCION)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE);

        restRestauranteMockMvc.perform(put("/api/restaurantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRestaurante)))
            .andExpect(status().isOk());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
        Restaurante testRestaurante = restauranteList.get(restauranteList.size() - 1);
        assertThat(testRestaurante.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testRestaurante.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testRestaurante.getPropietario()).isEqualTo(UPDATED_PROPIETARIO);
        assertThat(testRestaurante.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testRestaurante.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testRestaurante.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingRestaurante() throws Exception {
        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();

        // Create the Restaurante

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestauranteMockMvc.perform(put("/api/restaurantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurante)))
            .andExpect(status().isBadRequest());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRestaurante() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        int databaseSizeBeforeDelete = restauranteRepository.findAll().size();

        // Get the restaurante
        restRestauranteMockMvc.perform(delete("/api/restaurantes/{id}", restaurante.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Restaurante.class);
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        Restaurante restaurante2 = new Restaurante();
        restaurante2.setId(restaurante1.getId());
        assertThat(restaurante1).isEqualTo(restaurante2);
        restaurante2.setId(2L);
        assertThat(restaurante1).isNotEqualTo(restaurante2);
        restaurante1.setId(null);
        assertThat(restaurante1).isNotEqualTo(restaurante2);
    }
}
