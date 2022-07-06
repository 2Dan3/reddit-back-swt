package rs.ftn.RedditCopyCat.model.entity;

import lombok.Data;
import rs.ftn.RedditCopyCat.model.enums.ReactionType;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id", nullable = false)
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
}
