package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ftn.RedditCopyCat.model.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(nativeQuery = true, value =
            "select c.* from comment c " +
            "where c.belongs_to_post_post_id = :postId " +
            "and c.parent_comment_comment_id is null " +
            "and c.is_deleted = 0 " +
            "order by :criteria :sortDirection")
    List<Comment> findAllForPost(@Param("postId") Long postId, @Param("criteria") String criteria, @Param("sortDirection") String sortDirection);

    @Query(nativeQuery = true, value =
            "select r.* from comment r " +
            "where r.parent_comment_comment_id = :parentId " +
            "and r.is_deleted = 0 " +
            "order by :criteria :sortDirection")
    List<Comment> findRepliesTo(@Param("parentId") Long parentId, @Param("criteria") String criteria, @Param("sortDirection") String sortDirection);

    @Query(nativeQuery = true, value =
            "select c.* " +
            "from comment c " +
            "where c.belongs_to_post_post_id = :postId " +
            "and c.parent_comment_comment_id is null " +

            "order by (select count(r.reaction_id) from reaction r " +
            "where r.to_comment_comment_id = c.comment_id and r.type = ':criteria') :sortDirection")
    List<Comment> findAllSortedByReactions(@Param("postId") Long postId,
                                           @Param("criteria") String criteria,
                                           @Param("sortDirection") String sortDirection);

    @Query(nativeQuery = true, value =
            "select c.* " +
            "from comment c " +
            "where c.parent_comment_comment_id = :parentId " +
            "and c.is_deleted = 0 " +
            "order by (select count(r.reaction_id) from reaction r " +
            "where r.to_comment_comment_id = c.comment_id and r.type = ':criteria') :sortDirection")
    List<Comment> findAllRepliesSortedByReactions(@Param("parentId") Long parentCommentId,
                                                  @Param("criteria") String criteria,
                                                  @Param("sortDirection") String sortDirection);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
    "delete from comment " +
            "where belongs_to_post_post_id = :postId")
    void deleteAllForPost(@Param("postId") Long postId);

    @Query(nativeQuery = true, value =
        "select count(c.comment_id) " +
        "from comment c " +
        "where c.comment_id = :commentId and " +
                "c.belongs_to_user_user_id = :userId")
    int countAuthor(@Param("commentId") Long commentId,@Param("userId") Long userId);
}
