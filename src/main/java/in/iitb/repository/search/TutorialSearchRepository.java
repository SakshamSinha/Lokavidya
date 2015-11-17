package in.iitb.repository.search;

import in.iitb.domain.Tutorial;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Tutorial entity.
 */
public interface TutorialSearchRepository extends ElasticsearchRepository<Tutorial, Long> {
}
