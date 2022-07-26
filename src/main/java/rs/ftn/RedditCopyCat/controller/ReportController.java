package rs.ftn.RedditCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ftn.RedditCopyCat.model.DTO.ReportDTO;
import rs.ftn.RedditCopyCat.model.entity.Comment;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.model.entity.Report;
import rs.ftn.RedditCopyCat.service.CommentService;
import rs.ftn.RedditCopyCat.service.CommunityService;
import rs.ftn.RedditCopyCat.service.PostService;
import rs.ftn.RedditCopyCat.service.ReportService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "reddit")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @GetMapping(value = "/communities/{communityId}/reports")
    public ResponseEntity<List<ReportDTO>> getReportsInCommunity(@PathVariable Long communityId, @RequestParam(name = "for", defaultValue = "posts") String subjectOfReports) {

        if (!reportService.validQueryParams(subjectOfReports))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        Community community = communityService.findById(communityId);
        if (community == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        Set<Report> existingReports = reportService.findInCommunity(community, subjectOfReports);

        List<ReportDTO> reportsDTO = new ArrayList<>();
        for (Report r : existingReports) {
            reportsDTO.add(new ReportDTO(r));
        }

        return new ResponseEntity<>(reportsDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/posts/{postId}/reports")
    public ResponseEntity<ReportDTO> makeReportToPost(@PathVariable Long postId, @RequestBody @Validated ReportDTO receivedReport) {
        Post foundPost = postService.findById(postId);
        if (foundPost == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Report newReport = new Report();
        newReport.setAccepted(false);
        newReport.setReason(receivedReport.getReportReason());
        newReport.setForPost(foundPost);
        newReport.setTimestamp(LocalDate.now());
//      TODO*: findLoggedUser from JWT and confirm isLoggedIn()
//       newReport.setByUser(loggedUser);

        newReport = reportService.save(newReport);
        return new ResponseEntity<>(new ReportDTO(newReport), HttpStatus.CREATED);
    }

    @PostMapping(value = "/comments/{commentId}/reports")
    public ResponseEntity<ReportDTO> makeReportToComment(@PathVariable Long commentId, @RequestBody @Validated ReportDTO receivedReport) {
        Comment foundComment = commentService.findById(commentId);
        if (foundComment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Report newReport = new Report();
        newReport.setAccepted(false);
        newReport.setReason(receivedReport.getReportReason());
        newReport.setForComment(foundComment);
        newReport.setTimestamp(LocalDate.now());
//      TODO*: findLoggedUser from JWT and confirm isLoggedIn()
//       newReport.setByUser(loggedUser);

        newReport = reportService.save(newReport);
        return new ResponseEntity<>(new ReportDTO(newReport), HttpStatus.CREATED);
    }

    @PutMapping(value = "/reports/{reportId}")
    public ResponseEntity<ReportDTO> editOwnReport(@PathVariable Long reportId, @RequestBody @Validated ReportDTO receivedReport) {
        Report existingReport = reportService.findById(reportId);
        if (existingReport == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        existingReport.setReason(receivedReport.getReportReason());
        existingReport.setTimestamp(LocalDate.now());

        existingReport = reportService.save(existingReport);
        return new ResponseEntity<>(new ReportDTO(existingReport), HttpStatus.OK);
    }

    @PutMapping(value = "/reports/{reportId}")
    public ResponseEntity<Void> acceptReport(@PathVariable Long reportId) {
        Report existingReport = reportService.findById(reportId);
        if (existingReport == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        reportService.acceptReportReason(existingReport);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
