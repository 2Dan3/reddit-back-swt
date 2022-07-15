package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ftn.RedditCopyCat.model.entity.Rule;

import java.util.Set;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
//    TODO: Query - implement it --test run it
    @Query("")
    Set<Rule> findByCommunityId(Long communityId);
}
