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
import rs.ftn.RedditCopyCat.model.entity.*;
import rs.ftn.RedditCopyCat.model.enums.ReactionType;
import rs.ftn.RedditCopyCat.service.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "${apiPrefix}/communities")
public class CommunityController {

    @Autowired
    private CommunityService communityService;
    @Autowired
    private RulesService rulesService;
    @Autowired
    private PostService postService;
    @Autowired
    private FlairService flairService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReactionService reactionService;

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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommunityDTO> createCommunity(Authentication authentication, @RequestBody @Validated CreateCommunityDTO communityDTO) {

        if (communityService.findByName(communityDTO.getName()) != null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        User creator = userService.findByUsername(authentication.getName() );

        Community community = new Community();
        community.setName(communityDTO.getName());
        community.setDescription(communityDTO.getDescription());
        community.setCreationDate(LocalDate.now());
        community.setSuspended(false);
        community.addModerator(creator);
        community = communityService.save(community);
//        userService.save(creator);
        return new ResponseEntity<>(new CommunityDTO(community), HttpStatus.CREATED);
    }

    @PutMapping(consumes = "application/json")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommunityDTO> updateCommunity(@RequestBody @Validated CommunityDTO communityDTO) {

        // community must exist
        Community community = communityService.findById(communityDTO.getId());

        if (community == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        community.setName(communityDTO.getName());
        community.setDescription(communityDTO.getDescription());

        community = communityService.save(community);
        return new ResponseEntity<>(new CommunityDTO(community), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{communityId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long communityId) {

        // *Important: foreach in postService needs getPosts() to remove all, findById(id) here would
        // throw LazyInitializationException later on
        Community community = communityService.findOneWithPosts(communityId);

        if (community != null) {
            communityService.remove(community);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{communityId}/posts")
    public ResponseEntity<List<PostDTO>> getCommunityPosts(@PathVariable Long communityId,
                                                           @RequestParam(name = "sortBy", defaultValue = "creation_date") String criteria,
                                                           @RequestParam(name = "sort", defaultValue = "desc") String sortDirection) {
        if ( !postService.areSortParamsValid(criteria, sortDirection))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        Community community = communityService.findById(communityId);
        if (community == null || community.isSuspended())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<Post> posts =
            postService.findAllFromCommunitySortedBy(communityId, criteria, sortDirection);

        List<PostDTO> postsDTO = new ArrayList<>();

        for (Post p : posts) {
            PostDTO postDTO = new PostDTO(p, reactionService.getKarmaForPost(p.getId()));
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

        return new ResponseEntity<>(new PostDTO(post, reactionService.getKarmaForPost(post.getId())), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", value = "/{communityId}/posts")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PostDTO> createPost(Authentication authentication, @PathVariable Long communityId, @RequestBody @Validated PostDTO postSent) {

        Community community = communityService.findOneWithPosts(communityId);
        if (community == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User creator = userService.findByUsername(authentication.getName());

        Post newPost = new Post();
        newPost.setTitle(postSent.getTitle());
        newPost.setText(postSent.getText());
        newPost.setImagePath(postSent.getImagePath());
        newPost.setCreationDate(LocalDate.now());
        newPost.setFlair(flairService.findByName(postSent.getFlairName()));
        newPost.setPostedByUser(creator);
        community.addPost(newPost);
        communityService.save(community);
        Post savedPost = postService.save(newPost);
        reactionService.save(new Reaction(ReactionType.UPVOTE, newPost, null, creator));

        return new ResponseEntity<>(new PostDTO(savedPost, reactionService.getKarmaForPost(savedPost.getId())), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{communityId}/posts/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> updatePost(@PathVariable Long communityId, @RequestBody @Validated PostDTO postSent, @PathVariable Long postId) {

        Community community = communityService.findOneWithPosts(communityId);
        if (community == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Post targetedPost = postService.findById(postId);
        if (targetedPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        targetedPost.setTitle(postSent.getTitle());
        targetedPost.setText(postSent.getText());
//       TODO: poseban API -/endpoint
//        targetedPost.setImagePath(postSent.getImagePath());
        // TODO: treba li i promena Flair-a?
//        targetedPost.setFlair(flairService.findByName(postSent.getFlairName()));

        communityService.save(community);
//      postService.save(targetedPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

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


    @DeleteMapping(value = "/{communityId}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommunityDTO> banCommunity(@RequestBody @Validated BanCommunityDTO communityDTO, @PathVariable Long communityId) {

        // community must exist
        Community community = communityService.findById(communityId);
//        Community community = communityService.findByName(communityDTO.getName());
        if (community == null || community.isSuspended()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        community.setSuspended(true);
        community.setSuspensionReason(communityDTO.getSuspensionReason().trim() );
	    community.removeAllModerators();

        community = communityService.save(community);
        return new ResponseEntity(new CommunityDTO(community), HttpStatus.OK);
    }

    @GetMapping(value = "/{communityId}/posts/{postId}/flair")
    public ResponseEntity<FlairDTO> getFlairOfPost(@PathVariable Long communityId, @PathVariable Long postId) {
//        find by ID only, no join fetch needed since flair field in Post is EAGERLY loaded
        Post containingPost = postService.findById(postId);
        if (containingPost == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Flair wantedFlair = containingPost.getFlair();
        if (wantedFlair == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new FlairDTO(wantedFlair), HttpStatus.OK);
    }

    @PutMapping(value = "/{communityId}/posts/{postId}/flair")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> setFlairForPost(@RequestBody @Validated FlairDTO flairDTO, @PathVariable Long communityId, @PathVariable Long postId) {
//        find by ID only, no join fetch needed since flair field is EAGERLY loaded in Post
        Post containingPost = postService.findById(postId);
        if (containingPost == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Flair wantedFlair = flairService.findByNameForCommunityId(flairDTO.getName(), communityId);
        if (wantedFlair == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        containingPost.setFlair(wantedFlair);
        postService.save(containingPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{communityId}/flairs/{flair}")
    public ResponseEntity<FlairDTO> getFlairOfCommunity(@PathVariable Long communityId, @PathVariable String flair) {
//        comm & flair in it - must exist
        Community containingCommunity = communityService.findOneWithFlairs(communityId);
        if (containingCommunity == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Flair wantedFlair = flairService.findByName(flair);

        if (wantedFlair != null && containingCommunity.getFlairs().contains(wantedFlair)) {
            return new ResponseEntity<>(new FlairDTO(wantedFlair), HttpStatus.OK);
        };

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{communityId}/flairs/{flair}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteFlair(@PathVariable Long communityId, @PathVariable String flair) {
        Community containingCommunity = communityService.findOneWithFlairs(communityId);

        if (containingCommunity == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Flair wantedFlair = flairService.findOneWithCommunities(flair);

        if (wantedFlair != null && containingCommunity.getFlairs().contains(wantedFlair)) {
            containingCommunity.removeFlair(wantedFlair);
            communityService.save(containingCommunity);
            return new ResponseEntity<>(HttpStatus.OK);
        };

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
