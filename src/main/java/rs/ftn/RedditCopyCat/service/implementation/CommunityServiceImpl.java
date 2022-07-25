package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.entity.User;
import rs.ftn.RedditCopyCat.repository.CommunityRepository;
import rs.ftn.RedditCopyCat.service.CommunityService;
import rs.ftn.RedditCopyCat.service.PostService;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private PostService postService;

    @Override
    public Community findById(Long id) {
        return this.communityRepository.findById(id).orElse(null);
    }

    @Override
    public List<Community> findAll() {
        return this.communityRepository.findAll();
    }

    @Override
    public Community save(Community community) {
        return communityRepository.save(community);
    }

    @Override
    public Community findByName(String name) {
        Optional<Community> community = communityRepository.findFirstByName(name);
        if (!community.isEmpty()) {
            return community.get();
        }
        return null;
    }

    @Transactional
    @Override
    public void remove(Community c) {
        postService.deleteAllPostsFromCommunity(c);
        communityRepository.delete(c);
    }

    @Override
    public Community findOneWithPosts(Long communityId) {
        return communityRepository.findOneWithPosts(communityId);
    }

    @Override
    public Community findOneWithFlairs(Long communityId) {
        return communityRepository.findOneWithFlairs(communityId);
    }

}
