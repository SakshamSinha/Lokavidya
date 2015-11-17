package in.iitb.repository.search;

import in.iitb.domain.ExtVideo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ExtVideo entity.
 */
public interface ExtVideoSearchRepository extends ElasticsearchRepository<ExtVideo, Long> {
}
