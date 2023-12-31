package rs.ftn.RedditCopyCat.service;

import org.springframework.data.jpa.repository.Query;
import rs.ftn.RedditCopyCat.model.entity.Rule;

import java.util.Set;

public interface RulesService {

    Set<Rule> findByCommunityId(Long communityId);

    Rule save(Rule createdRule);

    Rule findById(Long ruleId);

    void remove(Rule wantedRule);
}
