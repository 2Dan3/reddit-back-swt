package rs.ftn.RedditCopyCat.service;

import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.model.entity.Reaction;
import rs.ftn.RedditCopyCat.model.entity.User;

import java.util.Set;

public interface ReactionService {
    void deleteAllForCommentsToPost(Post targetedPost);

    void deleteAllForPost(Post targetedPost);

    Set<Reaction> findAllForPost(Post targetedPost);

    Set<Reaction> findAllForComment(Comment targetedComment);

    Integer getTotalKarmaPoints(User user);

    Reaction findForPostByUser(Long postId, Long userId);

    Reaction findForCommentByUser(Long commentId, Long userId);

    Reaction findById(Long id);

    Reaction save(Reaction reaction);
}
