package in.iitb.web.rest;

import in.iitb.Application;
import in.iitb.domain.Tutorial;
import in.iitb.repository.TutorialRepository;
import in.iitb.repository.search.TutorialSearchRepository;

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


/**
 * Test class for the TutorialResource REST controller.
 *
 * @see TutorialResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TutorialResourceTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private TutorialRepository tutorialRepository;

    @Inject
    private TutorialSearchRepository tutorialSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTutorialMockMvc;

    private Tutorial tutorial;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TutorialResource tutorialResource = new TutorialResource();
        ReflectionTestUtils.setField(tutorialResource, "tutorialRepository", tutorialRepository);
        ReflectionTestUtils.setField(tutorialResource, "tutorialSearchRepository", tutorialSearchRepository);
        this.restTutorialMockMvc = MockMvcBuilders.standaloneSetup(tutorialResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tutorial = new Tutorial();
        tutorial.setTitle(DEFAULT_TITLE);
        tutorial.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTutorial() throws Exception {
        int databaseSizeBeforeCreate = tutorialRepository.findAll().size();

        // Create the Tutorial

        restTutorialMockMvc.perform(post("/api/tutorials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tutorial)))
                .andExpect(status().isCreated());

        // Validate the Tutorial in the database
        List<Tutorial> tutorials = tutorialRepository.findAll();
        assertThat(tutorials).hasSize(databaseSizeBeforeCreate + 1);
        Tutorial testTutorial = tutorials.get(tutorials.size() - 1);
        assertThat(testTutorial.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTutorial.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorialRepository.findAll().size();
        // set the field null
        tutorial.setTitle(null);

        // Create the Tutorial, which fails.

        restTutorialMockMvc.perform(post("/api/tutorials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tutorial)))
                .andExpect(status().isBadRequest());

        List<Tutorial> tutorials = tutorialRepository.findAll();
        assertThat(tutorials).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorialRepository.findAll().size();
        // set the field null
        tutorial.setDescription(null);

        // Create the Tutorial, which fails.

        restTutorialMockMvc.perform(post("/api/tutorials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tutorial)))
                .andExpect(status().isBadRequest());

        List<Tutorial> tutorials = tutorialRepository.findAll();
        assertThat(tutorials).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTutorials() throws Exception {
        // Initialize the database
        tutorialRepository.saveAndFlush(tutorial);

        // Get all the tutorials
        restTutorialMockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tutorial.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTutorial() throws Exception {
        // Initialize the database
        tutorialRepository.saveAndFlush(tutorial);

        // Get the tutorial
        restTutorialMockMvc.perform(get("/api/tutorials/{id}", tutorial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tutorial.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTutorial() throws Exception {
        // Get the tutorial
        restTutorialMockMvc.perform(get("/api/tutorials/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTutorial() throws Exception {
        // Initialize the database
        tutorialRepository.saveAndFlush(tutorial);

		int databaseSizeBeforeUpdate = tutorialRepository.findAll().size();

        // Update the tutorial
        tutorial.setTitle(UPDATED_TITLE);
        tutorial.setDescription(UPDATED_DESCRIPTION);

        restTutorialMockMvc.perform(put("/api/tutorials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tutorial)))
                .andExpect(status().isOk());

        // Validate the Tutorial in the database
        List<Tutorial> tutorials = tutorialRepository.findAll();
        assertThat(tutorials).hasSize(databaseSizeBeforeUpdate);
        Tutorial testTutorial = tutorials.get(tutorials.size() - 1);
        assertThat(testTutorial.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTutorial.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteTutorial() throws Exception {
        // Initialize the database
        tutorialRepository.saveAndFlush(tutorial);

		int databaseSizeBeforeDelete = tutorialRepository.findAll().size();

        // Get the tutorial
        restTutorialMockMvc.perform(delete("/api/tutorials/{id}", tutorial.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tutorial> tutorials = tutorialRepository.findAll();
        assertThat(tutorials).hasSize(databaseSizeBeforeDelete - 1);
    }
}
