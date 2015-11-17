package in.iitb.web.rest;

import in.iitb.Application;
import in.iitb.domain.Audio;
import in.iitb.repository.AudioRepository;
import in.iitb.repository.search.AudioSearchRepository;

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
 * Test class for the AudioResource REST controller.
 *
 * @see AudioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AudioResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Boolean DEFAULT_SYNC = false;
    private static final Boolean UPDATED_SYNC = true;

    @Inject
    private AudioRepository audioRepository;

    @Inject
    private AudioSearchRepository audioSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAudioMockMvc;

    private Audio audio;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AudioResource audioResource = new AudioResource();
        ReflectionTestUtils.setField(audioResource, "audioRepository", audioRepository);
        ReflectionTestUtils.setField(audioResource, "audioSearchRepository", audioSearchRepository);
        this.restAudioMockMvc = MockMvcBuilders.standaloneSetup(audioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        audio = new Audio();
        audio.setName(DEFAULT_NAME);
        audio.setSync(DEFAULT_SYNC);
    }

    @Test
    @Transactional
    public void createAudio() throws Exception {
        int databaseSizeBeforeCreate = audioRepository.findAll().size();

        // Create the Audio

        restAudioMockMvc.perform(post("/api/audios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(audio)))
                .andExpect(status().isCreated());

        // Validate the Audio in the database
        List<Audio> audios = audioRepository.findAll();
        assertThat(audios).hasSize(databaseSizeBeforeCreate + 1);
        Audio testAudio = audios.get(audios.size() - 1);
        assertThat(testAudio.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAudio.getSync()).isEqualTo(DEFAULT_SYNC);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = audioRepository.findAll().size();
        // set the field null
        audio.setName(null);

        // Create the Audio, which fails.

        restAudioMockMvc.perform(post("/api/audios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(audio)))
                .andExpect(status().isBadRequest());

        List<Audio> audios = audioRepository.findAll();
        assertThat(audios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSyncIsRequired() throws Exception {
        int databaseSizeBeforeTest = audioRepository.findAll().size();
        // set the field null
        audio.setSync(null);

        // Create the Audio, which fails.

        restAudioMockMvc.perform(post("/api/audios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(audio)))
                .andExpect(status().isBadRequest());

        List<Audio> audios = audioRepository.findAll();
        assertThat(audios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAudios() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audios
        restAudioMockMvc.perform(get("/api/audios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(audio.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].sync").value(hasItem(DEFAULT_SYNC.booleanValue())));
    }

    @Test
    @Transactional
    public void getAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get the audio
        restAudioMockMvc.perform(get("/api/audios/{id}", audio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(audio.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sync").value(DEFAULT_SYNC.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAudio() throws Exception {
        // Get the audio
        restAudioMockMvc.perform(get("/api/audios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

		int databaseSizeBeforeUpdate = audioRepository.findAll().size();

        // Update the audio
        audio.setName(UPDATED_NAME);
        audio.setSync(UPDATED_SYNC);

        restAudioMockMvc.perform(put("/api/audios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(audio)))
                .andExpect(status().isOk());

        // Validate the Audio in the database
        List<Audio> audios = audioRepository.findAll();
        assertThat(audios).hasSize(databaseSizeBeforeUpdate);
        Audio testAudio = audios.get(audios.size() - 1);
        assertThat(testAudio.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAudio.getSync()).isEqualTo(UPDATED_SYNC);
    }

    @Test
    @Transactional
    public void deleteAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

		int databaseSizeBeforeDelete = audioRepository.findAll().size();

        // Get the audio
        restAudioMockMvc.perform(delete("/api/audios/{id}", audio.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Audio> audios = audioRepository.findAll();
        assertThat(audios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
