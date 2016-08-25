package com.bukodi.p04.web.rest;

import com.bukodi.p04.P04App;
import com.bukodi.p04.domain.Foo;
import com.bukodi.p04.repository.FooRepository;
import com.bukodi.p04.service.FooService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bukodi.p04.domain.enumeration.Planets;
/**
 * Test class for the FooResource REST controller.
 *
 * @see FooResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = P04App.class)
public class FooResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_OPTIONAL_STRING = "AAAAA";
    private static final String UPDATED_OPTIONAL_STRING = "BBBBB";
    private static final String DEFAULT_REQUIRED_STRING = "AAAAA";
    private static final String UPDATED_REQUIRED_STRING = "BBBBB";

    private static final ZonedDateTime DEFAULT_ZONED_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_ZONED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_ZONED_DATE_TIME_STR = dateTimeFormatter.format(DEFAULT_ZONED_DATE_TIME);

    private static final Boolean DEFAULT_LOGICAL = false;
    private static final Boolean UPDATED_LOGICAL = true;

    private static final byte[] DEFAULT_BLOB_FIELD = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BLOB_FIELD = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_BLOB_FIELD_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BLOB_FIELD_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_BLOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_BLOB = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_BLOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_BLOB_CONTENT_TYPE = "image/png";

    private static final Planets DEFAULT_ENUM_FIELD = Planets.mercur;
    private static final Planets UPDATED_ENUM_FIELD = Planets.venus;

    @Inject
    private FooRepository fooRepository;

    @Inject
    private FooService fooService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFooMockMvc;

    private Foo foo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FooResource fooResource = new FooResource();
        ReflectionTestUtils.setField(fooResource, "fooService", fooService);
        this.restFooMockMvc = MockMvcBuilders.standaloneSetup(fooResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Foo createEntity(EntityManager em) {
        Foo foo = new Foo();
        foo = new Foo()
                .optionalString(DEFAULT_OPTIONAL_STRING)
                .requiredString(DEFAULT_REQUIRED_STRING)
                .zonedDateTime(DEFAULT_ZONED_DATE_TIME)
                .logical(DEFAULT_LOGICAL)
                .blobField(DEFAULT_BLOB_FIELD)
                .blobFieldContentType(DEFAULT_BLOB_FIELD_CONTENT_TYPE)
                .imageBlob(DEFAULT_IMAGE_BLOB)
                .imageBlobContentType(DEFAULT_IMAGE_BLOB_CONTENT_TYPE)
                .enumField(DEFAULT_ENUM_FIELD);
        return foo;
    }

    @Before
    public void initTest() {
        foo = createEntity(em);
    }

    @Test
    @Transactional
    public void createFoo() throws Exception {
        int databaseSizeBeforeCreate = fooRepository.findAll().size();

        // Create the Foo

        restFooMockMvc.perform(post("/api/foos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(foo)))
                .andExpect(status().isCreated());

        // Validate the Foo in the database
        List<Foo> foos = fooRepository.findAll();
        assertThat(foos).hasSize(databaseSizeBeforeCreate + 1);
        Foo testFoo = foos.get(foos.size() - 1);
        assertThat(testFoo.getOptionalString()).isEqualTo(DEFAULT_OPTIONAL_STRING);
        assertThat(testFoo.getRequiredString()).isEqualTo(DEFAULT_REQUIRED_STRING);
        assertThat(testFoo.getZonedDateTime()).isEqualTo(DEFAULT_ZONED_DATE_TIME);
        assertThat(testFoo.isLogical()).isEqualTo(DEFAULT_LOGICAL);
        assertThat(testFoo.getBlobField()).isEqualTo(DEFAULT_BLOB_FIELD);
        assertThat(testFoo.getBlobFieldContentType()).isEqualTo(DEFAULT_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testFoo.getImageBlob()).isEqualTo(DEFAULT_IMAGE_BLOB);
        assertThat(testFoo.getImageBlobContentType()).isEqualTo(DEFAULT_IMAGE_BLOB_CONTENT_TYPE);
        assertThat(testFoo.getEnumField()).isEqualTo(DEFAULT_ENUM_FIELD);
    }

    @Test
    @Transactional
    public void checkRequiredStringIsRequired() throws Exception {
        int databaseSizeBeforeTest = fooRepository.findAll().size();
        // set the field null
        foo.setRequiredString(null);

        // Create the Foo, which fails.

        restFooMockMvc.perform(post("/api/foos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(foo)))
                .andExpect(status().isBadRequest());

        List<Foo> foos = fooRepository.findAll();
        assertThat(foos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFoos() throws Exception {
        // Initialize the database
        fooRepository.saveAndFlush(foo);

        // Get all the foos
        restFooMockMvc.perform(get("/api/foos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(foo.getId().intValue())))
                .andExpect(jsonPath("$.[*].optionalString").value(hasItem(DEFAULT_OPTIONAL_STRING.toString())))
                .andExpect(jsonPath("$.[*].requiredString").value(hasItem(DEFAULT_REQUIRED_STRING.toString())))
                .andExpect(jsonPath("$.[*].zonedDateTime").value(hasItem(DEFAULT_ZONED_DATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].logical").value(hasItem(DEFAULT_LOGICAL.booleanValue())))
                .andExpect(jsonPath("$.[*].blobFieldContentType").value(hasItem(DEFAULT_BLOB_FIELD_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].blobField").value(hasItem(Base64Utils.encodeToString(DEFAULT_BLOB_FIELD))))
                .andExpect(jsonPath("$.[*].imageBlobContentType").value(hasItem(DEFAULT_IMAGE_BLOB_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].imageBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_BLOB))))
                .andExpect(jsonPath("$.[*].enumField").value(hasItem(DEFAULT_ENUM_FIELD.toString())));
    }

    @Test
    @Transactional
    public void getFoo() throws Exception {
        // Initialize the database
        fooRepository.saveAndFlush(foo);

        // Get the foo
        restFooMockMvc.perform(get("/api/foos/{id}", foo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(foo.getId().intValue()))
            .andExpect(jsonPath("$.optionalString").value(DEFAULT_OPTIONAL_STRING.toString()))
            .andExpect(jsonPath("$.requiredString").value(DEFAULT_REQUIRED_STRING.toString()))
            .andExpect(jsonPath("$.zonedDateTime").value(DEFAULT_ZONED_DATE_TIME_STR))
            .andExpect(jsonPath("$.logical").value(DEFAULT_LOGICAL.booleanValue()))
            .andExpect(jsonPath("$.blobFieldContentType").value(DEFAULT_BLOB_FIELD_CONTENT_TYPE))
            .andExpect(jsonPath("$.blobField").value(Base64Utils.encodeToString(DEFAULT_BLOB_FIELD)))
            .andExpect(jsonPath("$.imageBlobContentType").value(DEFAULT_IMAGE_BLOB_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageBlob").value(Base64Utils.encodeToString(DEFAULT_IMAGE_BLOB)))
            .andExpect(jsonPath("$.enumField").value(DEFAULT_ENUM_FIELD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFoo() throws Exception {
        // Get the foo
        restFooMockMvc.perform(get("/api/foos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFoo() throws Exception {
        // Initialize the database
        fooService.save(foo);

        int databaseSizeBeforeUpdate = fooRepository.findAll().size();

        // Update the foo
        Foo updatedFoo = fooRepository.findOne(foo.getId());
        updatedFoo
                .optionalString(UPDATED_OPTIONAL_STRING)
                .requiredString(UPDATED_REQUIRED_STRING)
                .zonedDateTime(UPDATED_ZONED_DATE_TIME)
                .logical(UPDATED_LOGICAL)
                .blobField(UPDATED_BLOB_FIELD)
                .blobFieldContentType(UPDATED_BLOB_FIELD_CONTENT_TYPE)
                .imageBlob(UPDATED_IMAGE_BLOB)
                .imageBlobContentType(UPDATED_IMAGE_BLOB_CONTENT_TYPE)
                .enumField(UPDATED_ENUM_FIELD);

        restFooMockMvc.perform(put("/api/foos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFoo)))
                .andExpect(status().isOk());

        // Validate the Foo in the database
        List<Foo> foos = fooRepository.findAll();
        assertThat(foos).hasSize(databaseSizeBeforeUpdate);
        Foo testFoo = foos.get(foos.size() - 1);
        assertThat(testFoo.getOptionalString()).isEqualTo(UPDATED_OPTIONAL_STRING);
        assertThat(testFoo.getRequiredString()).isEqualTo(UPDATED_REQUIRED_STRING);
        assertThat(testFoo.getZonedDateTime()).isEqualTo(UPDATED_ZONED_DATE_TIME);
        assertThat(testFoo.isLogical()).isEqualTo(UPDATED_LOGICAL);
        assertThat(testFoo.getBlobField()).isEqualTo(UPDATED_BLOB_FIELD);
        assertThat(testFoo.getBlobFieldContentType()).isEqualTo(UPDATED_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testFoo.getImageBlob()).isEqualTo(UPDATED_IMAGE_BLOB);
        assertThat(testFoo.getImageBlobContentType()).isEqualTo(UPDATED_IMAGE_BLOB_CONTENT_TYPE);
        assertThat(testFoo.getEnumField()).isEqualTo(UPDATED_ENUM_FIELD);
    }

    @Test
    @Transactional
    public void deleteFoo() throws Exception {
        // Initialize the database
        fooService.save(foo);

        int databaseSizeBeforeDelete = fooRepository.findAll().size();

        // Get the foo
        restFooMockMvc.perform(delete("/api/foos/{id}", foo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Foo> foos = fooRepository.findAll();
        assertThat(foos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
