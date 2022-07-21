package rs.ftn.RedditCopyCat.model.DTO;

import javax.validation.constraints.NotBlank;

// DTO za login
public class JwtAuthenticationRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
