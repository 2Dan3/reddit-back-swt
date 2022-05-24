package rs.ftn.RedditCopyCat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Flair {

    @Column
    private String name;

//    TODO parametrizovati manytomany annotation?
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Community> communities = new HashSet<Community>();
}
