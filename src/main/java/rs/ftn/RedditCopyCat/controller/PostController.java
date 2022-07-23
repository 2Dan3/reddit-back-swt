package rs.ftn.RedditCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ftn.RedditCopyCat.model.DTO.CommentDTO;
import rs.ftn.RedditCopyCat.model.DTO.FlairDTO;
import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.entity.Flair;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.service.CommentService;
import rs.ftn.RedditCopyCat.service.PostService;
import rs.ftn.RedditCopyCat.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "reddit/posts")
public class PostController {

    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    Principal principal;

//    TODO: Sort by Reactions
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long postId,
                                                        @RequestParam(name = "replyToId", defaultValue = "null") Long parentId,
                                                        @RequestParam(name = "sortBy", defaultValue = "timestamp") String criteria,
                                                        @RequestParam(name = "sort", defaultValue = "desc") String sortDirection) {
        Post targetedPost = postService.findById(postId);
        if (targetedPost == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        List<Comment> resultComments;

        if (parentId == null) {
            resultComments = commentService.findAllForPost(postId, criteria, sortDirection);
        }else {
            Comment parent = commentService.findById(parentId);
            if (parent == null || parent.isDeleted()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            resultComments = commentService.findRepliesTo(parentId, criteria, sortDirection);
        }
        if (resultComments == null) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }

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

    @PostMapping("/{postId}/comments/{parentId}")
    public ResponseEntity<CommentDTO> makeComment(@PathVariable Long postId, @RequestBody @Validated CommentDTO receivedComment, @PathVariable Long parentId) {
        Post targetedPost = postService.findById(postId);
        if (targetedPost == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Comment madeComment = commentService.attachComment(targetedPost, parentId, receivedComment.getText());
        return new ResponseEntity<>(new CommentDTO(madeComment), HttpStatus.CREATED);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getAllReplies(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody @Validated CommentDTO receivedComment) {

        Comment targetComment = commentService.findById(commentId);
        if (postService.findById(postId) == null || targetComment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        targetComment.setText(receivedComment.getText());
//        targetComment.setTimestamp(LocalDate.now());
        targetComment = commentService.save(targetComment);

        return new ResponseEntity<>(new CommentDTO(targetComment), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> removeComment(@PathVariable Long postId, @PathVariable Long commentId) {

        Comment targetComment = commentService.findById(commentId);
        if (postService.findById(postId) == null || targetComment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        targetComment.setDeleted(true);

        targetComment = commentService.save(targetComment);
        return new ResponseEntity<>(new CommentDTO(targetComment), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        Post targetedPost = postService.findById(postId);
        if (targetedPost == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        targetedPost.getCommunity().removePost(targetedPost);
        postService.delete(targetedPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
