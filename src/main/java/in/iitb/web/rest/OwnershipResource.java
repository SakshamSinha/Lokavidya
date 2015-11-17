package in.iitb.web.rest;

import com.codahale.metrics.annotation.Timed;
import in.iitb.domain.Ownership;
import in.iitb.repository.OwnershipRepository;
import in.iitb.repository.search.OwnershipSearchRepository;
import in.iitb.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Ownership.
 */
@RestController
@RequestMapping("/api")
public class OwnershipResource {

    private final Logger log = LoggerFactory.getLogger(OwnershipResource.class);

    @Inject
    private OwnershipRepository ownershipRepository;

    @Inject
    private OwnershipSearchRepository ownershipSearchRepository;

    /**
     * POST  /ownerships -> Create a new ownership.
     */
    @RequestMapping(value = "/ownerships",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ownership> createOwnership(@RequestBody Ownership ownership) throws URISyntaxException {
        log.debug("REST request to save Ownership : {}", ownership);
        if (ownership.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new ownership cannot already have an ID").body(null);
        }
        Ownership result = ownershipRepository.save(ownership);
        ownershipSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ownerships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ownership", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ownerships -> Updates an existing ownership.
     */
    @RequestMapping(value = "/ownerships",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ownership> updateOwnership(@RequestBody Ownership ownership) throws URISyntaxException {
        log.debug("REST request to update Ownership : {}", ownership);
        if (ownership.getId() == null) {
            return createOwnership(ownership);
        }
        Ownership result = ownershipRepository.save(ownership);
        ownershipSearchRepository.save(ownership);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ownership", ownership.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ownerships -> get all the ownerships.
     */
    @RequestMapping(value = "/ownerships",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ownership> getAllOwnerships() {
        log.debug("REST request to get all Ownerships");
        return ownershipRepository.findAll();
    }

    /**
     * GET  /ownerships/:id -> get the "id" ownership.
     */
    @RequestMapping(value = "/ownerships/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ownership> getOwnership(@PathVariable Long id) {
        log.debug("REST request to get Ownership : {}", id);
        return Optional.ofNullable(ownershipRepository.findOne(id))
            .map(ownership -> new ResponseEntity<>(
                ownership,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ownerships/:id -> delete the "id" ownership.
     */
    @RequestMapping(value = "/ownerships/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOwnership(@PathVariable Long id) {
        log.debug("REST request to delete Ownership : {}", id);
        ownershipRepository.delete(id);
        ownershipSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ownership", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ownerships/:query -> search for the ownership corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/ownerships/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ownership> searchOwnerships(@PathVariable String query) {
        return StreamSupport
            .stream(ownershipSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
