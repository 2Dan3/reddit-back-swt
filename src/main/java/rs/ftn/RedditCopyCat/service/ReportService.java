package rs.ftn.RedditCopyCat.service;

import org.springframework.data.jpa.repository.Query;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.model.entity.Report;

import java.util.Set;

public interface ReportService {

    void deleteAllForCommentsToPost(Post targetedPost);

    void deleteAllForPost(Post targetedPost);

    Set<Report> findInCommunity(Community community, String subjectOfReports);

    boolean validQueryParams(String subjectOfReports);

    Report save(Report newReport);

    Report findById(Long reportId);

    void acceptReportReason(Report existingReport);

    boolean isAuthor(Long reportId, Long userId);
}
