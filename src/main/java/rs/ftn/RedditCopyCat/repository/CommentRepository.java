package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ftn.RedditCopyCat.model.entity.Comment;

import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.belongsToPost = ?1")
    Set<Comment> findAllForPost(Long postId);

    @Query("select r from Comment r where r.parentComment = ?1")
    Set<Comment> findRepliesTo(Long parentId);
}
