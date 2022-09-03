package rs.ftn.RedditCopyCat.model.DTO;

import lombok.Getter;
import lombok.Setter;
import rs.ftn.RedditCopyCat.model.entity.Community;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class CreateCommunityDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    public CreateCommunityDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CreateCommunityDTO() { };

}
