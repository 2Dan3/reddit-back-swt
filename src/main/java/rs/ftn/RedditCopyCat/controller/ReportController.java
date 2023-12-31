package rs.ftn.RedditCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import rs.ftn.RedditCopyCat.model.DTO.ReportDTO;
import rs.ftn.RedditCopyCat.model.entity.*;
import rs.ftn.RedditCopyCat.service.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "${apiPrefix}")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/communities/{communityId}/reports")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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

    @PostMapping(consumes = "application/json", value = "/posts/{postId}/reports")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReportDTO> makeReportToPost(Authentication authentication, @PathVariable Long postId, @Valid @RequestBody ReportDTO receivedReport) {
        Post foundPost = postService.findById(postId);
        if (foundPost == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User loggedUser = userService.findByUsername(authentication.getName());

        Report newReport = new Report();
        newReport.setAccepted(false);
        newReport.setReason(receivedReport.getReportReason());
        newReport.setForPost(foundPost);
        newReport.setTimestamp(LocalDate.now());
        newReport.setByUser(loggedUser);

        newReport = reportService.save(newReport);
        return new ResponseEntity<>(new ReportDTO(newReport), HttpStatus.CREATED);
    }

    @PostMapping(consumes = "application/json", value = "/comments/{commentId}/reports")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReportDTO> makeReportToComment(Authentication authentication, @PathVariable Long commentId, @Valid @RequestBody ReportDTO receivedReport) {
        Comment foundComment = commentService.findById(commentId);
        if (foundComment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User loggedUser = userService.findByUsername(authentication.getName());

        Report newReport = new Report();
        newReport.setAccepted(false);
        newReport.setReason(receivedReport.getReportReason());
        newReport.setForComment(foundComment);
        newReport.setTimestamp(LocalDate.now());
        newReport.setByUser(loggedUser);

        newReport = reportService.save(newReport);
        return new ResponseEntity<>(new ReportDTO(newReport), HttpStatus.CREATED);
    }

    @PutMapping(value = "/reports/{reportId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReportDTO> editOwnReport(@PathVariable Long reportId, @Valid @RequestBody ReportDTO receivedReport) {
        Report existingReport = reportService.findById(reportId);
        if (existingReport == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        existingReport.setReason(receivedReport.getReportReason());
        existingReport.setTimestamp(LocalDate.now());

        existingReport = reportService.save(existingReport);
        return new ResponseEntity<>(new ReportDTO(existingReport), HttpStatus.OK);
    }

    @PutMapping(value = "/reports/{reportId}/accept")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> acceptReport(@PathVariable Long reportId) {
        Report existingReport = reportService.findById(reportId);
        if (existingReport == null || existingReport.isAccepted())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        reportService.acceptReportReason(existingReport);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
