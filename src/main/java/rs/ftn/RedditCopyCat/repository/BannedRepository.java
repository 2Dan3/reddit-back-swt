package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ftn.RedditCopyCat.model.entity.Banned;

import java.util.List;

@Repository
public interface BannedRepository extends JpaRepository<Banned, Long> {

    @Query(nativeQuery = true, value =
        "select count(b.ban_id) from banned b " +
        "where " +
            "b.banned_user_user_id = :userId and " +
            "for_community_community_id = :communityId")
    int existsBan(
            @Param("userId") Long userId,
            @Param("communityId") Long communityId );


    @Query(nativeQuery = true, value =
        "select * from banned " +
        "where " +
            "banned_user_user_id = :userId and " +
            "for_community_community_id = :communityId")
    List<Banned> findByCommunityAndUser(Long communityId, Long userId);
}
