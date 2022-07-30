package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.repository.CommentRepository;
import rs.ftn.RedditCopyCat.service.CommentService;
import rs.ftn.RedditCopyCat.service.ReactionService;
import rs.ftn.RedditCopyCat.service.ReportService;
import rs.ftn.RedditCopyCat.service.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ReportService reportService;
    @Autowired
    ReactionService reactionService;
    @Autowired
    UserService userService;
    @Autowired
    Principal principal;

    @Override
    public List<Comment> findAllForPost(Long postId, String criteria, String sortDirection) {
        criteria = criteria.toLowerCase();
        sortDirection = sortDirection.toLowerCase();

        if (criteria.equals("timestamp"))
            return commentRepository.findAllForPost(postId, criteria, sortDirection);
        else {
            criteria = criteria.toUpperCase();
            return commentRepository.findAllSortedByReactions(postId, criteria, sortDirection);
        }
    }

    @Override
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public List<Comment> findAllNewestFirst() {
        return commentRepository.findAll(Sort.by("timestamp").ascending());
    }

    @Override
    public List<Comment> findAllOldestFirst() {
        return commentRepository.findAll(Sort.by("timestamp").descending());
    }

    @Override
    public Comment save(Comment madeComment) {
        return commentRepository.save(madeComment);
    }

    @Override
    public List<Comment> findRepliesTo(Long parentId, String criteria, String sortDirection) {
        criteria = criteria.toLowerCase();
        sortDirection = sortDirection.toLowerCase();

        if (criteria.equals("timestamp"))
            return commentRepository.findRepliesTo(parentId, criteria, sortDirection);
        else {
            criteria = criteria.toUpperCase();
            return commentRepository.findAllRepliesSortedByReactions(parentId, criteria, sortDirection);
        }
    }

    public Comment attachComment(Post targetedPost, Long parentId, String commentText) {
        Comment madeComment = new Comment();
        madeComment.setParentComment(null);

        if (parentId != null) {
            Comment parentComment = findById(parentId);
            if (parentComment != null && !parentComment.isDeleted())
                madeComment.setParentComment(parentComment);
        }

        madeComment.setDeleted(false);
        madeComment.setTimestamp(LocalDate.now());
        madeComment.setText(commentText);
        madeComment.setBelongsToPost(targetedPost);
//        TODO*: fix setBelongToUser argument, findByUsername from JWT
//         // & call a function to check whether returned user is logged in currently in securityContext
        madeComment.setBelongsToUser(userService.findByUsername(principal.getName()));

        return save(madeComment);
    }

    @Transactional
    @Override
    public void deleteAllForPost(Post targetedPost) {
        reportService.deleteAllForCommentsToPost(targetedPost);
        reactionService.deleteAllForCommentsToPost(targetedPost);
        commentRepository.deleteAllForPost(targetedPost.getId());
    }

    @Override
    public void deleteVisibility(Comment comment) {
        if (comment != null) {
            comment.setDeleted(true);
            save(comment);
        }
    }

    @Override
    public boolean areSortParamsValid(String criteria, String sortDirection) {
        criteria = criteria.toLowerCase();
        sortDirection = sortDirection.toLowerCase();
        return (sortDirection.equals("desc") || sortDirection.equals("asc"))
                && (criteria.equals("timestamp") || criteria.equals("upvote") || criteria.equals("downvote"));
    }

    @Override
    public boolean isAuthor(Long commentId, Long userId) {
        return commentRepository.countAuthor(commentId, userId)==0?false:true;
    }

}
