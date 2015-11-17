package in.iitb.web.rest;

import com.codahale.metrics.annotation.Timed;
import in.iitb.domain.Audio;
import in.iitb.repository.AudioRepository;
import in.iitb.repository.search.AudioSearchRepository;
import in.iitb.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Audio.
 */
@RestController
@RequestMapping("/api")
public class AudioResource {

    private final Logger log = LoggerFactory.getLogger(AudioResource.class);

    @Inject
    private AudioRepository audioRepository;

    @Inject
    private AudioSearchRepository audioSearchRepository;

    /**
     * POST  /audios -> Create a new audio.
     */
    @RequestMapping(value = "/audios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Audio> createAudio(@Valid @RequestBody Audio audio) throws URISyntaxException {
        log.debug("REST request to save Audio : {}", audio);
        if (audio.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new audio cannot already have an ID").body(null);
        }
        Audio result = audioRepository.save(audio);
        audioSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/audios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("audio", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /audios -> Updates an existing audio.
     */
    @RequestMapping(value = "/audios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Audio> updateAudio(@Valid @RequestBody Audio audio) throws URISyntaxException {
        log.debug("REST request to update Audio : {}", audio);
        if (audio.getId() == null) {
            return createAudio(audio);
        }
        Audio result = audioRepository.save(audio);
        audioSearchRepository.save(audio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("audio", audio.getId().toString()))
            .body(result);
    }

    /**
     * GET  /audios -> get all the audios.
     */
    @RequestMapping(value = "/audios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Audio> getAllAudios() {
        log.debug("REST request to get all Audios");
        return audioRepository.findAll();
    }

    /**
     * GET  /audios/:id -> get the "id" audio.
     */
    @RequestMapping(value = "/audios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Audio> getAudio(@PathVariable Long id) {
        log.debug("REST request to get Audio : {}", id);
        return Optional.ofNullable(audioRepository.findOne(id))
            .map(audio -> new ResponseEntity<>(
                audio,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /audios/:id -> delete the "id" audio.
     */
    @RequestMapping(value = "/audios/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAudio(@PathVariable Long id) {
        log.debug("REST request to delete Audio : {}", id);
        audioRepository.delete(id);
        audioSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("audio", id.toString())).build();
    }

    /**
     * SEARCH  /_search/audios/:query -> search for the audio corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/audios/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Audio> searchAudios(@PathVariable String query) {
        return StreamSupport
            .stream(audioSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
