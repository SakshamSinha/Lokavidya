package in.iitb.web.rest;

import in.iitb.Application;
import in.iitb.domain.Ownership;
import in.iitb.repository.OwnershipRepository;
import in.iitb.repository.search.OwnershipSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import in.iitb.domain.enumeration.OwnershipType;
import in.iitb.domain.enumeration.Rights;

/**
 * Test class for the OwnershipResource REST controller.
 *
 * @see OwnershipResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OwnershipResourceTest {



private static final OwnershipType DEFAULT_TYPE = OwnershipType.PROJ;
    private static final OwnershipType UPDATED_TYPE = OwnershipType.TUTORIAL;


private static final Rights DEFAULT_RIGHT = Rights.ADMIN;
    private static final Rights UPDATED_RIGHT = Rights.CONTRIBUTOR;

    @Inject
    private OwnershipRepository ownershipRepository;

    @Inject
    private OwnershipSearchRepository ownershipSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOwnershipMockMvc;

    private Ownership ownership;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OwnershipResource ownershipResource = new OwnershipResource();
        ReflectionTestUtils.setField(ownershipResource, "ownershipRepository", ownershipRepository);
        ReflectionTestUtils.setField(ownershipResource, "ownershipSearchRepository", ownershipSearchRepository);
        this.restOwnershipMockMvc = MockMvcBuilders.standaloneSetup(ownershipResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ownership = new Ownership();
        ownership.setType(DEFAULT_TYPE);
        ownership.setRight(DEFAULT_RIGHT);
    }

    @Test
    @Transactional
    public void createOwnership() throws Exception {
        int databaseSizeBeforeCreate = ownershipRepository.findAll().size();

        // Create the Ownership

        restOwnershipMockMvc.perform(post("/api/ownerships")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ownership)))
                .andExpect(status().isCreated());

        // Validate the Ownership in the database
        List<Ownership> ownerships = ownershipRepository.findAll();
        assertThat(ownerships).hasSize(databaseSizeBeforeCreate + 1);
        Ownership testOwnership = ownerships.get(ownerships.size() - 1);
        assertThat(testOwnership.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOwnership.getRight()).isEqualTo(DEFAULT_RIGHT);
    }

    @Test
    @Transactional
    public void getAllOwnerships() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownerships
        restOwnershipMockMvc.perform(get("/api/ownerships"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ownership.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].right").value(hasItem(DEFAULT_RIGHT.toString())));
    }

    @Test
    @Transactional
    public void getOwnership() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get the ownership
        restOwnershipMockMvc.perform(get("/api/ownerships/{id}", ownership.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ownership.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.right").value(DEFAULT_RIGHT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOwnership() throws Exception {
        // Get the ownership
        restOwnershipMockMvc.perform(get("/api/ownerships/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOwnership() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

		int databaseSizeBeforeUpdate = ownershipRepository.findAll().size();

        // Update the ownership
        ownership.setType(UPDATED_TYPE);
        ownership.setRight(UPDATED_RIGHT);

        restOwnershipMockMvc.perform(put("/api/ownerships")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ownership)))
                .andExpect(status().isOk());

        // Validate the Ownership in the database
        List<Ownership> ownerships = ownershipRepository.findAll();
        assertThat(ownerships).hasSize(databaseSizeBeforeUpdate);
        Ownership testOwnership = ownerships.get(ownerships.size() - 1);
        assertThat(testOwnership.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOwnership.getRight()).isEqualTo(UPDATED_RIGHT);
    }

    @Test
    @Transactional
    public void deleteOwnership() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

		int databaseSizeBeforeDelete = ownershipRepository.findAll().size();

        // Get the ownership
        restOwnershipMockMvc.perform(delete("/api/ownerships/{id}", ownership.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ownership> ownerships = ownershipRepository.findAll();
        assertThat(ownerships).hasSize(databaseSizeBeforeDelete - 1);
    }
}
