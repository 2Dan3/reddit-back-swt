package rs.ftn.RedditCopyCat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.ftn.RedditCopyCat.model.DTO.CommunityDTO;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.model.entity.Report;
import rs.ftn.RedditCopyCat.model.entity.User;
import rs.ftn.RedditCopyCat.service.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

//** Komponenta koja moze da obavlja dodatnu proveru zahteva pre nego sto dospe na endpoint.
//Moguce je pristupiti @PathVariable podacima sa URL-a zahteva na endpoint, poput {id}.
//https://docs.spring.io/spring-security/site/docs/5.2.11.RELEASE/reference/html/authorization.html

@Component
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity {

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReactionService reactionService;
    @Autowired
    private ReportService reportService;

    public boolean moderatesCommunity(Long communityId, Principal principal) {
        User moderator = userService.findByUsername( ((UserDetails)principal).getUsername());
        if (moderator == null)
            return false;
        if (!userService.moderatesCommunity(communityId, moderator))
            return false;
        return true;
    }

    public boolean moderatesCommunity(CommunityDTO communityDTO, Principal principal) {
        User moderator = userService.findByUsername( ((UserDetails)principal).getUsername());
        if (moderator == null)
            return false;
        if (!userService.moderatesCommunity(communityDTO.getId(), moderator))
            return false;
        return true;
    }

    public boolean isUserLogged(Principal principal) {
        if ( (UserDetails)principal == null)
            return false;
        return true;
    }

    public boolean canChangePost(Long postId, Principal principal) {
        User loggedUser = userService.findByUsername( ((UserDetails)principal).getUsername());
        if (loggedUser == null)
            return false;

        if (!canUserParttake(postId, principal) || !postService.isAuthor(postId, loggedUser.getId()))
            return false;
        return true;
    }

    public boolean canChangeComment(Long postId, Long commentId, Principal principal) {
        User loggedUser = userService.findByUsername( ((UserDetails)principal).getUsername());
        if (loggedUser == null)
            return false;

        if (!canUserParttake(postId, principal) || !commentService.isAuthor(commentId, loggedUser.getId()))
            return false;
        return true;
    }

    public boolean canUserParttake(Long postId, Principal principal) {
        // This method checks that the given User is
        // authenticated & not banned from associated Community
        User loggedUser = userService.findByUsername( ((UserDetails)principal).getUsername());
        if (loggedUser == null)
            return false;

        Post foundPost = postService.findById(postId);
        if (foundPost == null || userService.isUserBanned(loggedUser, foundPost.getCommunity()))
            return false;
        return true;
    }

    public boolean canChangeReaction(Long reactionId, Principal principal) {
        User loggedUser = userService.findByUsername( ((UserDetails)principal).getUsername());
        if (loggedUser == null)
            return false;

        Post foundPost = postService.findByReactionId(reactionId);

        if (foundPost == null || !canUserParttake(foundPost.getId(), principal) || !reactionService.isAuthor(reactionId, loggedUser.getId()))
            return false;
        return true;
    }

    public boolean canReactToPost(Principal principal, Long postId) {
        User loggedUser = userService.findByUsername( ((UserDetails)principal).getUsername());

        if (loggedUser == null || reactionService.existsForPost(postId, loggedUser) ||
                !canUserParttake(postId, principal) )
        {
            return false;
        }
        return true;
    }

    public boolean canReactToComment(Principal principal, Long commentId) {
        User loggedUser = userService.findByUsername( ((UserDetails)principal).getUsername());
        Post foundPost = postService.findByCommentId(commentId);

        if (foundPost == null || loggedUser == null ||
                reactionService.existsForComment(commentId, loggedUser) ||
                !canUserParttake(foundPost.getId(), principal) )
        {
            return false;
        }
        return true;
    }

    public boolean canReportPost(Principal principal, Long postId) {
        if (!canUserParttake(postId, principal)) {
            return false;
        }
        return true;
    }

    public boolean canReportComment(Principal principal, Long commentId) {
        Post associatedPost = postService.findByCommentId(commentId);
        if (associatedPost == null || !canUserParttake(associatedPost.getId(), principal)) {
            return false;
        }
        return true;
    }

    public boolean canChangeReport(Principal principal, Long reportId) {
        User loggedUser = userService.findByUsername( ((UserDetails)principal).getUsername());
        if (loggedUser == null)
            return false;

        Report targetedReport = reportService.findById(reportId);
        Post associatedPost;
        if (targetedReport.getForComment() != null) {
            associatedPost = targetedReport.getForComment().getBelongsToPost();
        }else {
            associatedPost = targetedReport.getForPost();
        }

        if (!canUserParttake(associatedPost.getId(), principal) || !reportService.isAuthor(reportId, loggedUser.getId()))
            return false;
        return true;
    }

    public boolean canAcceptReports(Principal principal, Long reportId) {

        Report targetedReport = reportService.findById(reportId);
        Post associatedPost;

        if (targetedReport.getForComment() != null) {
            associatedPost = targetedReport.getForComment().getBelongsToPost();
        }else {
            associatedPost = targetedReport.getForPost();
        }

        return moderatesCommunity(associatedPost.getCommunity().getId(), principal);
    }
}
