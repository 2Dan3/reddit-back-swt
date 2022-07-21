package rs.ftn.RedditCopyCat.model.entity;

import lombok.Data;
import rs.ftn.RedditCopyCat.model.enums.ReportReason;

import javax.persistence.*;
import java.time.LocalDate;

// TODO valja li entitet?

@Data
@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @Column
    private LocalDate timestamp;

    @ManyToOne
    private User byUser;

    @Column(nullable = false)
    private boolean accepted;
//    Boolean

    @ManyToOne
    private Post forPost;

    @ManyToOne
    private Comment forComment;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Report r = (Report) o;
        return id != null && id.equals(r.getId());
    }

    @Override
    public int hashCode() {
        return 1821;
    }
}
