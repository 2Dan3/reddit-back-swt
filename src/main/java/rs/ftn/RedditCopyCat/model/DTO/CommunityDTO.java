package rs.ftn.RedditCopyCat.model.DTO;

import lombok.Data;
import rs.ftn.RedditCopyCat.model.Community;

import java.time.LocalDate;

@Data
public class CommunityDTO {

    private String name;
    private String description;
    private LocalDate creationDate;

    public CommunityDTO(Community comm) {
       this.name = comm.getName();
       this.description = comm.getDescription();
       this.creationDate = comm.getCreationDate();
    }

//    public CommunityDTO(String name, LocalDate creationDate) {
//        this.name = name;
//        this.creationDate = creationDate;
//    }
}
