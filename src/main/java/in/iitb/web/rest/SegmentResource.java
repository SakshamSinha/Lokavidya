package in.iitb.web.rest;

import com.codahale.metrics.annotation.Timed;
import in.iitb.domain.Segment;
import in.iitb.repository.SegmentRepository;
import in.iitb.repository.search.SegmentSearchRepository;
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
 * REST controller for managing Segment.
 */
@RestController
@RequestMapping("/api")
public class SegmentResource {

    private final Logger log = LoggerFactory.getLogger(SegmentResource.class);

    @Inject
    private SegmentRepository segmentRepository;

    @Inject
    private SegmentSearchRepository segmentSearchRepository;

    /**
     * POST  /segments -> Create a new segment.
     */
    @RequestMapping(value = "/segments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Segment> createSegment(@Valid @RequestBody Segment segment) throws URISyntaxException {
        log.debug("REST request to save Segment : {}", segment);
        if (segment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new segment cannot already have an ID").body(null);
        }
        Segment result = segmentRepository.save(segment);
        segmentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/segments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("segment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /segments -> Updates an existing segment.
     */
    @RequestMapping(value = "/segments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Segment> updateSegment(@Valid @RequestBody Segment segment) throws URISyntaxException {
        log.debug("REST request to update Segment : {}", segment);
        if (segment.getId() == null) {
            return createSegment(segment);
        }
        Segment result = segmentRepository.save(segment);
        segmentSearchRepository.save(segment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("segment", segment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /segments -> get all the segments.
     */
    @RequestMapping(value = "/segments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Segment> getAllSegments(@RequestParam(required = false) String filter) {
        if ("segmentvideo-is-null".equals(filter)) {
            log.debug("REST request to get all Segments where segmentVideo is null");
            return StreamSupport
                .stream(segmentRepository.findAll().spliterator(), false)
                .filter(segment -> segment.getSegmentVideo() == null)
                .collect(Collectors.toList());
        }

        if ("segmentimage-is-null".equals(filter)) {
            log.debug("REST request to get all Segments where segmentImage is null");
            return StreamSupport
                .stream(segmentRepository.findAll().spliterator(), false)
                .filter(segment -> segment.getSegmentImage() == null)
                .collect(Collectors.toList());
        }

        if ("segmentaudio-is-null".equals(filter)) {
            log.debug("REST request to get all Segments where segmentAudio is null");
            return StreamSupport
                .stream(segmentRepository.findAll().spliterator(), false)
                .filter(segment -> segment.getSegmentAudio() == null)
                .collect(Collectors.toList());
        }

        log.debug("REST request to get all Segments");
        return segmentRepository.findAll();
    }

    /**
     * GET  /segments/:id -> get the "id" segment.
     */
    @RequestMapping(value = "/segments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Segment> getSegment(@PathVariable Long id) {
        log.debug("REST request to get Segment : {}", id);
        return Optional.ofNullable(segmentRepository.findOne(id))
            .map(segment -> new ResponseEntity<>(
                segment,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /segments/:id -> delete the "id" segment.
     */
    @RequestMapping(value = "/segments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSegment(@PathVariable Long id) {
        log.debug("REST request to delete Segment : {}", id);
        segmentRepository.delete(id);
        segmentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("segment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/segments/:query -> search for the segment corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/segments/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Segment> searchSegments(@PathVariable String query) {
        return StreamSupport
            .stream(segmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
