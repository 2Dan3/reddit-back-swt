package rs.ftn.RedditCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCommunityDTO {
    private Long id;
//    @NotBlank
    private String name;
    @NotBlank(message = "Description must be provided!")
    private  String description;
}
