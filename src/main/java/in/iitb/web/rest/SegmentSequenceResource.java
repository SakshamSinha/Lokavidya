package in.iitb.web.rest;

import com.codahale.metrics.annotation.Timed;
import in.iitb.domain.SegmentSequence;
import in.iitb.repository.SegmentSequenceRepository;
import in.iitb.repository.search.SegmentSequenceSearchRepository;
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
 * REST controller for managing SegmentSequence.
 */
@RestController
@RequestMapping("/api")
public class SegmentSequenceResource {

    private final Logger log = LoggerFactory.getLogger(SegmentSequenceResource.class);

    @Inject
    private SegmentSequenceRepository segmentSequenceRepository;

    @Inject
    private SegmentSequenceSearchRepository segmentSequenceSearchRepository;

    /**
     * POST  /segmentSequences -> Create a new segmentSequence.
     */
    @RequestMapping(value = "/segmentSequences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SegmentSequence> createSegmentSequence(@Valid @RequestBody SegmentSequence segmentSequence) throws URISyntaxException {
        log.debug("REST request to save SegmentSequence : {}", segmentSequence);
        if (segmentSequence.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new segmentSequence cannot already have an ID").body(null);
        }
        SegmentSequence result = segmentSequenceRepository.save(segmentSequence);
        segmentSequenceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/segmentSequences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("segmentSequence", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /segmentSequences -> Updates an existing segmentSequence.
     */
    @RequestMapping(value = "/segmentSequences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SegmentSequence> updateSegmentSequence(@Valid @RequestBody SegmentSequence segmentSequence) throws URISyntaxException {
        log.debug("REST request to update SegmentSequence : {}", segmentSequence);
        if (segmentSequence.getId() == null) {
            return createSegmentSequence(segmentSequence);
        }
        SegmentSequence result = segmentSequenceRepository.save(segmentSequence);
        segmentSequenceSearchRepository.save(segmentSequence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("segmentSequence", segmentSequence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /segmentSequences -> get all the segmentSequences.
     */
    @RequestMapping(value = "/segmentSequences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SegmentSequence> getAllSegmentSequences() {
        log.debug("REST request to get all SegmentSequences");
        return segmentSequenceRepository.findAll();
    }

    /**
     * GET  /segmentSequences/:id -> get the "id" segmentSequence.
     */
    @RequestMapping(value = "/segmentSequences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SegmentSequence> getSegmentSequence(@PathVariable Long id) {
        log.debug("REST request to get SegmentSequence : {}", id);
        return Optional.ofNullable(segmentSequenceRepository.findOne(id))
            .map(segmentSequence -> new ResponseEntity<>(
                segmentSequence,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /segmentSequences/:id -> delete the "id" segmentSequence.
     */
    @RequestMapping(value = "/segmentSequences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSegmentSequence(@PathVariable Long id) {
        log.debug("REST request to delete SegmentSequence : {}", id);
        segmentSequenceRepository.delete(id);
        segmentSequenceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("segmentSequence", id.toString())).build();
    }

    /**
     * SEARCH  /_search/segmentSequences/:query -> search for the segmentSequence corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/segmentSequences/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SegmentSequence> searchSegmentSequences(@PathVariable String query) {
        return StreamSupport
            .stream(segmentSequenceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
