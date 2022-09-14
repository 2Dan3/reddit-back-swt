package rs.ftn.RedditCopyCat.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "community")
@Getter
@Setter
@NoArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id", nullable = false, unique = true)
    private Long id;

    @ManyToMany(mappedBy = "moderatedCommunities", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH } )
    private Set<User> moderators = new HashSet<User>();

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private boolean isSuspended;
//    Boolean

    @Column
    private String suspensionReason;

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE,   CascadeType.REFRESH})
    private Set<Post> posts = new HashSet<Post>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "communities_flairs",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "flair_id")
    )
    private Set<Flair> flairs = new HashSet<Flair>();



    // Bidirectional Field Synchronizer - ADD- / REMOVE- child
    public void addFlair(Flair flair) {
        flair.getCommunities().add(this);
        flairs.add(flair);
        }
    public void removeFlair(Flair flair) {
        flairs.remove(flair);
        flair.getCommunities().remove(this);
    }
    public void addPost(Post post) {
        posts.add(post);
        post.setCommunity(this);
    }
    public void removePost(Post post) {
        posts.remove(post);
        post.setCommunity(null);
    }
    public void addModerator(User creator) {
        this.getModerators().add(creator);
        creator.getModeratedCommunities().add(this);
    }
    public void removeModerator(User moderator) {
        this.getModerators().remove(moderator);
        moderator.getModeratedCommunities().remove(this);
    }
    public void removeAllModerators(){
        for ( User moderator : this.getModerators() ) {
            moderator.getModeratedCommunities().remove(this);
        }
        this.getModerators().clear();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Community community = (Community) o;
        if (community.name == null || name == null) return false;
        return Objects.equals(name, community.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
