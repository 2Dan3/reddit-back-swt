package rs.ftn.RedditCopyCat.service;

import rs.ftn.RedditCopyCat.model.entity.Post;

public interface ReactionService {
    void deleteAllForCommentsToPost(Post targetedPost);

    void deleteAllForPost(Post targetedPost);
}
