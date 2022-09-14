package rs.ftn.RedditCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ftn.RedditCopyCat.model.DTO.RuleDTO;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.entity.Rule;
import rs.ftn.RedditCopyCat.service.CommunityService;
import rs.ftn.RedditCopyCat.service.RulesService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "${apiPrefix}/communities/{communityId}/rules")
public class RuleController {

    @Autowired
    private RulesService rulesService;
    @Autowired
    private CommunityService communityService;

    //TODO *  !  !  !  *
    @GetMapping(value = "/")
    public ResponseEntity<List<RuleDTO>> getCommunityRules(@PathVariable Long communityId) {

        // JOIN FETCH query
        Set<Rule> rules = rulesService.findByCommunityId(communityId);

        List<RuleDTO> rulesDTO = new ArrayList<>();
        for (Rule r : rules) {
            RuleDTO ruleDTO = new RuleDTO(r);
            rulesDTO.add(ruleDTO);
        }
        return new ResponseEntity<>(rulesDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", value = "/")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<RuleDTO> createRule(@Valid @RequestBody RuleDTO ruleDTO, @PathVariable Long communityId) {

        // JOIN FETCH query
        Community targetedCommunity = communityService.findById(communityId);
        if (targetedCommunity == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Rule createdRule = new Rule();
        createdRule.setDescription(ruleDTO.getDescription());
        createdRule.setBelongsToCommunity(targetedCommunity);

        createdRule = rulesService.save(createdRule);
        if (createdRule == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

//        return new ResponseEntity<>(new RuleDTO(createdRule), HttpStatus.OK);
        return new ResponseEntity<>(new RuleDTO(createdRule), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{ruleId}")
    public ResponseEntity<RuleDTO> getCommunityRule(@PathVariable Long communityId, @PathVariable Long ruleId) {

        Rule wantedRule = rulesService.findById(ruleId);
        if (wantedRule == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new RuleDTO(wantedRule), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{ruleId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> removeRule(@PathVariable Long communityId, @PathVariable Long ruleId) {

        Rule wantedRule = rulesService.findById(ruleId);
        if (wantedRule == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        rulesService.remove(wantedRule);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
