package in.iitb.repository.search;

import in.iitb.domain.Audio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Audio entity.
 */
public interface AudioSearchRepository extends ElasticsearchRepository<Audio, Long> {
}
