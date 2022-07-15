package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.RedditCopyCat.model.entity.Rule;
import rs.ftn.RedditCopyCat.repository.RuleRepository;
import rs.ftn.RedditCopyCat.service.RulesService;

import java.util.Set;

@Service
public class RuleServiceImpl implements RulesService {

    @Autowired
    RuleRepository ruleRepository;

    @Override
    public Set<Rule> findByCommunityId(Long communityId) {
        return ruleRepository.findByCommunityId(communityId);
    }
}
