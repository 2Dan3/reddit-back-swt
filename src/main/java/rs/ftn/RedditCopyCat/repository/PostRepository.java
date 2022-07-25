package rs.ftn.RedditCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ftn.RedditCopyCat.model.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(nativeQuery = true, value =
            "select p " +
            "from post p " +
            "where p.community_id = :communityId " +
            "order by p.creation_date " +
            ":sortDir")
    List<Post> findPostsSortedByDate(@Param("communityId") Long communityId,
                                     @Param("sortDir") String sortDir);


    @Query(nativeQuery = true, value =
            "select p " +
            "from post p " +
            "where p.community_id = :communityId " +
            "order by " +
                "(select count(r.reaction_id) " +
                "from reaction r " +
                "where " +
                    "r.to_post_post_id = p.post_id " +
                    "and r.type = ':criteria') " +
                ":sortDir")
    List<Post> findPostsSortedByPopularity(@Param("communityId") Long communityId,
                                           @Param("sortDir") String sortDir,
                                           @Param("criteria") String criteria);

    
    @Query(nativeQuery = true, value =
            "select p " +
            "from post p " +
            "where p.community_id = :communityId " +
            "order by (( " +
                "select count(r.reaction_id) " +
                "from reaction r " +
                "where " +
                    "r.to_post_post_id = p.post_id) *0.4) + " +
                    "(p.creation_date *0.6) ) " +
                ":sortDir")
    List<Post> findPostsSortedByTrending(@Param("communityId") Long communityId,
                                         @Param("sortDir") String sortDir);
}
