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


    @Query(nativeQuery = true, value =
        "select sum(u) - sum(d) " +
                "from " +
                "(select sum(r.type='UPVOTE') as u,  sum(r.type='DOWNVOTE') as d " +
                "from post p, reaction r " +
                "where p.user_id = :userId and " +
                "r.to_post_post_id = p.post_id " +
                "union " +
                "select sum(r.type='UPVOTE') as u, sum(r.type='DOWNVOTE') as d " +
                "from comment c, reaction r " +
                "where c.belongs_to_user_user_id = :userId and " +
                "r.to_comment_comment_id = c.comment_id)")
    Integer getTotalKarmaForUserId(@Param("userId") Long userId);

//    TODO: AN ALTERNATIVE VERSION - TEST the main 1st:
//    @Query(nativeQuery = true, value =
//        "select sum(r.type='UPVOTE') - sum(r.type='DOWNVOTE') " +
//                "from post p, reaction r " +
//                "where p.user_id = :userId and " +
//                "r.to_post_post_id = p.post_id " +
//                "union " +
//                "select sum(r.type='UPVOTE') - sum(r.type='DOWNVOTE') " +
//                "from comment c, reaction r " +
//                "where c.belongs_to_user_user_id = :userId and " +
//                "r.to_comment_comment_id = c.comment_id;")
//    Integer getTotalKarmaForUserId(@Param("userId") Long userId);


    @Query(nativeQuery = true, value =
        "select * from reaction" +
                "where made_by_user_id = :userId and " +
                    "to_post_post_id = :postId")
    Reaction findForPostByUser(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query(nativeQuery = true, value =
            "select * from reaction " +
                    "where made_by_user_id = :userId and " +
                    "to_comment_comment_id = :commentId")
    Reaction findForCommentByUser(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Query(nativeQuery = true, value =
        "SELECT COUNT(r.reaction_id) FROM post r " +
                "WHERE r.reaction_id = :reactionId and " +
                "r.made_by_user_id = :userId")
    Integer countAuthor(@Param("reactionId") Long reactionId,@Param("userId") Long userId);

    @Query(nativeQuery = true, value =
        "select count(r.reaction_id) " +
        "from reaction r " +
        "where " +
            "r.to_post_post_id = :postId and " +
            "r.made_by_user_id = :userId")
    Integer countForPost(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query(nativeQuery = true, value =
            "select count(r.reaction_id) " +
                    "from reaction r " +
                    "where " +
                    "r.to_comment_comment_id = :commentId and " +
                    "r.made_by_user_id = :userId")
    Integer countForComment(@Param("commentId") Long commentId, @Param("userId") Long userId);
}
