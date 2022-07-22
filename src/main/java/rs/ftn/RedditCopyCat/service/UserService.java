package rs.ftn.RedditCopyCat.service;

import rs.ftn.RedditCopyCat.model.DTO.UserDTO;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.entity.User;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    User createUser(UserDTO userDTO);

    List<User> findAll();

    User findById(Long id);

    void changeOwnData(UserDTO newData, User currentUserData);

    void changeOwnPassword(String password, User foundUser);

    void remove(User foundUser);

    boolean isLoggedUser(User subjectUser);

    void banUserFromCommunity(Community community, User userBeingBanned, User moderator);

    boolean moderatesCommunity(Long communityId, User moderator);

    boolean isUserBanned(User user, Community community);
}