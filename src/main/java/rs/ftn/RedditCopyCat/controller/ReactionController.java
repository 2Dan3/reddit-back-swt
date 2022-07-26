package rs.ftn.RedditCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ftn.RedditCopyCat.model.DTO.ReactionDTO;
import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.model.entity.Reaction;
import rs.ftn.RedditCopyCat.model.entity.User;
import rs.ftn.RedditCopyCat.service.CommentService;
import rs.ftn.RedditCopyCat.service.PostService;
import rs.ftn.RedditCopyCat.service.ReactionService;
import rs.ftn.RedditCopyCat.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "reddit")
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

    @GetMapping(value = "/users/{userId}/karma")
    public ResponseEntity<Integer> getUserKarmaPoints(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Integer karmaPoints = reactionService.getTotalKarmaPoints(user);
//        TODO query
        return new ResponseEntity<>(karmaPoints, HttpStatus.OK);
    }

//    *TODO: URL-PATH !
    @GetMapping()
    public ResponseEntity<ReactionDTO> getUserReactionToPost(@PathVariable Long postId, @PathVariable Long userId) {
        if (postService.findById(postId) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Reaction userReaction = reactionService.findForPostByUser(postId, userId);
        if (userReaction == null)
            return new ResponseEntity<>(null, HttpStatus.OK);

        return new ResponseEntity<>(new ReactionDTO(userReaction), HttpStatus.OK);
    }

    //    *TODO: URL-PATH
    @GetMapping()
    public ResponseEntity<ReactionDTO> getUserReactionToComment(@PathVariable Long commentId, @PathVariable Long userId) {
        if (commentService.findById(commentId) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Reaction userReaction = reactionService.findForCommentByUser(commentId, userId);
        if (userReaction == null)
            return new ResponseEntity<>(null, HttpStatus.OK);

        return new ResponseEntity<>(new ReactionDTO(userReaction), HttpStatus.OK);
    }

//    *TODO: URL-PATH
    @PutMapping()
    public ResponseEntity<ReactionDTO> changeReaction(@RequestBody @Validated ReactionDTO receivedReaction) {

        Reaction existingReaction = reactionService.findById(receivedReaction.getId());
        if (existingReaction == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        existingReaction.setType(receivedReaction.getType());
        existingReaction.setTimestamp(LocalDate.now());

        existingReaction = reactionService.save(existingReaction);
        return new ResponseEntity<>(new ReactionDTO(existingReaction), HttpStatus.OK);
    }

//    *TODO: URL-PATH
    @PostMapping(value = "/posts/{postId}/reactions")
    public ResponseEntity<ReactionDTO> reactToPost(@PathVariable Long postId, @RequestBody @Validated ReactionDTO receivedReaction) {

        Post targetedPost = postService.findById(postId);
        if (targetedPost == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Reaction newReaction = new Reaction();
        newReaction.setType(receivedReaction.getType());
        newReaction.setToPost(targetedPost);
        newReaction.setTimestamp(LocalDate.now());

        newReaction = reactionService.save(newReaction);
        return new ResponseEntity<>(new ReactionDTO(newReaction), HttpStatus.CREATED);
    }

    //    *TODO: URL-PATH
    @PostMapping(value = "/comments/{commentId}/reactions")
    public ResponseEntity<ReactionDTO> reactToComment(@PathVariable Long commentId, @RequestBody @Validated ReactionDTO receivedReaction) {

        Comment targetedComment = commentService.findById(commentId);
        if (targetedComment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Reaction newReaction = new Reaction();
        newReaction.setType(receivedReaction.getType());
        newReaction.setToComment(targetedComment);
        newReaction.setTimestamp(LocalDate.now());

        newReaction = reactionService.save(newReaction);
        return new ResponseEntity<>(new ReactionDTO(newReaction), HttpStatus.CREATED);
    }
}
