package in.iitb.web.rest;

import in.iitb.Application;
import in.iitb.domain.ExtVideo;
import in.iitb.repository.ExtVideoRepository;
import in.iitb.repository.search.ExtVideoSearchRepository;

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
 * Test class for the ExtVideoResource REST controller.
 *
 * @see ExtVideoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ExtVideoResourceTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    private static final Boolean DEFAULT_IS_YOUTUBE = false;
    private static final Boolean UPDATED_IS_YOUTUBE = true;

    @Inject
    private ExtVideoRepository extVideoRepository;

    @Inject
    private ExtVideoSearchRepository extVideoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restExtVideoMockMvc;

    private ExtVideo extVideo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExtVideoResource extVideoResource = new ExtVideoResource();
        ReflectionTestUtils.setField(extVideoResource, "extVideoRepository", extVideoRepository);
        ReflectionTestUtils.setField(extVideoResource, "extVideoSearchRepository", extVideoSearchRepository);
        this.restExtVideoMockMvc = MockMvcBuilders.standaloneSetup(extVideoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        extVideo = new ExtVideo();
        extVideo.setTitle(DEFAULT_TITLE);
        extVideo.setUrl(DEFAULT_URL);
        extVideo.setIsYoutube(DEFAULT_IS_YOUTUBE);
    }

    @Test
    @Transactional
    public void createExtVideo() throws Exception {
        int databaseSizeBeforeCreate = extVideoRepository.findAll().size();

        // Create the ExtVideo

        restExtVideoMockMvc.perform(post("/api/extVideos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(extVideo)))
                .andExpect(status().isCreated());

        // Validate the ExtVideo in the database
        List<ExtVideo> extVideos = extVideoRepository.findAll();
        assertThat(extVideos).hasSize(databaseSizeBeforeCreate + 1);
        ExtVideo testExtVideo = extVideos.get(extVideos.size() - 1);
        assertThat(testExtVideo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testExtVideo.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testExtVideo.getIsYoutube()).isEqualTo(DEFAULT_IS_YOUTUBE);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = extVideoRepository.findAll().size();
        // set the field null
        extVideo.setTitle(null);

        // Create the ExtVideo, which fails.

        restExtVideoMockMvc.perform(post("/api/extVideos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(extVideo)))
                .andExpect(status().isBadRequest());

        List<ExtVideo> extVideos = extVideoRepository.findAll();
        assertThat(extVideos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = extVideoRepository.findAll().size();
        // set the field null
        extVideo.setUrl(null);

        // Create the ExtVideo, which fails.

        restExtVideoMockMvc.perform(post("/api/extVideos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(extVideo)))
                .andExpect(status().isBadRequest());

        List<ExtVideo> extVideos = extVideoRepository.findAll();
        assertThat(extVideos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsYoutubeIsRequired() throws Exception {
        int databaseSizeBeforeTest = extVideoRepository.findAll().size();
        // set the field null
        extVideo.setIsYoutube(null);

        // Create the ExtVideo, which fails.

        restExtVideoMockMvc.perform(post("/api/extVideos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(extVideo)))
                .andExpect(status().isBadRequest());

        List<ExtVideo> extVideos = extVideoRepository.findAll();
        assertThat(extVideos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExtVideos() throws Exception {
        // Initialize the database
        extVideoRepository.saveAndFlush(extVideo);

        // Get all the extVideos
        restExtVideoMockMvc.perform(get("/api/extVideos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(extVideo.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].isYoutube").value(hasItem(DEFAULT_IS_YOUTUBE.booleanValue())));
    }

    @Test
    @Transactional
    public void getExtVideo() throws Exception {
        // Initialize the database
        extVideoRepository.saveAndFlush(extVideo);

        // Get the extVideo
        restExtVideoMockMvc.perform(get("/api/extVideos/{id}", extVideo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(extVideo.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.isYoutube").value(DEFAULT_IS_YOUTUBE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExtVideo() throws Exception {
        // Get the extVideo
        restExtVideoMockMvc.perform(get("/api/extVideos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtVideo() throws Exception {
        // Initialize the database
        extVideoRepository.saveAndFlush(extVideo);

		int databaseSizeBeforeUpdate = extVideoRepository.findAll().size();

        // Update the extVideo
        extVideo.setTitle(UPDATED_TITLE);
        extVideo.setUrl(UPDATED_URL);
        extVideo.setIsYoutube(UPDATED_IS_YOUTUBE);

        restExtVideoMockMvc.perform(put("/api/extVideos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(extVideo)))
                .andExpect(status().isOk());

        // Validate the ExtVideo in the database
        List<ExtVideo> extVideos = extVideoRepository.findAll();
        assertThat(extVideos).hasSize(databaseSizeBeforeUpdate);
        ExtVideo testExtVideo = extVideos.get(extVideos.size() - 1);
        assertThat(testExtVideo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testExtVideo.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testExtVideo.getIsYoutube()).isEqualTo(UPDATED_IS_YOUTUBE);
    }

    @Test
    @Transactional
    public void deleteExtVideo() throws Exception {
        // Initialize the database
        extVideoRepository.saveAndFlush(extVideo);

		int databaseSizeBeforeDelete = extVideoRepository.findAll().size();

        // Get the extVideo
        restExtVideoMockMvc.perform(delete("/api/extVideos/{id}", extVideo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ExtVideo> extVideos = extVideoRepository.findAll();
        assertThat(extVideos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
