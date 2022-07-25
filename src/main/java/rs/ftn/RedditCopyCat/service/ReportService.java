package rs.ftn.RedditCopyCat.service;

import org.springframework.data.jpa.repository.Query;
import rs.ftn.RedditCopyCat.model.entity.Post;

public interface ReportService {

    void deleteAllForCommentsToPost(Post targetedPost);

    void deleteAllForPost(Post targetedPost);
}
