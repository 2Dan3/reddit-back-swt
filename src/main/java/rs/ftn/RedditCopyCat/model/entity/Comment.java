package rs.ftn.RedditCopyCat.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column
    private String text;

    @Column(nullable = false)
    private LocalDate timestamp;

    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    private Post belongsToPost;

    @ManyToOne(fetch = FetchType.EAGER)
    private User belongsToUser;

//    TODO hoce li (u slucaju OneToMany) cascadeAll ovde praviti problem (ko je parent a ko child/reply)?
//    @OneToMany(mappedBy = "repliesTo", cascade = CascadeType.ALL)
    @ManyToOne()
    private Comment parentComment;
}
