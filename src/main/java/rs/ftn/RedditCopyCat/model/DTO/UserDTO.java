package rs.ftn.RedditCopyCat.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import rs.ftn.RedditCopyCat.model.entity.User;

@Data
public class UserDTO {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public UserDTO(User createdUser) {
        this.id = createdUser.getId();
        this.username = createdUser.getUsername();
    }

}
