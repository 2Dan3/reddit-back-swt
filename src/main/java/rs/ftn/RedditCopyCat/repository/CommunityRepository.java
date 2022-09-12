package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ftn.RedditCopyCat.model.entity.Community;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Override
    @Modifying
    @Query("select c from Community c where c.isSuspended = false")
    List<Community> findAll();

    Optional<Community> findFirstByName(String name);

    @Query("select c from Community c left join fetch c.posts p where c.id =?1")
    Community findOneWithPosts(Long communityId);

//    TODO: Query - test run it
    @Query("select c from Community c left join fetch c.flairs f where c.id =?1")
    Community findOneWithFlairs(Long communityId);
}
