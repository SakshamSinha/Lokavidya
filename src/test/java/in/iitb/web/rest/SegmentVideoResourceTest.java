package in.iitb.web.rest;

import in.iitb.Application;
import in.iitb.domain.SegmentVideo;
import in.iitb.repository.SegmentVideoRepository;
import in.iitb.repository.search.SegmentVideoSearchRepository;

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
 * Test class for the SegmentVideoResource REST controller.
 *
 * @see SegmentVideoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SegmentVideoResourceTest {


    private static final Boolean DEFAULT_SYNC = false;
    private static final Boolean UPDATED_SYNC = true;

    @Inject
    private SegmentVideoRepository segmentVideoRepository;

    @Inject
    private SegmentVideoSearchRepository segmentVideoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSegmentVideoMockMvc;

    private SegmentVideo segmentVideo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SegmentVideoResource segmentVideoResource = new SegmentVideoResource();
        ReflectionTestUtils.setField(segmentVideoResource, "segmentVideoRepository", segmentVideoRepository);
        ReflectionTestUtils.setField(segmentVideoResource, "segmentVideoSearchRepository", segmentVideoSearchRepository);
        this.restSegmentVideoMockMvc = MockMvcBuilders.standaloneSetup(segmentVideoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        segmentVideo = new SegmentVideo();
        segmentVideo.setSync(DEFAULT_SYNC);
    }

    @Test
    @Transactional
    public void createSegmentVideo() throws Exception {
        int databaseSizeBeforeCreate = segmentVideoRepository.findAll().size();

        // Create the SegmentVideo

        restSegmentVideoMockMvc.perform(post("/api/segmentVideos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(segmentVideo)))
                .andExpect(status().isCreated());

        // Validate the SegmentVideo in the database
        List<SegmentVideo> segmentVideos = segmentVideoRepository.findAll();
        assertThat(segmentVideos).hasSize(databaseSizeBeforeCreate + 1);
        SegmentVideo testSegmentVideo = segmentVideos.get(segmentVideos.size() - 1);
        assertThat(testSegmentVideo.getSync()).isEqualTo(DEFAULT_SYNC);
    }

    @Test
    @Transactional
    public void checkSyncIsRequired() throws Exception {
        int databaseSizeBeforeTest = segmentVideoRepository.findAll().size();
        // set the field null
        segmentVideo.setSync(null);

        // Create the SegmentVideo, which fails.

        restSegmentVideoMockMvc.perform(post("/api/segmentVideos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(segmentVideo)))
                .andExpect(status().isBadRequest());

        List<SegmentVideo> segmentVideos = segmentVideoRepository.findAll();
        assertThat(segmentVideos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSegmentVideos() throws Exception {
        // Initialize the database
        segmentVideoRepository.saveAndFlush(segmentVideo);

        // Get all the segmentVideos
        restSegmentVideoMockMvc.perform(get("/api/segmentVideos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(segmentVideo.getId().intValue())))
                .andExpect(jsonPath("$.[*].sync").value(hasItem(DEFAULT_SYNC.booleanValue())));
    }

    @Test
    @Transactional
    public void getSegmentVideo() throws Exception {
        // Initialize the database
        segmentVideoRepository.saveAndFlush(segmentVideo);

        // Get the segmentVideo
        restSegmentVideoMockMvc.perform(get("/api/segmentVideos/{id}", segmentVideo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(segmentVideo.getId().intValue()))
            .andExpect(jsonPath("$.sync").value(DEFAULT_SYNC.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSegmentVideo() throws Exception {
        // Get the segmentVideo
        restSegmentVideoMockMvc.perform(get("/api/segmentVideos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSegmentVideo() throws Exception {
        // Initialize the database
        segmentVideoRepository.saveAndFlush(segmentVideo);

		int databaseSizeBeforeUpdate = segmentVideoRepository.findAll().size();

        // Update the segmentVideo
        segmentVideo.setSync(UPDATED_SYNC);

        restSegmentVideoMockMvc.perform(put("/api/segmentVideos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(segmentVideo)))
                .andExpect(status().isOk());

        // Validate the SegmentVideo in the database
        List<SegmentVideo> segmentVideos = segmentVideoRepository.findAll();
        assertThat(segmentVideos).hasSize(databaseSizeBeforeUpdate);
        SegmentVideo testSegmentVideo = segmentVideos.get(segmentVideos.size() - 1);
        assertThat(testSegmentVideo.getSync()).isEqualTo(UPDATED_SYNC);
    }

    @Test
    @Transactional
    public void deleteSegmentVideo() throws Exception {
        // Initialize the database
        segmentVideoRepository.saveAndFlush(segmentVideo);

		int databaseSizeBeforeDelete = segmentVideoRepository.findAll().size();

        // Get the segmentVideo
        restSegmentVideoMockMvc.perform(delete("/api/segmentVideos/{id}", segmentVideo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SegmentVideo> segmentVideos = segmentVideoRepository.findAll();
        assertThat(segmentVideos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
