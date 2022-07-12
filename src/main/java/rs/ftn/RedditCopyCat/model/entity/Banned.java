package rs.ftn.RedditCopyCat.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

// TODO valja li Banned klasa?

@Data
@Entity
@Table(name = "banned")
public class Banned {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id", nullable = false)
    private Long id;

    @Column
    private LocalDate timestamp;

    @OneToOne
    private User bannedBy;

    @ManyToOne
    private User bannedUser;

    @ManyToOne
    private Community forCommunity;
}
