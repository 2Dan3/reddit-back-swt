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

    boolean changeOwnPassword(String oldPassword, String newPassword, User foundUser);

    void remove(User foundUser);

    boolean isLoggedUser(User subjectUser);

    void banUserFromCommunity(Community community, User userBeingBanned, User moderator);

    void unbanUserFromCommunity(Community community, User userBeingUnBanned);

    boolean moderatesCommunity(Long communityId, User moderator);
//    TODO: use for checks in Web's configure()
    boolean isUserBanned(User user, Community community);
}
