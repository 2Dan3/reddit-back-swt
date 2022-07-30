package rs.ftn.RedditCopyCat.service;

import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Post;

import java.util.List;
import java.util.Set;

public interface CommentService {

    List<Comment> findAllForPost(Long postId, String criteria, String sortDirection);

    Comment findById(Long commentId);

    List<Comment> findAllNewestFirst();

    List<Comment> findAllOldestFirst();

    Comment save(Comment madeComment);

    List<Comment> findRepliesTo(Long parentId, String criteria, String sortDirection);

    Comment attachComment(Post targetedPost, Long parentId, String text);

    void deleteAllForPost(Post targetedPost);

    void deleteVisibility(Comment comment);

    boolean areSortParamsValid(String criteria, String sortDirection);

    boolean isAuthor(Long commentId, Long userId);
}
