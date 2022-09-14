package rs.ftn.RedditCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeDataUserDTO {
    @NotBlank
    @Email(message = "Invalid email format", regexp = ".+[@].+[\\.].+")
    private String email;
    @NotBlank
    private String displayName;
    @NotBlank
    private String description;
    private String avatar;
}
