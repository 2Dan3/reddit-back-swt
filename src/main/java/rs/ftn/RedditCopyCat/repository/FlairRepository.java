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

//    *TODO: Refactor flair_communities & community_flairs into one ManyToMany table
    @Query("select f from Flair f where f.flairName = ?1 and f.")
    Flair findByNameForCommunityId(String flairName, Long communityId);

    @Query("select f from Flair f join fetch f.communities c where f.name =?1")
    Flair findOneWithCommunities(String flair);
}
