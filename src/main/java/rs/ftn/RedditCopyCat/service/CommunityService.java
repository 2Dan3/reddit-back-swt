package rs.ftn.RedditCopyCat.service;

import rs.ftn.RedditCopyCat.model.entity.Community;

import java.util.List;

public interface CommunityService {
    Community findById(Long communityId);

    List<Community> findAll();

    Community save(Community community);

    Community findByName(String name);

    void remove(Community community);

    Community findOneWithPosts(Long communityId);

    Community findOneWithFlairs(Long communityId);
}
