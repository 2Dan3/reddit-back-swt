package rs.ftn.RedditCopyCat.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.RedditCopyCat.model.DTO.PostDTO;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column
    private String imagePath;

//    TODO treba li "EAGER" uopste?
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST} )
    @JoinColumn(name = "flair_id")
    private Flair flair;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User postedByUser;

//    TODO: @JsonIgnore
//    TODO: Skinuti fetch = FetchType.EAGER ?
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "community_id")
    private Community community;

//    public Post(PostDTO postDTO) {
////        this.id = postDTO.getId();
//        this.title = postDTO.getTitle();
//        this.text = postDTO.getText();
//        // TODO: check if Date is persisted or null
//        this.creationDate = postDTO.getCreationDate();
//        this.imagePath = postDTO.getImagePath();
//        this.flair = flairService.findByName(postDTO.getFlairName());
//        this.community = communityService
//    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post p = (Post) o;
        return id != null && id.equals(p.getId());
    }

    @Override
    public int hashCode() {
        return 1554;
    }
}
