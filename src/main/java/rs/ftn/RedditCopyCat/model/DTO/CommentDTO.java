package rs.ftn.RedditCopyCat.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.model.entity.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {

    private Long id;
    @NotBlank
    private String text;
//    @NotBlank
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

    public CommentDTO(Long id, String text, LocalDate timestamp, Long postId, String authorDisplayName) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.postId = postId;
        this.authorDisplayName = authorDisplayName;
    }
}
