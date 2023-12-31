package rs.ftn.RedditCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import rs.ftn.RedditCopyCat.model.entity.Reaction;
import rs.ftn.RedditCopyCat.model.entity.User;
import rs.ftn.RedditCopyCat.model.enums.ReactionType;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReactionDTO {
    private Long id;
    @NotBlank(message = "Reaction type required!")
    private String type;
    private LocalDate timestamp;
    private Long authorId;

    public ReactionDTO(Reaction r) {
        this.id = r.getId();
        this.type = r.getType().toString();
        this.timestamp = r.getTimestamp();
        this.authorId = r.getMadeBy().getId();
    }
}
