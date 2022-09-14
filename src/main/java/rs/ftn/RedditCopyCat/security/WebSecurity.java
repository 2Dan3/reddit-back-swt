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

    public boolean moderatesCommunity(Authentication authentication, HttpServletRequest request, Long communityId) {
        if (authentication == null)
            return false;
        User moderator = userService.findByUsername(authentication.getName());
        if (moderator == null)
            return false;
        if (!userService.moderatesCommunity(communityId, moderator))
            return false;
        return true;
    }

    public boolean moderatesCommunity(Authentication authentication, HttpServletRequest request, CommunityDTO communityDTO) {
        if (authentication == null)
            return false;
        User moderator = userService.findByUsername(authentication.getName() );
        if (moderator == null)
            return false;
        if (!userService.moderatesCommunity(communityDTO.getId(), moderator))
            return false;
        return true;
    }

    public boolean isUserLogged(Authentication authentication, HttpServletRequest request) {
        if ( authentication == null)
            return false;
        return true;
    }

    public boolean canChangePost(Authentication authentication, HttpServletRequest request, Long postId) {
        if (authentication == null)
            return false;
        User loggedUser = userService.findByUsername(authentication.getName() );
        if (loggedUser == null)
            return false;

        if (!canUserParttake(authentication, request, postId) || !postService.isAuthor(postId, loggedUser.getId()))
            return false;
        return true;
    }

    public boolean canChangeComment(Authentication authentication, HttpServletRequest request, Long postId, Long commentId) {
        if (authentication == null)
            return false;
        User loggedUser = userService.findByUsername(authentication.getName());
        if (loggedUser == null)
            return false;

        if (!canUserParttake(authentication, request, postId) || !commentService.isAuthor(commentId, loggedUser.getId()))
            return false;
        return true;
    }

    public boolean canUserParttake(Authentication authentication, HttpServletRequest request, Long postId) {
        // This method checks that the given User is
        // authenticated & not banned from associated Community
        if (authentication == null)
            return false;
        User loggedUser = userService.findByUsername(authentication.getName());
        if (loggedUser == null)
            return false;

        Post foundPost = postService.findById(postId);
        if (foundPost == null || userService.isUserBanned(loggedUser, foundPost.getCommunity()))
            return false;
        return true;
    }

    public boolean canChangeReaction(Authentication authentication, HttpServletRequest request, Long reactionId) {
        if (authentication == null)
            return false;
        User loggedUser = userService.findByUsername(authentication.getName());
        if (loggedUser == null)
            return false;

        Post foundPost = postService.findByReactionId(reactionId);

        if (foundPost == null || !canUserParttake(authentication, request, foundPost.getId()) || !reactionService.isAuthor(reactionId, loggedUser.getId()))
            return false;
        return true;
    }

    public boolean canReactToPost(Authentication authentication, HttpServletRequest request, Long postId) {
        if (authentication == null)
            return false;
        User loggedUser = userService.findByUsername(authentication.getName());

        if (loggedUser == null || reactionService.existsForPost(postId, loggedUser) ||
                !canUserParttake(authentication, request, postId) )
        {
            return false;
        }
        return true;
    }

    public boolean canReactToComment(Authentication authentication, HttpServletRequest request, Long commentId) {
        if (authentication == null)
            return false;
        User loggedUser = userService.findByUsername(authentication.getName());
        Post foundPost = postService.findByCommentId(commentId);

        if (foundPost == null || loggedUser == null ||
                reactionService.existsForComment(commentId, loggedUser) ||
                !canUserParttake(authentication, request, foundPost.getId()) )
        {
            return false;
        }
        return true;
    }

    public boolean canReportPost(Authentication authentication, HttpServletRequest request, Long postId) {
        if (!canUserParttake(authentication, request, postId)) {
            return false;
        }
        return true;
    }

    public boolean canReportComment(Authentication authentication, HttpServletRequest request, Long commentId) {
        Post associatedPost = postService.findByCommentId(commentId);
        if (associatedPost == null || !canUserParttake(authentication, request, associatedPost.getId()) ) {
            return false;
        }
        return true;
    }

    public boolean canChangeReport(Authentication authentication, HttpServletRequest request, Long reportId) {
        if (authentication == null)
            return false;
        User loggedUser = userService.findByUsername(authentication.getName());
        if (loggedUser == null)
            return false;

        Report targetedReport = reportService.findById(reportId);
        Post associatedPost;
        if (targetedReport.getForComment() != null) {
            associatedPost = targetedReport.getForComment().getBelongsToPost();
        }else {
            associatedPost = targetedReport.getForPost();
        }

        if (!canUserParttake(authentication, request, associatedPost.getId()) || !reportService.isAuthor(reportId, loggedUser.getId()))
            return false;
        return true;
    }

    public boolean canAcceptReports(Authentication authentication, HttpServletRequest request, Long reportId) {

        Report targetedReport = reportService.findById(reportId);
        Post associatedPost;

        if (targetedReport.getForComment() != null) {
            associatedPost = targetedReport.getForComment().getBelongsToPost();
        }else {
            associatedPost = targetedReport.getForPost();
        }

        return moderatesCommunity(authentication, request, associatedPost.getCommunity().getId());
    }
}
