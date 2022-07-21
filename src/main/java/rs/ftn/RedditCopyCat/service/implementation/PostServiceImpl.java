package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.repository.PostRepository;
import rs.ftn.RedditCopyCat.service.CommentService;
import rs.ftn.RedditCopyCat.service.PostService;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    ReportService reportService;
    @Autowired
    ReactionService reactionService;

    @Override
    public Post findById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    @Override
    public Post save(Post containingPost) {
        return postRepository.save(containingPost);
    }

//    TODO: confirm that @Transactional is functioning properly
    @Transactional
    @Override
    public void delete(Post targetedPost) {
        commentService.deleteAllForPost(targetedPost);
        reportService.deleteAllForPost(targetedPost);
        reactionService.deleteAllForPost(targetedPost);
        postRepository.delete(targetedPost);
    }
}
