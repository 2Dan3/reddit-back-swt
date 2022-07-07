package rs.ftn.RedditCopyCat.model.DTO;

//import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import rs.ftn.RedditCopyCat.model.entity.User;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
    @NotBlank
    private String email;

    private String avatar;

    private String displayName;

    private String description;

    public UserDTO(User createdUser) {
        this.id = createdUser.getId();
        this.username = createdUser.getUsername();
        this.avatar = createdUser.getAvatar();
        this.email = createdUser.getEmail();
        this.displayName = createdUser.getDisplayName();
        this.description = createdUser.getDescription();
    }

}
