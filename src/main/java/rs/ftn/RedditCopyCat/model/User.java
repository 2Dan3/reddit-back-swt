package rs.ftn.RedditCopyCat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

//    @Email
    @Column
    private String email;

    @Column(nullable = true)
    private String avatar;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @Column
    private String description;
    @Column
    private String displayName;

// TODO treba li orphanRemoval ovde ?
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Moderator> moderators = new HashSet<Moderator>();
}
