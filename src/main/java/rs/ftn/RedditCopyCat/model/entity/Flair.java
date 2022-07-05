package rs.ftn.RedditCopyCat.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.RedditCopyCat.model.entity.Community;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Flair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

//    TODO parametrizovati manytomany annotation?
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Community> communities = new HashSet<Community>();
}
