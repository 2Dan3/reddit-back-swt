package rs.ftn.RedditCopyCat.model.entity;

import lombok.Data;
import rs.ftn.RedditCopyCat.model.enums.ReportReason;

import javax.persistence.*;
import java.time.LocalDate;

// TODO valja li entitet?

@Data
@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @Column
    private LocalDate timestamp;

    @ManyToOne
    private User byUser;

    @Column
    private boolean accepted;

    @ManyToOne
    private Post forPost;

    @ManyToOne
    private Comment forComment;
}
