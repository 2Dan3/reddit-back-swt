package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.model.entity.Report;
import rs.ftn.RedditCopyCat.repository.ReportRepository;
import rs.ftn.RedditCopyCat.service.CommentService;
import rs.ftn.RedditCopyCat.service.PostService;
import rs.ftn.RedditCopyCat.service.ReportService;

import java.util.Set;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;


    @Override
    public void deleteAllForCommentsToPost(Post targetedPost) {
        reportRepository.deleteAllForCommentsToPost(targetedPost.getId());
    }

    @Override
    public void deleteAllForPost(Post targetedPost) {
        reportRepository.deleteAllForPost(targetedPost.getId());
    }

    @Override
    public Set<Report> findInCommunity(Community community, String subjectOfReports) {
        if (subjectOfReports.equalsIgnoreCase("comments")){
            return reportRepository.findForCommentsInCommunity(community.getId());
        } else {
            // subjectOfReports is by default "posts"
            return reportRepository.findForPostsInCommunity(community.getId());
        }
    }

    @Override
    public boolean validQueryParams(String subjectOfReports) {
        return subjectOfReports.equalsIgnoreCase("posts") || subjectOfReports.equalsIgnoreCase("comments");
    }

    @Override
    public Report save(Report newReport) {
        return reportRepository.save(newReport);
    }

    @Override
    public Report findById(Long reportId) {
        return reportRepository.findById(reportId).orElse(null);
    }

    @Override
    public void acceptReportReason(Report existingReport) {
        existingReport.setAccepted(true);

        Comment associatedComment = existingReport.getForComment();
        if (associatedComment != null)
            commentService.deleteVisibility(existingReport.getForComment());
        else
            postService.delete(existingReport.getForPost());
    }

    @Override
    public boolean isAuthor(Long reportId, Long userId) {
        return reportRepository.countAuthor(reportId, userId)==0?false:true;
    }

}
