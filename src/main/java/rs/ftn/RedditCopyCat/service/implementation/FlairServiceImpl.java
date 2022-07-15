package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import rs.ftn.RedditCopyCat.model.entity.Flair;
import rs.ftn.RedditCopyCat.repository.FlairRepository;
import rs.ftn.RedditCopyCat.service.FlairService;

public class FlairServiceImpl implements FlairService {

    @Autowired
    FlairRepository flairRepository;

    @Override
    public Flair findByName(String flairName) {
        return flairRepository.findByName(flairName);
    }
}
