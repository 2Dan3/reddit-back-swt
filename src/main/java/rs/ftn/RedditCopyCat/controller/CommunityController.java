package rs.ftn.RedditCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ftn.RedditCopyCat.model.DTO.*;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.entity.Flair;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.model.entity.Rule;
import rs.ftn.RedditCopyCat.service.CommunityService;
import rs.ftn.RedditCopyCat.service.FlairService;
import rs.ftn.RedditCopyCat.service.PostService;
import rs.ftn.RedditCopyCat.service.RulesService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "reddit/communities")
public class CommunityController {

    @Autowired
    private CommunityService communityService;
    @Autowired
    private RulesService rulesService;
    @Autowired
    private PostService postService;
    @Autowired
    private FlairService flairService;
//    @Autowired
//    private Principal principal;

    @GetMapping()
    public ResponseEntity<List<CommunityDTO>> getAllCommunities() {

        List<Community> communities = communityService.findAll();

        // convert communities to DTOs
        List<CommunityDTO> communitiesDTO = new ArrayList<>();
        for (Community c : communities) {
            communitiesDTO.add(new CommunityDTO(c));
        }

        return new ResponseEntity<>(communitiesDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommunityDTO> getCommunity(@PathVariable Long id) {

        Community community = communityService.findById(id);

        // comm must exist
        if (community == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new CommunityDTO(community), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<CommunityDTO> createCommunity(@RequestBody CommunityDTO communityDTO) {

        Community community = new Community();
        community.setName(communityDTO.getName());
        community.setDescription(communityDTO.getDescription());
        community.setCreationDate(LocalDate.now());

        community = communityService.save(community);
        return new ResponseEntity<>(new CommunityDTO(community), HttpStatus.CREATED);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<CommunityDTO> updateCommunity(@RequestBody CommunityDTO communityDTO) {

        // community must exist
//        TODO  findByName mozda bolje ako ne zelim ID u DTO da se mapira sa JSON ?
        Community community = communityService.findByName(communityDTO.getName());

        if (community == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        community.setName(communityDTO.getName());
        community.setDescription(communityDTO.getDescription());

        community = communityService.save(community);
        return new ResponseEntity<>(new CommunityDTO(community), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id) {

        Community community = communityService.findById(id);

        if (community != null) {
            communityService.remove(community);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //TODO *  !  !  !  *
    @GetMapping(value = "/{communityId}/posts")
    public ResponseEntity<List<PostDTO>> getCommunityPosts(@PathVariable Long communityId) {

        Community community = communityService.findOneWithPosts(communityId);

        //ako je podesen fetchType LAZY i pozovemo findOne umesto findOneWithPosts,
        //na poziv getPosts bismo dobili LazyInitializationException
        Set<Post> posts = community.getPosts();
        List<PostDTO> postsDTO = new ArrayList<>();

        for (Post p : posts) {
            PostDTO postDTO = new PostDTO(p);
            postsDTO.add(postDTO);
        }
        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{communityId}/posts/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long communityId, @PathVariable Long postId) {

        Community community = communityService.findById(communityId);
        if (community == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Post post = postService.findById(postId);
        if (post == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new PostDTO(post), HttpStatus.OK);
    }

    // TODO: how to getLoggedUser from ReceivedJWToken
    @PostMapping(value = "/{communityId}/posts")                                        /*@Validated*/
    public ResponseEntity<Void> createPost(Authentication authentication, @PathVariable Long communityId, @RequestBody PostDTO postSent) {

        Community community = communityService.findOneWithPosts(communityId);
        if (community == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Post newPost = new Post();
        newPost.setTitle(postSent.getTitle());
        newPost.setText(postSent.getText());
        newPost.setImagePath(postSent.getImagePath());
        newPost.setCreationDate(LocalDate.now());
        newPost.setCommunity(community);
        newPost.setFlair(flairService.findByName(postSent.getFlairName()));
//TODO:        newPost.setPostedByUser();

        community.getPosts().add(newPost);
        communityService.save(community);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{communityId}/posts/{postId}")                               /*@Validated*/
    public ResponseEntity<Void> updatePost(@PathVariable Long communityId, @RequestBody PostDTO postSent, @PathVariable Long postId) {

        Community community = communityService.findOneWithPosts(communityId);
        if (community == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            OR HttpStatus.NOT_FOUND
        }
        Post targetedPost = postService.findById(postId);
        if (targetedPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        targetedPost.setTitle(postSent.getTitle());
        targetedPost.setText(postSent.getText());
        targetedPost.setImagePath(postSent.getImagePath());
        // TODO: treba li i promena Flair-a?
//        targetedPost.setFlair(flairService.findByName(postSent.getFlairName()));

        communityService.save(community);
//      postService.save(targetedPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO *  !  !  !  *
    @GetMapping(value = "/{communityId}/flairs")
    public ResponseEntity<List<FlairDTO>> getCommunityFlairs(@PathVariable Long communityId) {

        // JOIN FETCH query
        Community community = communityService.findOneWithFlairs(communityId);

        //ako je podesen fetchType LAZY i pozovemo findOne umesto findOneWithFlairs,
        //na poziv getFlairs bismo dobili LazyInitializationException
        Set<Flair> flairs = community.getFlairs();
        List<FlairDTO> flairsDTO = new ArrayList<>();
        for (Flair f : flairs) {
            FlairDTO flairDTO = new FlairDTO(f);
            flairsDTO.add(flairDTO);
        }
        return new ResponseEntity<>(flairsDTO, HttpStatus.OK);
    }
    //TODO *  !  !  !  *
    @GetMapping(value = "/{communityId}/rules")
    public ResponseEntity<List<RuleDTO>> getCommunityRules(@PathVariable Long communityId) {

        // JOIN FETCH query
        Set<Rule> rules = rulesService.findByCommunityId(communityId);

        List<RuleDTO> rulesDTO = new ArrayList<>();
        for (Rule r : rules) {
            RuleDTO ruleDTO = new RuleDTO(r);
            rulesDTO.add(ruleDTO);
        }
        return new ResponseEntity<>(rulesDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{communityId}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> banCommunity(@RequestBody CommunityDTO communityDTO) {

        // community must exist
//        TODO  findByName mozda bolje ako ne zelim ID u DTO da se mapira sa JSON ?
        Community community = communityService.findByName(communityDTO.getName());

        if (community == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        community.setIsSuspended(true);
        community.setSuspensionReason(communityDTO.getSuspensionReason());

        community = communityService.save(community);
        return new ResponseEntity(HttpStatus.OK);
    }

}
