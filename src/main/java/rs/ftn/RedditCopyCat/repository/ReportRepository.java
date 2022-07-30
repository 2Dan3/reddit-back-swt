package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ftn.RedditCopyCat.model.entity.Report;

import java.util.Set;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
        "delete from report " +
            "where for_comment_comment_id in " +
            "(select comment_id from comment where belongs_to_post_post_id = :postId)")
    void deleteAllForCommentsToPost(@Param("postId") Long postId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
        "delete from report " +
            "where for_post_post_id = :postId")
    void deleteAllForPost(@Param("postId") Long postId);


    //    TODO: check Query
    @Query(nativeQuery = true, value =
        "select r.* " +
        "from report r, post p " +
        "where " +
            "r.for_post_post_id = p.post_id and " +
            "p.community_id = :communityId")
    Set<Report> findForPostsInCommunity(@Param("communityId") Long communityId);

//    TODO: check Query
    @Query(nativeQuery = true, value =
        "select r.* " +
        "from report r, comment c, post p" +
        "where " +
            "r.for_comment_comment_id = c.comment_id and " +
            "c.belongs_to_post_post_id = p.post_id and " +
            "p.community_id = :communityId")
    Set<Report> findForCommentsInCommunity(@Param("communityId") Long communityId);

    @Query(nativeQuery = true, value =
        "SELECT COUNT(r) FROM report r " +
                "WHERE r.report_id = :reportId and " +
                "r.by_user_user_id = :userId")
    Integer countAuthor(@Param("reportId") Long reportId,@Param("userId") Long userId);
}
