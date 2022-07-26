package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ftn.RedditCopyCat.model.entity.Reaction;

import java.util.Set;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
        "delete from reaction " +
        "where to_comment_comment_id in " +
            "(select comment_id from comment where belongs_to_post_post_id = :postId)")
    void deleteAllForCommentsToPost(@Param("postId") Long postId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
        "delete from reaction " +
            "where to_post_post_id = :postId")
    void deleteAllForPost(@Param("postId") Long postId);

    @Query(nativeQuery = true, value =
            "select from reaction " +
                    "where to_post_post_id = :postId")
    Set<Reaction> findAllForPostId(@Param("postId") Long postId);

    @Query(nativeQuery = true, value =
            "select from reaction " +
                    "where to_comment_comment_id = :commentId")
    Set<Reaction> findAllForCommentId(@Param("commentId") Long commentId);


    //    TODO: getTotalKarmaForUserId
    @Query(nativeQuery = true, value =
        "")
    Integer getTotalKarmaForUserId(@Param("userId") Long userId);


    @Query(nativeQuery = true, value =
        "select * from reaction" +
                "where made_by_user_id = :userId and " +
                    "to_post_post_id = :postId")
    Reaction findForPostByUser(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query(nativeQuery = true, value =
            "select * from reaction " +
                    "where made_by_user_id = :userId and " +
                    "to_comment_comment_id = :postId")
    Reaction findForCommentByUser(@Param("commentId") Long commentId, @Param("userId") Long userId);

}
