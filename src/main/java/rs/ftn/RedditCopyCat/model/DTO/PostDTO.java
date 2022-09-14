package rs.ftn.RedditCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.service.ReactionService;

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
    private Integer karmaPoints;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.imagePath = post.getImagePath();
        this.creationDate = post.getCreationDate();
        this.author = post.getPostedByUser().getDisplayName();
        this.flairName = post.getFlair()==null?null:post.getFlair().getName();
    }
                    /*Long id*/
    public PostDTO(String title, String author, String text, LocalDate creationDate, String imagePath, String flairName) {
//        this.id = id;
        this.title = title;
        this.author = author;
        this.text = text;
        this.creationDate = LocalDate.now();
        this.imagePath = imagePath;
        this.flairName = flairName;
    }

    public PostDTO(){ }

    public PostDTO(Post post, Integer karmaForPost) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.imagePath = post.getImagePath();
        this.creationDate = post.getCreationDate();
        this.author = post.getPostedByUser().getDisplayName();
        this.flairName = post.getFlair()==null?null:post.getFlair().getName();
        this.karmaPoints = karmaForPost;
    }
}
