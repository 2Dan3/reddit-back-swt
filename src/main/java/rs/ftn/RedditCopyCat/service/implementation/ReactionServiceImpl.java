package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.model.entity.Reaction;
import rs.ftn.RedditCopyCat.model.entity.User;
import rs.ftn.RedditCopyCat.repository.ReactionRepository;
import rs.ftn.RedditCopyCat.service.ReactionService;

import java.util.Set;

@Service
public class ReactionServiceImpl implements ReactionService {
    @Autowired
    ReactionRepository reactionRepository;


    @Override
    public void deleteAllForCommentsToPost(Post targetedPost) {
        reactionRepository.deleteAllForCommentsToPost(targetedPost.getId());
    }

    @Override
    public void deleteAllForPost(Post targetedPost) {
        reactionRepository.deleteAllForPost(targetedPost.getId());
    }

    @Override
    public Set<Reaction> findAllForPost(Post targetedPost) {
        return reactionRepository.findAllForPostId(targetedPost.getId());
    }

    @Override
    public Set<Reaction> findAllForComment(Comment targetedComment) {
        return reactionRepository.findAllForCommentId(targetedComment.getId());
    }

    @Override
    public Integer getTotalKarmaPoints(User user) {
        return reactionRepository.getTotalKarmaForUserId(user.getId());
    }

    @Override
    public Reaction findForPostByUser(Long postId, Long userId) {
        return reactionRepository.findForPostByUser(postId, userId);
    }

    @Override
    public Reaction findForCommentByUser(Long commentId, Long userId) {
        return reactionRepository.findForCommentByUser(commentId, userId);
    }

    @Override
    public Reaction findById(Long id) {
        return reactionRepository.findById(id).orElse(null);
    }

    @Override
    public Reaction save(Reaction reaction) {
        return reactionRepository.save(reaction);
    }
}
