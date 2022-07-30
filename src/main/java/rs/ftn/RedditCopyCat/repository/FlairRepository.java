package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.entity.Flair;

@Repository
public interface FlairRepository extends JpaRepository<Flair, Long> {
    // TODO check
    Flair findByName(String flairName);

    @Query(nativeQuery = true, value =
        "select f.* " +
        "from flair f, communities_flairs cf " +
        "where " +
            "f.name = :flairName and " +
            "f.flair_id = cf.flair_id and " +
            "cf.community_id = :communityId")
    Flair findByNameForCommunityId(String flairName, Long communityId);

    @Query("select f from Flair f join fetch f.communities c where f.name =?1")
    Flair findOneWithCommunities(String flair);
}
