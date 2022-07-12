package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ftn.RedditCopyCat.model.entity.User;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByUsername(String username);

    @Query("insert into banned (timestamp, bannedBy, bannedUser, forCommunity) " +
                    "values (:timeStamp, :moderatorId, :userId, :communityId")
    void banUserFromCommunity(
            @Param("communityId") Long communityId,
            @Param("userId") Long userId,
            @Param("moderatorId") Long moderatorId,
            @Param("timeStamp") LocalDate now);

    //    TODO: Return type boolean ili int?
    @Query(nativeQuery = true,
            value = "SELECT COUNT(m) FROM moderator m WHERE m.commu_id = :communityId and m.u_id = :userId")
    int findModerator(@Param("communityId") Long communityId,
                          @Param("userId") Long userId);
}
