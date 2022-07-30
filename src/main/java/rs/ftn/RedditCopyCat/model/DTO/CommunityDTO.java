package rs.ftn.RedditCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import rs.ftn.RedditCopyCat.model.entity.Community;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class CommunityDTO {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private LocalDate creationDate;
    private boolean isSuspended;
    private String suspensionReason;

    public CommunityDTO(Community comm) {
       this.id = comm.getId();
       this.name = comm.getName();
       this.description = comm.getDescription();
       this.creationDate = comm.getCreationDate();
       this.isSuspended = comm.isSuspended();
       this.suspensionReason = comm.getSuspensionReason();
    }

    public CommunityDTO(String name, LocalDate creationDate) {
        this.name = name;
        this.creationDate = creationDate;
    }

    public CommunityDTO(String suspensionReason) {
        this.suspensionReason = suspensionReason;
    }
}
