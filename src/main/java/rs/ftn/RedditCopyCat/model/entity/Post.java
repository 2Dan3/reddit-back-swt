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
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String text;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column
    private String imagePath;

//    TODO treba li "cascadeAll"?
//    TODO treba li "EAGER" uopste?
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flair_id")
    private Flair flair;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User postedByUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "community_id")
    private Community community;

}
