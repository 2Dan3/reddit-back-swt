package rs.ftn.RedditCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ftn.RedditCopyCat.model.entity.Flair;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
public class FlairDTO {
    private Long id;
    @NotBlank
    private String name;

    public FlairDTO(Flair flair) {
        this.id = flair.getId();
        this.name = flair.getName();
    }
}
