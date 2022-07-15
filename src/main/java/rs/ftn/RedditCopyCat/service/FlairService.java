package rs.ftn.RedditCopyCat.service;

import rs.ftn.RedditCopyCat.model.entity.Flair;

public interface FlairService {
    Flair findByName(String flairName);
}
