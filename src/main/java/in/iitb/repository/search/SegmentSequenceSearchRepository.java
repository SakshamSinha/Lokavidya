package in.iitb.repository.search;

import in.iitb.domain.SegmentSequence;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SegmentSequence entity.
 */
public interface SegmentSequenceSearchRepository extends ElasticsearchRepository<SegmentSequence, Long> {
}
