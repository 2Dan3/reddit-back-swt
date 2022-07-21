package rs.ftn.RedditCopyCat.model.entity;

import lombok.Data;
import rs.ftn.RedditCopyCat.model.enums.ReactionType;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "reaction")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReactionType type;

    @Column
    private LocalDate timestamp;

    @ManyToOne
    private Post toPost;

    @ManyToOne
    private Comment toComment;

    @ManyToOne
    private User madeBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reaction reaction = (Reaction) o;
        return id != null && id.equals(reaction.getId());
    }

    @Override
    public int hashCode() {
        return 2337;
    }
}
