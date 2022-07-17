package rs.ftn.RedditCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ftn.RedditCopyCat.model.entity.Rule;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
public class RuleDTO {
    private Long id;
    @NotBlank
    private String description;
//    private CommunityDTO? community;
//    private Long communityId;

    public RuleDTO(Rule rule) {
        this.id = rule.getId();
        this.description = rule.getDescription();
//        this.communityId = rule.getBelongsToCommunity().getId();
    }
}
