package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.repository.CommentRepository;
import rs.ftn.RedditCopyCat.service.CommentService;
import rs.ftn.RedditCopyCat.service.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    Principal principal;

    @Override
    public Set<Comment> findAllForPost(Long postId) {
        return commentRepository.findAllForPost(postId);
    }

    @Override
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public Comment save(Comment madeComment) {
        return commentRepository.save(madeComment);
    }

    @Override
    public Set<Comment> findRepliesTo(Long parentId) {
        return commentRepository.findRepliesTo(parentId);
    }

    public Comment attachComment(Post targetedPost, Long parentId, String commentText) {
        Comment madeComment = new Comment();
        madeComment.setParentComment(null);

        if (parentId != null) {
            Comment parentComment = findById(parentId);
            if (parentComment != null && !parentComment.getIsDeleted())
                madeComment.setParentComment(parentComment);
        }

        madeComment.setIsDeleted(false);
        madeComment.setTimestamp(LocalDate.now());
        madeComment.setText(commentText);
        madeComment.setBelongsToPost(targetedPost);
        madeComment.setBelongsToUser(userService.findByUsername(principal.getName()));

        return commentService.save(madeComment);
    }

}
