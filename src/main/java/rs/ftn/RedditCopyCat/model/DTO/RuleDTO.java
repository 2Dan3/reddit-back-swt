package rs.ftn.RedditCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ftn.RedditCopyCat.model.entity.Rule;

@AllArgsConstructor
@Getter
@Setter
public class RuleDTO {
    private Long id;
    private String description;
//    private CommunityDTO? community;

    public RuleDTO(Rule rule) {
        this.id = rule.getId();
        this.description = rule.getDescription();
    }
}
