package rs.ftn.RedditCopyCat.model.DTO;

//import jakarta.validation.constraints.NotBlank;
import lombok.*;
import rs.ftn.RedditCopyCat.model.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Email(message = "Invalid email format", regexp = ".+[@].+[\\.].+")
    private String email;

    private String avatar;

    private String displayName;

    private String description;

    public UserDTO(User createdUser) {
        this.id = createdUser.getId();
        this.username = createdUser.getUsername();
        this.avatar = createdUser.getAvatar();
        this.email = createdUser.getEmail();
        this.displayName = createdUser.getDisplayName() == null? this.username : createdUser.getDisplayName();
        this.description = createdUser.getDescription();
    }

}
