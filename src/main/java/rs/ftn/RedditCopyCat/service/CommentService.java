package rs.ftn.RedditCopyCat.service;

import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Post;

import java.util.Set;

public interface CommentService {

    Set<Comment> findAllForPost(Long postId);

    Comment findById(Long commentId);

    Comment save(Comment madeComment);

    Set<Comment> findRepliesTo(Long parentId);

    Comment attachComment(Post targetedPost, Long parentId, String text);
}
