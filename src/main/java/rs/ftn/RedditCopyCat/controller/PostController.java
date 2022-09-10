package rs.ftn.RedditCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;
import rs.ftn.RedditCopyCat.model.DTO.CommentDTO;
import rs.ftn.RedditCopyCat.model.DTO.FlairDTO;
import rs.ftn.RedditCopyCat.model.DTO.PostDTO;
import rs.ftn.RedditCopyCat.model.entity.*;
import rs.ftn.RedditCopyCat.model.enums.ReactionType;
import rs.ftn.RedditCopyCat.service.CommentService;
import rs.ftn.RedditCopyCat.service.PostService;
import rs.ftn.RedditCopyCat.service.ReactionService;
import rs.ftn.RedditCopyCat.service.UserService;

import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReactionService reactionService;
    @Autowired
    private ServletContext servletContext;

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long postId,
                                                        @RequestParam(name = "replyToId", required = false) Long parentId,
                                                        @RequestParam(name = "sortBy", defaultValue = "timestamp") String criteria,
                                                        @RequestParam(name = "sort", defaultValue = "desc") String sortDirection) {
        if ( !commentService.areSortParamsValid(criteria, sortDirection))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        Post targetedPost = postService.findById(postId);
        if (targetedPost == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        List<Comment> resultComments;
        if (parentId == null) {
            resultComments = commentService.findAllForPost(postId, criteria, sortDirection);
        }else {
            Comment parent = commentService.findById(parentId);
            if (parent == null || parent.isDeleted())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            resultComments = commentService.findRepliesTo(parentId, criteria, sortDirection);
        }

        List<CommentDTO> commentsDTO = new ArrayList<>();
        for (Comment c : resultComments) {
            commentsDTO.add(new CommentDTO(c));
        }
        return new ResponseEntity<>(commentsDTO, HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable Long postId, @PathVariable Long commentId) {
        Post wantedPost = postService.findById(postId);
        if (wantedPost == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Comment wantedComment = commentService.findById(commentId);
        if (wantedComment == null || wantedComment.isDeleted()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new CommentDTO(wantedComment), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", value = "/{postId}/comments/{parentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDTO> makeComment(Authentication authentication, @PathVariable Long postId, @RequestBody @Validated CommentDTO receivedComment, @PathVariable Long parentId) {
        Post targetedPost = postService.findById(postId);
        if (targetedPost == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User creator = userService.findByUsername(authentication.getName() );

        Comment madeComment = commentService.attachComment(creator, targetedPost, parentId, receivedComment.getText());
        reactionService.save(new Reaction(ReactionType.UPVOTE, null, madeComment, creator));
        return new ResponseEntity<>(new CommentDTO(madeComment), HttpStatus.CREATED);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDTO> editOwnComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody @Validated CommentDTO receivedComment) {

        Comment targetComment = commentService.findById(commentId);
        if (postService.findById(postId) == null || targetComment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        targetComment.setText(receivedComment.getText());
        targetComment.setTimestamp(LocalDate.now());
        targetComment = commentService.save(targetComment);

        return new ResponseEntity<>(new CommentDTO(targetComment), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDTO> removeOwnComment(@PathVariable Long postId, @PathVariable Long commentId) {

        Comment targetComment = commentService.findById(commentId);
        if (postService.findById(postId) == null || targetComment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        targetComment.setDeleted(true);

        targetComment = commentService.save(targetComment);
        return new ResponseEntity<>(new CommentDTO(targetComment), HttpStatus.OK);
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {

        Post post = postService.findById(postId);
        if (post == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new PostDTO(post), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteOwnPost(@PathVariable Long postId) {
        Post targetedPost = postService.findById(postId);
        if (targetedPost == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        targetedPost.getCommunity().removePost(targetedPost);
        postService.delete(targetedPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    TODO*: Image Get, Put, ?Post?

    @GetMapping(value = "/{postId}/image")
    public ResponseEntity<Resource> getImageForPost(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String pathToImage = post.getImagePath();
//        TODO: . . .
//
        Resource resource =
                new ServletContextResource(servletContext, pathToImage);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PutMapping(value = "/{postId}/image")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> setImageForPost(@PathVariable Long postId, @RequestBody String imagePath) {
        Post post = postService.findById(postId);
        if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        post.setImagePath(imagePath);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
