package rs.ftn.RedditCopyCat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.DTO.CommunityDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "reddit/communities")
public class CommunityController {

//    @Autowired
//    private CommunityService communityService;

//    @GetMapping(value = "/")
//    public ResponseEntity<List<CommunityDTO>> getAllCommunities() {
//
//        List<Community> communities = communityService.findAll();
//
//        // convert communities to DTOs
//        List<CommunityDTO> communitiesDTO = new ArrayList<>();
//        for (Community c : communities) {
//            communitiesDTO.add(new CommunityDTO(c));
//        }
//
//        return new ResponseEntity<>(communitiesDTO, HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/{id}")
//    public ResponseEntity<CommunityDTO> getCommunity(@PathVariable Integer id) {
//
//        Community community = communityService.findOne(id);
//
//        // comm must exist
//        if (community == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(new CommunityDTO(community), HttpStatus.OK);
//    }
//
//    @PostMapping(consumes = "application/json")
//    public ResponseEntity<CommunityDTO> createCommunity(@RequestBody CommunityDTO communityDTO) {
//
//        Community community = new Community();
//        community.setName(communityDTO.getName());
//        community.setDescription(communityDTO.getDescription());
//        community.setCreationDate(LocalDate.now());
//
//        community = communityService.save(community);
//        return new ResponseEntity<>(new CommunityDTO(community), HttpStatus.CREATED);
//    }
//
//    @PutMapping(consumes = "application/json")
//    public ResponseEntity<CommunityDTO> updateStudent(@RequestBody CommunityDTO communityDTO) {
//
//        // community must exist
////        TODO  findByName mozda bolje ako ne zelim ID u DTO da se mapira sa JSON ?
//        Community community = communityService.findOne(communityDTO.getId());
//
//        if (community == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            // TODO - ili HttpStatus.NOT_FOUND
//        }
//
//        community.setName(communityDTO.getName());
//        community.setDescription(communityDTO.getDescription());
//
//        community = communityService.save(community);
//        return new ResponseEntity<>(new CommunityDTO(community), HttpStatus.OK);
//    }
//
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<Void> removeCommunity(@PathVariable Integer id) {
//
//        Community community = communityService.findOne(id);
//
//        if (community != null) {
//            communityService.remove(id);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    //TODO *  !  !  !  *
//    @GetMapping(value = "/{communityId}/posts")
//    public ResponseEntity<List<PostDTO>> getCommunityPosts(@PathVariable Integer communityId) {
//
//        //traze se polozeni ispiti studenta, sto znaci da moramo uputiti JOIN FETCH upit
//        //kako bismo dobili sve trazene podatke
//        Student student = studentService.findOneWithExams(studentId);
//
//        //ako je podesen fetchType LAZY i pozovemo findOne umesto findOneWithExams,
//        //na poziv getExams bismo dobili LazyInitializationException
//        Set<Exam> exams = student.getExams();
//        List<ExamDTO> examsDTO = new ArrayList<>();
//        for (Exam e : exams) {
//            ExamDTO examDTO = new ExamDTO();
//            examDTO.setId(e.getId());
//            examDTO.setGrade(e.getGrade());
//            examDTO.setDate(e.getDate());
//            examDTO.setCourse(new CourseDTO(e.getCourse()));
//            examDTO.setStudent(new StudentDTO(e.getStudent()));
//
//            examsDTO.add(examDTO);
//        }
//        return new ResponseEntity<>(examsDTO, HttpStatus.OK);
//    }
//
//    //TODO *  !  !  !  *
//    @GetMapping(value = "/{communityId}/flairs")
//    public ResponseEntity<List<FlairDTO>> getCommunityFlairs(@PathVariable Integer communityId) {
//
//        //traze se polozeni ispiti studenta, sto znaci da moramo uputiti JOIN FETCH upit
//        //kako bismo dobili sve trazene podatke
//        Student student = studentService.findOneWithExams(studentId);
//
//        //ako je podesen fetchType LAZY i pozovemo findOne umesto findOneWithExams,
//        //na poziv getExams bismo dobili LazyInitializationException
//        Set<Exam> exams = student.getExams();
//        List<ExamDTO> examsDTO = new ArrayList<>();
//        for (Exam e : exams) {
//            ExamDTO examDTO = new ExamDTO();
//            examDTO.setId(e.getId());
//            examDTO.setGrade(e.getGrade());
//            examDTO.setDate(e.getDate());
//            examDTO.setCourse(new CourseDTO(e.getCourse()));
//            examDTO.setStudent(new StudentDTO(e.getStudent()));
//
//            examsDTO.add(examDTO);
//        }
//        return new ResponseEntity<>(examsDTO, HttpStatus.OK);
//    }
//    //TODO *  !  !  !  *
//    @GetMapping(value = "/{communityId}/rules")
//    public ResponseEntity<List<RuleDTO>> getCommunityRules(@PathVariable Integer communityId) {
//
//        //traze se polozeni ispiti studenta, sto znaci da moramo uputiti JOIN FETCH upit
//        //kako bismo dobili sve trazene podatke
//        Student student = studentService.findOneWithExams(studentId);
//
//        //ako je podesen fetchType LAZY i pozovemo findOne umesto findOneWithExams,
//        //na poziv getExams bismo dobili LazyInitializationException
//        Set<Exam> exams = student.getExams();
//        List<ExamDTO> examsDTO = new ArrayList<>();
//        for (Exam e : exams) {
//            ExamDTO examDTO = new ExamDTO();
//            examDTO.setId(e.getId());
//            examDTO.setGrade(e.getGrade());
//            examDTO.setDate(e.getDate());
//            examDTO.setCourse(new CourseDTO(e.getCourse()));
//            examDTO.setStudent(new StudentDTO(e.getStudent()));
//
//            examsDTO.add(examDTO);
//        }
//        return new ResponseEntity<>(examsDTO, HttpStatus.OK);
//    }

}
