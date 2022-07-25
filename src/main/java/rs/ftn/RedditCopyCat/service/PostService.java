package rs.ftn.RedditCopyCat.service;

import rs.ftn.RedditCopyCat.model.entity.Post;

import java.util.List;

public interface PostService {

    Post findById(Long postId);

    Post save(Post containingPost);

    void delete(Post targetedPost);

    boolean areSortParamsValid(String criteria, String sortDirection);

    List<Post> findAllFromCommunitySortedBy(Long communityId, String criteria, String sortDirection);
}
