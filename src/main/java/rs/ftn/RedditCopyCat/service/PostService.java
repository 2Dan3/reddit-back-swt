package rs.ftn.RedditCopyCat.service;

import rs.ftn.RedditCopyCat.model.entity.Post;

public interface PostService {

    Post findById(Long postId);

    Post save(Post containingPost);
}
