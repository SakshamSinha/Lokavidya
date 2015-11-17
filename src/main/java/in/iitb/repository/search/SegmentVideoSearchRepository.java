package in.iitb.repository.search;

import in.iitb.domain.SegmentVideo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SegmentVideo entity.
 */
public interface SegmentVideoSearchRepository extends ElasticsearchRepository<SegmentVideo, Long> {
}
