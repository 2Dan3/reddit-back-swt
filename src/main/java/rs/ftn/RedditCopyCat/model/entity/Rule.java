package rs.ftn.RedditCopyCat.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "rule")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private Community belongsToCommunity;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rule r = (Rule) o;
        return id != null && id.equals(r.getId());
    }

    @Override
    public int hashCode() {
        /*
         * Pretpostavka je da je u pitanju tranzijentni objekat (jos nije sacuvan u bazu) i da id ima null vrednost.
         * Kada se sacuva u bazu dobice non-null vrednost. To znaci da ce objekat imati razlicite kljuceve u dva stanja, te ce za generisan
         * hashCode i equals vratiti razlicite vrednosti. Vracanje konstantne vrednosti resava ovaj problem.
         * Sa druge strane ovakva implementacija moze da afektuje performanse u slucaju velikog broja objekata
         * koji ce zavrsiti u istom hash bucket-u.
         */
        return 1337;
    }
}
