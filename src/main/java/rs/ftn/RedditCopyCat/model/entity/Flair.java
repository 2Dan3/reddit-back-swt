package rs.ftn.RedditCopyCat.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.RedditCopyCat.model.entity.Community;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "flair")
@Getter
@Setter
@NoArgsConstructor
public class Flair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flair_id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

//    @JsonIgnore
//    TODO parametrizovati manytomany annotation?
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Community> communities = new HashSet<Community>();
}
