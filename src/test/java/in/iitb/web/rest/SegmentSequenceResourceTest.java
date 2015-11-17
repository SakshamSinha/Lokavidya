package in.iitb.web.rest;

import in.iitb.Application;
import in.iitb.domain.SegmentSequence;
import in.iitb.repository.SegmentSequenceRepository;
import in.iitb.repository.search.SegmentSequenceSearchRepository;

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
 * Test class for the SegmentSequenceResource REST controller.
 *
 * @see SegmentSequenceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SegmentSequenceResourceTest {

    private static final String DEFAULT_SEGMENTSEQUENCE = "AAAAA";
    private static final String UPDATED_SEGMENTSEQUENCE = "BBBBB";

    @Inject
    private SegmentSequenceRepository segmentSequenceRepository;

    @Inject
    private SegmentSequenceSearchRepository segmentSequenceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSegmentSequenceMockMvc;

    private SegmentSequence segmentSequence;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SegmentSequenceResource segmentSequenceResource = new SegmentSequenceResource();
        ReflectionTestUtils.setField(segmentSequenceResource, "segmentSequenceRepository", segmentSequenceRepository);
        ReflectionTestUtils.setField(segmentSequenceResource, "segmentSequenceSearchRepository", segmentSequenceSearchRepository);
        this.restSegmentSequenceMockMvc = MockMvcBuilders.standaloneSetup(segmentSequenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        segmentSequence = new SegmentSequence();
        segmentSequence.setSegmentsequence(DEFAULT_SEGMENTSEQUENCE);
    }

    @Test
    @Transactional
    public void createSegmentSequence() throws Exception {
        int databaseSizeBeforeCreate = segmentSequenceRepository.findAll().size();

        // Create the SegmentSequence

        restSegmentSequenceMockMvc.perform(post("/api/segmentSequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(segmentSequence)))
                .andExpect(status().isCreated());

        // Validate the SegmentSequence in the database
        List<SegmentSequence> segmentSequences = segmentSequenceRepository.findAll();
        assertThat(segmentSequences).hasSize(databaseSizeBeforeCreate + 1);
        SegmentSequence testSegmentSequence = segmentSequences.get(segmentSequences.size() - 1);
        assertThat(testSegmentSequence.getSegmentsequence()).isEqualTo(DEFAULT_SEGMENTSEQUENCE);
    }

    @Test
    @Transactional
    public void checkSegmentsequenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = segmentSequenceRepository.findAll().size();
        // set the field null
        segmentSequence.setSegmentsequence(null);

        // Create the SegmentSequence, which fails.

        restSegmentSequenceMockMvc.perform(post("/api/segmentSequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(segmentSequence)))
                .andExpect(status().isBadRequest());

        List<SegmentSequence> segmentSequences = segmentSequenceRepository.findAll();
        assertThat(segmentSequences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSegmentSequences() throws Exception {
        // Initialize the database
        segmentSequenceRepository.saveAndFlush(segmentSequence);

        // Get all the segmentSequences
        restSegmentSequenceMockMvc.perform(get("/api/segmentSequences"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(segmentSequence.getId().intValue())))
                .andExpect(jsonPath("$.[*].segmentsequence").value(hasItem(DEFAULT_SEGMENTSEQUENCE.toString())));
    }

    @Test
    @Transactional
    public void getSegmentSequence() throws Exception {
        // Initialize the database
        segmentSequenceRepository.saveAndFlush(segmentSequence);

        // Get the segmentSequence
        restSegmentSequenceMockMvc.perform(get("/api/segmentSequences/{id}", segmentSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(segmentSequence.getId().intValue()))
            .andExpect(jsonPath("$.segmentsequence").value(DEFAULT_SEGMENTSEQUENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSegmentSequence() throws Exception {
        // Get the segmentSequence
        restSegmentSequenceMockMvc.perform(get("/api/segmentSequences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSegmentSequence() throws Exception {
        // Initialize the database
        segmentSequenceRepository.saveAndFlush(segmentSequence);

		int databaseSizeBeforeUpdate = segmentSequenceRepository.findAll().size();

        // Update the segmentSequence
        segmentSequence.setSegmentsequence(UPDATED_SEGMENTSEQUENCE);

        restSegmentSequenceMockMvc.perform(put("/api/segmentSequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(segmentSequence)))
                .andExpect(status().isOk());

        // Validate the SegmentSequence in the database
        List<SegmentSequence> segmentSequences = segmentSequenceRepository.findAll();
        assertThat(segmentSequences).hasSize(databaseSizeBeforeUpdate);
        SegmentSequence testSegmentSequence = segmentSequences.get(segmentSequences.size() - 1);
        assertThat(testSegmentSequence.getSegmentsequence()).isEqualTo(UPDATED_SEGMENTSEQUENCE);
    }

    @Test
    @Transactional
    public void deleteSegmentSequence() throws Exception {
        // Initialize the database
        segmentSequenceRepository.saveAndFlush(segmentSequence);

		int databaseSizeBeforeDelete = segmentSequenceRepository.findAll().size();

        // Get the segmentSequence
        restSegmentSequenceMockMvc.perform(delete("/api/segmentSequences/{id}", segmentSequence.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SegmentSequence> segmentSequences = segmentSequenceRepository.findAll();
        assertThat(segmentSequences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
