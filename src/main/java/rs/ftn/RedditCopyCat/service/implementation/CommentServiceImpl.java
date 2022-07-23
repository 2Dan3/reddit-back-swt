package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.repository.CommentRepository;
import rs.ftn.RedditCopyCat.service.CommentService;
import rs.ftn.RedditCopyCat.service.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

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
    public List<Comment> findAllForPost(Long postId, String criteria, String sortDirection) {
        criteria = criteria.toLowerCase();
        sortDirection = sortDirection.toLowerCase();

        if (criteria.equals("timestamp") && (sortDirection.equals("desc") || sortDirection.equals("asc")) )
            return commentRepository.findAllForPost(postId, criteria, sortDirection);
        else if (criteria.equals("reactions"))
            return commentRepository.findAllSortedByReactions();
        else
            return null;
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
        return commentRepository.findRepliesTo(parentId, criteria, sortDirection);
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
        madeComment.setBelongsToUser(userService.findByUsername(principal.getName()));

        return commentService.save(madeComment);
    }

    @Override
    public void deleteAllForPost(Post targetedPost) {
//        *TODO: *Delete all for post and call deletion of Reports & Reactions to this Comment
    }

}
