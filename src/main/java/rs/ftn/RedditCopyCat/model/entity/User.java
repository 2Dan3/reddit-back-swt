package rs.ftn.RedditCopyCat.model.entity;

import lombok.*;
import rs.ftn.RedditCopyCat.model.enums.Roles;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USER")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

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
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<Moderator> moderators = new HashSet<Moderator>();

    @ManyToMany
    @JoinTable(
            name = "moderator",
            joinColumns = @JoinColumn(name = "u_id"),
            inverseJoinColumns = @JoinColumn(name = "commu_id")
    )
    private Set<Community> moderatedCommunities  = new HashSet<Community>();

//    @Transient
    @Column(nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;

//    TODO check JPA Persistence & in Administrator Class
    @Transient
    public String getRole(){
        return this.getClass().getAnnotation(DiscriminatorValue.class).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (user.username == null || username == null) return false;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}
