package rs.ftn.RedditCopyCat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Moderator {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

//    TODO treba li EAGER?
    @OneToOne(fetch = FetchType.LAZY)
    private Community community;
}
