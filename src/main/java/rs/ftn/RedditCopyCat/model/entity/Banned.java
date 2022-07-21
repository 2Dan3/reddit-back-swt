package rs.ftn.RedditCopyCat.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "banned")
public class Banned {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id", nullable = false, unique = true)
    private Long id;

    @Column
    private LocalDate timestamp;

    @OneToOne
    private User bannedBy;

    @ManyToOne
    private User bannedUser;

    @ManyToOne
    private Community forCommunity;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Banned banned = (Banned) o;
        return id != null && id.equals(banned.getId());
    }

    @Override
    public int hashCode() {
        return 1519;
    }
}
