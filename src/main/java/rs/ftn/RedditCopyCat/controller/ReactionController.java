package rs.ftn.RedditCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rs.ftn.RedditCopyCat.model.DTO.ReactionDTO;
import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.model.entity.Reaction;
import rs.ftn.RedditCopyCat.model.entity.User;
import rs.ftn.RedditCopyCat.model.enums.ReactionType;
import rs.ftn.RedditCopyCat.service.CommentService;
import rs.ftn.RedditCopyCat.service.PostService;
import rs.ftn.RedditCopyCat.service.ReactionService;
import rs.ftn.RedditCopyCat.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "${apiPrefix}")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/posts/{postId}/reactions")
    public ResponseEntity<List<ReactionDTO>> getReactionsToPost(@PathVariable Long postId) {
        Post targetedPost = postService.findById(postId);
        if (targetedPost == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<Reaction> reactionsToPost = reactionService.findAllForPost(targetedPost);

        List<ReactionDTO> reactionsDTO = new ArrayList<>();
        for (Reaction r: reactionsToPost) {
            reactionsDTO.add(new ReactionDTO(r));
        }
        return new ResponseEntity<>(reactionsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/comments/{commentId}/reactions")
    public ResponseEntity<List<ReactionDTO>> getReactionsToComment(@PathVariable Long commentId) {
        Comment targetedComment = commentService.findById(commentId);
        if (targetedComment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<Reaction> reactionsToComment = reactionService.findAllForComment(targetedComment);

        List<ReactionDTO> reactionsDTO = new ArrayList<>();
        for (Reaction r: reactionsToComment) {
            reactionsDTO.add(new ReactionDTO(r));
        }
        return new ResponseEntity<>(reactionsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/users/karma")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Integer> getUserKarmaPoints(Authentication authentication) {
        User loggedUser = userService.findByUsername(authentication.getName());
        if (loggedUser == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Integer karmaPoints = reactionService.getTotalKarmaPoints(loggedUser);
        if (karmaPoints == null) {
            karmaPoints = 0;
        }
        return new ResponseEntity<>(karmaPoints, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/reaction")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReactionDTO> getCurrentUserReactionToPost(Authentication authentication, @PathVariable Long postId) {
        if (postService.findById(postId) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User currentUser = userService.findByUsername(authentication.getName());

        Reaction userReaction = reactionService.findForPostByUser(postId, currentUser.getId());
        if (userReaction == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new ReactionDTO(userReaction), HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}/reaction")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReactionDTO> getCurrentUserReactionToComment(Authentication authentication, @PathVariable Long commentId) {
        if (commentService.findById(commentId) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User currentUser = userService.findByUsername(authentication.getName());

        Reaction userReaction = reactionService.findForCommentByUser(commentId, currentUser.getId());
        if (userReaction == null)
            return new ResponseEntity<>(null, HttpStatus.OK);

        return new ResponseEntity<>(new ReactionDTO(userReaction), HttpStatus.OK);
    }

//    @PutMapping("/reactions/{reactionId}")
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//    public ResponseEntity<ReactionDTO> changeOwnReaction(@RequestBody @Validated ReactionDTO receivedReaction, @PathVariable Long reactionId) {
//
//        Reaction existingReaction = reactionService.findById(reactionId);
////        Reaction existingReaction = reactionService.findById(receivedReaction.getId());
//        if (existingReaction == null)
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//        existingReaction.setType(receivedReaction.getType());
//        existingReaction.setTimestamp(LocalDate.now());
//
//        existingReaction = reactionService.save(existingReaction);
//        return new ResponseEntity<>(new ReactionDTO(existingReaction), HttpStatus.OK);
//    }

    @PostMapping(consumes = "application/json",
                value = "/posts/{postId}/reactions")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReactionDTO> reactToPost(Authentication authentication, @PathVariable Long postId, @Valid @RequestBody ReactionDTO receivedReaction) {

        Post targetedPost = postService.findById(postId);
        if (targetedPost == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User currentUser = userService.findByUsername(authentication.getName());

        Reaction existingReaction = reactionService.findForPostByUser(postId, currentUser.getId());
        if (existingReaction == null) {
            Reaction newReaction = new Reaction();
            newReaction.setType(ReactionType.valueOf(receivedReaction.getType()));
            newReaction.setToPost(targetedPost);
            newReaction.setMadeBy(currentUser);
            newReaction.setTimestamp(LocalDate.now());

            newReaction = reactionService.save(newReaction);
            return new ResponseEntity<>(new ReactionDTO(newReaction), HttpStatus.CREATED);
        }else {
            if (existingReaction.getType().toString().equalsIgnoreCase(receivedReaction.getType()))
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

            existingReaction.setType(ReactionType.valueOf(receivedReaction.getType()));
            existingReaction = reactionService.save(existingReaction);
            return new ResponseEntity<>(new ReactionDTO(existingReaction), HttpStatus.CREATED);
        }
    }

    @PostMapping(consumes = "application/json", value = "/comments/{commentId}/reactions")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReactionDTO> reactToComment(Authentication authentication, @PathVariable Long commentId, @Valid @RequestBody ReactionDTO receivedReaction) {

        Comment targetedComment = commentService.findById(commentId);
        if (targetedComment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User currentUser = userService.findByUsername(authentication.getName());

        Reaction existingReaction = reactionService.findForCommentByUser(commentId, currentUser.getId());
        if (existingReaction == null) {
            Reaction newReaction = new Reaction();
            newReaction.setType(ReactionType.valueOf(receivedReaction.getType()));
            newReaction.setToComment(targetedComment);
            newReaction.setMadeBy(currentUser);
            newReaction.setTimestamp(LocalDate.now());

            newReaction = reactionService.save(newReaction);
            return new ResponseEntity<>(new ReactionDTO(newReaction), HttpStatus.CREATED);
        } else {
            if (existingReaction.getType().toString().equalsIgnoreCase(receivedReaction.getType()))
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

            existingReaction.setType(ReactionType.valueOf(receivedReaction.getType()));
            existingReaction = reactionService.save(existingReaction);
            return new ResponseEntity<>(new ReactionDTO(existingReaction), HttpStatus.CREATED);
        }
    }

    @GetMapping("/comments/{commentId}/karma")
    public ResponseEntity<Integer> getCommentTotalKarma(@PathVariable Long commentId) {
        if(commentService.findById(commentId) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(reactionService.getKarmaForComment(commentId), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/karma")
    public ResponseEntity<Integer> getPostTotalKarma(@PathVariable Long postId) {
        if(postService.findById(postId) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(reactionService.getKarmaForPost(postId), HttpStatus.OK);
    }

}
