package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.repository.PostRepository;
import rs.ftn.RedditCopyCat.service.CommentService;
import rs.ftn.RedditCopyCat.service.PostService;

import java.util.List;

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

    @Override
    public boolean areSortParamsValid(String criteria, String sortDirection) {
        criteria = criteria.toLowerCase();
        sortDirection = sortDirection.toLowerCase();
        return (sortDirection.equals("desc") || sortDirection.equals("asc"))
                && (criteria.equals("creation_date") || criteria.equals("upvote")
                || criteria.equals("downvote") || criteria.equals("trending"));
    }

    @Override
    public List<Post> findAllFromCommunitySortedBy(Long communityId, String criteria, String sortDirection) {

        if (criteria.equalsIgnoreCase("creation_date")) {
            return postRepository.findPostsSortedByDate(communityId, sortDirection);
        }else if (criteria.equalsIgnoreCase("trending")) {
            return postRepository.findPostsSortedByTrending(communityId, sortDirection);
        }else
            // in this case: criteria equals downvote || upvote
            return postRepository.findPostsSortedByPopularity(communityId, sortDirection, criteria.toUpperCase());
    }


}
