package rs.ftn.RedditCopyCat.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rule")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id", nullable = false)
    private Long id;

    @ManyToOne
    private Community belongsToCommunity;
}
