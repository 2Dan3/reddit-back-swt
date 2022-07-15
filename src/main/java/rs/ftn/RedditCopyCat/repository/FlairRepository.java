package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ftn.RedditCopyCat.model.entity.Flair;

@Repository
public interface FlairRepository extends JpaRepository<Flair, Long> {
    // TODO check
    Flair findByName(String flairName);
}
