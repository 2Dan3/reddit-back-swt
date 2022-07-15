package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.repository.PostRepository;
import rs.ftn.RedditCopyCat.service.PostService;

public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public Post findById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }
}
