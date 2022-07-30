package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ftn.RedditCopyCat.model.entity.Rule;

import java.util.Set;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

    //    TODO: Query -test run it
//    @Query(nativeQuery = true, value = "select * from rule where belongs_to_community_community_id = :communityId")
    @Query("select r from Rule r where r.belongsToCommunity =?1")
    Set<Rule> findByCommunityId(Long communityId);
}
