package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ftn.RedditCopyCat.model.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
