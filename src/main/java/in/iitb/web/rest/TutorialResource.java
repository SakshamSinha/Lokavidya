package in.iitb.web.rest;

import com.codahale.metrics.annotation.Timed;
import in.iitb.domain.Tutorial;
import in.iitb.repository.TutorialRepository;
import in.iitb.repository.search.TutorialSearchRepository;
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
 * REST controller for managing Tutorial.
 */
@RestController
@RequestMapping("/api")
public class TutorialResource {

    private final Logger log = LoggerFactory.getLogger(TutorialResource.class);

    @Inject
    private TutorialRepository tutorialRepository;

    @Inject
    private TutorialSearchRepository tutorialSearchRepository;

    /**
     * POST  /tutorials -> Create a new tutorial.
     */
    @RequestMapping(value = "/tutorials",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tutorial> createTutorial(@Valid @RequestBody Tutorial tutorial) throws URISyntaxException {
        log.debug("REST request to save Tutorial : {}", tutorial);
        if (tutorial.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new tutorial cannot already have an ID").body(null);
        }
        Tutorial result = tutorialRepository.save(tutorial);
        tutorialSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tutorials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tutorial", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tutorials -> Updates an existing tutorial.
     */
    @RequestMapping(value = "/tutorials",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tutorial> updateTutorial(@Valid @RequestBody Tutorial tutorial) throws URISyntaxException {
        log.debug("REST request to update Tutorial : {}", tutorial);
        if (tutorial.getId() == null) {
            return createTutorial(tutorial);
        }
        Tutorial result = tutorialRepository.save(tutorial);
        tutorialSearchRepository.save(tutorial);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tutorial", tutorial.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tutorials -> get all the tutorials.
     */
    @RequestMapping(value = "/tutorials",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tutorial> getAllTutorials(@RequestParam(required = false) String filter) {
        if ("tutorialsvideo-is-null".equals(filter)) {
            log.debug("REST request to get all Tutorials where tutorialSVideo is null");
            return StreamSupport
                .stream(tutorialRepository.findAll().spliterator(), false)
                .filter(tutorial -> tutorial.getTutorialSVideo() == null)
                .collect(Collectors.toList());
        }

        log.debug("REST request to get all Tutorials");
        return tutorialRepository.findAll();
    }

    /**
     * GET  /tutorials/:id -> get the "id" tutorial.
     */
    @RequestMapping(value = "/tutorials/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tutorial> getTutorial(@PathVariable Long id) {
        log.debug("REST request to get Tutorial : {}", id);
        return Optional.ofNullable(tutorialRepository.findOne(id))
            .map(tutorial -> new ResponseEntity<>(
                tutorial,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tutorials/:id -> delete the "id" tutorial.
     */
    @RequestMapping(value = "/tutorials/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTutorial(@PathVariable Long id) {
        log.debug("REST request to delete Tutorial : {}", id);
        tutorialRepository.delete(id);
        tutorialSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tutorial", id.toString())).build();
    }

    /**
     * SEARCH  /_search/tutorials/:query -> search for the tutorial corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/tutorials/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tutorial> searchTutorials(@PathVariable String query) {
        return StreamSupport
            .stream(tutorialSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
