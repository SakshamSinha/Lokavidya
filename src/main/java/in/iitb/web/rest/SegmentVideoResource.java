package in.iitb.web.rest;

import com.codahale.metrics.annotation.Timed;
import in.iitb.domain.SegmentVideo;
import in.iitb.repository.SegmentVideoRepository;
import in.iitb.repository.search.SegmentVideoSearchRepository;
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
 * REST controller for managing SegmentVideo.
 */
@RestController
@RequestMapping("/api")
public class SegmentVideoResource {

    private final Logger log = LoggerFactory.getLogger(SegmentVideoResource.class);

    @Inject
    private SegmentVideoRepository segmentVideoRepository;

    @Inject
    private SegmentVideoSearchRepository segmentVideoSearchRepository;

    /**
     * POST  /segmentVideos -> Create a new segmentVideo.
     */
    @RequestMapping(value = "/segmentVideos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SegmentVideo> createSegmentVideo(@Valid @RequestBody SegmentVideo segmentVideo) throws URISyntaxException {
        log.debug("REST request to save SegmentVideo : {}", segmentVideo);
        if (segmentVideo.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new segmentVideo cannot already have an ID").body(null);
        }
        SegmentVideo result = segmentVideoRepository.save(segmentVideo);
        segmentVideoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/segmentVideos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("segmentVideo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /segmentVideos -> Updates an existing segmentVideo.
     */
    @RequestMapping(value = "/segmentVideos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SegmentVideo> updateSegmentVideo(@Valid @RequestBody SegmentVideo segmentVideo) throws URISyntaxException {
        log.debug("REST request to update SegmentVideo : {}", segmentVideo);
        if (segmentVideo.getId() == null) {
            return createSegmentVideo(segmentVideo);
        }
        SegmentVideo result = segmentVideoRepository.save(segmentVideo);
        segmentVideoSearchRepository.save(segmentVideo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("segmentVideo", segmentVideo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /segmentVideos -> get all the segmentVideos.
     */
    @RequestMapping(value = "/segmentVideos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SegmentVideo> getAllSegmentVideos(@RequestParam(required = false) String filter) {
        if ("segmentvideossequence-is-null".equals(filter)) {
            log.debug("REST request to get all SegmentVideos where segmentVideoSSequence is null");
            return StreamSupport
                .stream(segmentVideoRepository.findAll().spliterator(), false)
                .filter(segmentVideo -> segmentVideo.getSegmentVideoSSequence() == null)
                .collect(Collectors.toList());
        }

        log.debug("REST request to get all SegmentVideos");
        return segmentVideoRepository.findAll();
    }

    /**
     * GET  /segmentVideos/:id -> get the "id" segmentVideo.
     */
    @RequestMapping(value = "/segmentVideos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SegmentVideo> getSegmentVideo(@PathVariable Long id) {
        log.debug("REST request to get SegmentVideo : {}", id);
        return Optional.ofNullable(segmentVideoRepository.findOne(id))
            .map(segmentVideo -> new ResponseEntity<>(
                segmentVideo,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /segmentVideos/:id -> delete the "id" segmentVideo.
     */
    @RequestMapping(value = "/segmentVideos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSegmentVideo(@PathVariable Long id) {
        log.debug("REST request to delete SegmentVideo : {}", id);
        segmentVideoRepository.delete(id);
        segmentVideoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("segmentVideo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/segmentVideos/:query -> search for the segmentVideo corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/segmentVideos/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SegmentVideo> searchSegmentVideos(@PathVariable String query) {
        return StreamSupport
            .stream(segmentVideoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
