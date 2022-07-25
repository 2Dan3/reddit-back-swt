package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.RedditCopyCat.model.entity.Post;
import rs.ftn.RedditCopyCat.repository.ReportRepository;
import rs.ftn.RedditCopyCat.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    ReportRepository reportRepository;


    @Override
    public void deleteAllForCommentsToPost(Post targetedPost) {
        reportRepository.deleteAllForCommentsToPost(targetedPost.getId());
    }

    @Override
    public void deleteAllForPost(Post targetedPost) {
        reportRepository.deleteAllForPost(targetedPost.getId());
    }
}
