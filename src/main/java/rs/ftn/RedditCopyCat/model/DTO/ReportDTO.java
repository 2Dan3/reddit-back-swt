package rs.ftn.RedditCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import rs.ftn.RedditCopyCat.model.entity.Report;
import rs.ftn.RedditCopyCat.model.enums.ReportReason;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ReportDTO {
    private Long id;
    @NotBlank
    private ReportReason reportReason;
    private LocalDate timestamp;
    private boolean isAccepted;
//    @NotBlank
    private String authorDisplayName;

    public ReportDTO(Report r) {
        this.id = r.getId();
        this.reportReason = r.getReason();
        this.timestamp = r.getTimestamp();
        this.authorDisplayName = r.getByUser().getDisplayName();
        this.isAccepted = r.isAccepted();
    }
}
