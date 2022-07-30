package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ftn.RedditCopyCat.model.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(nativeQuery = true, value =
            "select p.* " +
            "from post p " +
            "where p.community_id = :communityId " +
            "order by p.creation_date " +
            ":sortDir")
    List<Post> findPostsSortedByDate(@Param("communityId") Long communityId,
                                     @Param("sortDir") String sortDir);

    //    TODO: Return type boolean ili Integer?
    @Query(nativeQuery = true, value =
        "SELECT COUNT(p.post_id) FROM post p " +
        "WHERE p.post_id = :postId and " +
            "p.posted_by_user_user_id = :userId")
    int countAuthor(@Param("postId") Long postId,
                      @Param("userId") Long userId);

    @Query(nativeQuery = true, value =
            "select p.* " +
            "from post p " +
            "where p.community_id = :communityId " +
            "order by " +
                "(select count(r.reaction_id) " +
                "from reaction r " +
                "where " +
                    "r.to_post_post_id = p.post_id " +
                    "and r.type = ':criteria') " +
                ":sortDir")
    List<Post> findPostsSortedByPopularity(@Param("communityId") Long communityId,
                                           @Param("sortDir") String sortDir,
                                           @Param("criteria") String criteria);


    @Query(nativeQuery = true, value =
            "select p.* " +
            "from post p " +
            "where p.community_id = :communityId " +
            "order by (( " +
                "select count(r.reaction_id) " +
                "from reaction r " +
                "where " +
                    "r.to_post_post_id = p.post_id) *0.4) + " +
                    "(p.creation_date *0.6) ) " +
                ":sortDir")
    List<Post> findPostsSortedByTrending(@Param("communityId") Long communityId,
                                         @Param("sortDir") String sortDir);

    @Query(nativeQuery = true, value =
        "select p.* " +
        "from post p, reaction r " +
        "where " +
            "r.reaction_id = :reactionId and " +
            "p.post_id = r.to_post_post_id")
    Post findByReactionId(@Param("reactionId") Long reactionId);

    @Query(nativeQuery = true, value =
        "select p.* " +
        "from post p, comment c " +
        "where " +
            "p.post_id = c.belongs_to_post_post_id and " +
            "c.comment_id = :commentId")
    Post findByCommentId(@Param("commentId") Long commentId);
}
