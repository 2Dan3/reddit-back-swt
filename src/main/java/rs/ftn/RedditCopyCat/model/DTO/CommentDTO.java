package rs.ftn.RedditCopyCat.model.DTO;

import lombok.Getter;
import lombok.Setter;
import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.model.entity.User;

import java.time.LocalDate;

@Getter
@Setter
public class CommentDTO {

    private Long id;
    private String text;
    private LocalDate timestamp;
    private Long postId;
    private String authorDisplayName;
//    private Long parentCommentId;

    public CommentDTO(Comment c) {
        this.id = c.getId();
        this.text = c.getText();
        this.timestamp = c.getTimestamp();
        this.postId = c.getBelongsToPost().getId();
        this.authorDisplayName = c.getBelongsToUser().getDisplayName();
//        this.parentCommentId = c.getParentComment().getId();
    }
}
