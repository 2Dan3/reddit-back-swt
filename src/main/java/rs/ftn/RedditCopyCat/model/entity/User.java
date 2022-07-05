package rs.ftn.RedditCopyCat.model.entity;

import lombok.*;
import rs.ftn.RedditCopyCat.model.entity.Moderator;
import rs.ftn.RedditCopyCat.model.enums.Roles;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "user_role", discriminatorType = DiscriminatorType.STRING)
//@DiscriminatorValue("USER")
public class User implements Serializable {

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

// TODO treba li orphanRemoval = true ovde ?
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Moderator> moderators = new HashSet<Moderator>();

//    @Transient
    private Roles role;
}
