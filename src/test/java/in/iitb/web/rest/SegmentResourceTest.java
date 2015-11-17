package in.iitb.web.rest;

import in.iitb.Application;
import in.iitb.domain.Segment;
import in.iitb.repository.SegmentRepository;
import in.iitb.repository.search.SegmentSearchRepository;

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
 * Test class for the SegmentResource REST controller.
 *
 * @see SegmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SegmentResourceTest {


    private static final Boolean DEFAULT_SYNC = false;
    private static final Boolean UPDATED_SYNC = true;

    @Inject
    private SegmentRepository segmentRepository;

    @Inject
    private SegmentSearchRepository segmentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSegmentMockMvc;

    private Segment segment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SegmentResource segmentResource = new SegmentResource();
        ReflectionTestUtils.setField(segmentResource, "segmentRepository", segmentRepository);
        ReflectionTestUtils.setField(segmentResource, "segmentSearchRepository", segmentSearchRepository);
        this.restSegmentMockMvc = MockMvcBuilders.standaloneSetup(segmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        segment = new Segment();
        segment.setSync(DEFAULT_SYNC);
    }

    @Test
    @Transactional
    public void createSegment() throws Exception {
        int databaseSizeBeforeCreate = segmentRepository.findAll().size();

        // Create the Segment

        restSegmentMockMvc.perform(post("/api/segments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(segment)))
                .andExpect(status().isCreated());

        // Validate the Segment in the database
        List<Segment> segments = segmentRepository.findAll();
        assertThat(segments).hasSize(databaseSizeBeforeCreate + 1);
        Segment testSegment = segments.get(segments.size() - 1);
        assertThat(testSegment.getSync()).isEqualTo(DEFAULT_SYNC);
    }

    @Test
    @Transactional
    public void checkSyncIsRequired() throws Exception {
        int databaseSizeBeforeTest = segmentRepository.findAll().size();
        // set the field null
        segment.setSync(null);

        // Create the Segment, which fails.

        restSegmentMockMvc.perform(post("/api/segments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(segment)))
                .andExpect(status().isBadRequest());

        List<Segment> segments = segmentRepository.findAll();
        assertThat(segments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSegments() throws Exception {
        // Initialize the database
        segmentRepository.saveAndFlush(segment);

        // Get all the segments
        restSegmentMockMvc.perform(get("/api/segments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(segment.getId().intValue())))
                .andExpect(jsonPath("$.[*].sync").value(hasItem(DEFAULT_SYNC.booleanValue())));
    }

    @Test
    @Transactional
    public void getSegment() throws Exception {
        // Initialize the database
        segmentRepository.saveAndFlush(segment);

        // Get the segment
        restSegmentMockMvc.perform(get("/api/segments/{id}", segment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(segment.getId().intValue()))
            .andExpect(jsonPath("$.sync").value(DEFAULT_SYNC.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSegment() throws Exception {
        // Get the segment
        restSegmentMockMvc.perform(get("/api/segments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSegment() throws Exception {
        // Initialize the database
        segmentRepository.saveAndFlush(segment);

		int databaseSizeBeforeUpdate = segmentRepository.findAll().size();

        // Update the segment
        segment.setSync(UPDATED_SYNC);

        restSegmentMockMvc.perform(put("/api/segments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(segment)))
                .andExpect(status().isOk());

        // Validate the Segment in the database
        List<Segment> segments = segmentRepository.findAll();
        assertThat(segments).hasSize(databaseSizeBeforeUpdate);
        Segment testSegment = segments.get(segments.size() - 1);
        assertThat(testSegment.getSync()).isEqualTo(UPDATED_SYNC);
    }

    @Test
    @Transactional
    public void deleteSegment() throws Exception {
        // Initialize the database
        segmentRepository.saveAndFlush(segment);

		int databaseSizeBeforeDelete = segmentRepository.findAll().size();

        // Get the segment
        restSegmentMockMvc.perform(delete("/api/segments/{id}", segment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Segment> segments = segmentRepository.findAll();
        assertThat(segments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
