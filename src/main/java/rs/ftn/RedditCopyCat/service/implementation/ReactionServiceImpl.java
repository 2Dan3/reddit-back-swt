package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.repository.ReactionRepository;
import rs.ftn.RedditCopyCat.service.ReactionService;

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
}
