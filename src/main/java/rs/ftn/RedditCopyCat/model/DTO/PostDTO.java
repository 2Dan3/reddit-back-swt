package rs.ftn.RedditCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ftn.RedditCopyCat.model.entity.Post;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class PostDTO {
    private Long id;
    @NotBlank
    private String title;
    private String author;
    @NotBlank
    private String text;
    private LocalDate creationDate;
    private String imagePath;
    private String flairName;
//    private Long communityId;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.imagePath = post.getImagePath();
        this.creationDate = post.getCreationDate();
        // TODO: check - test run it
        this.author = post.getPostedByUser().getDisplayName();
        this.flairName = post.getFlair().getName();
    }
                    /*Long id*/
    public PostDTO(String title, String author, String text, String imagePath, String flairName) {
//        this.id = id;
        this.title = title;
        this.author = author;
        this.text = text;
        this.creationDate = LocalDate.now();
        this.imagePath = imagePath;
        this.flairName = flairName;
    }


}
