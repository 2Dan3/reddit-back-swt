package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ftn.RedditCopyCat.model.DTO.UserDTO;
import rs.ftn.RedditCopyCat.model.entity.Banned;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.entity.User;
import rs.ftn.RedditCopyCat.model.enums.Roles;
import rs.ftn.RedditCopyCat.repository.BannedRepository;
import rs.ftn.RedditCopyCat.repository.UserRepository;
import rs.ftn.RedditCopyCat.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private BannedRepository bannedRepository;
    private UserRepository userRepository;
//    @Autowired
//    private Principal principal;

    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(/*PasswordEncoder passwordEnc, */BannedRepository bannedRepository, UserRepository userRepository) {
//        this.passwordEncoder = passwordEnc;
        this.bannedRepository = bannedRepository;
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findFirstByUsername(username);
        if (!user.isEmpty()) {
            return user.get();
        }
        return null;
    }

    @Override
    public User createUser(UserDTO userDTO) {

        Optional<User> user = userRepository.findFirstByUsername(userDTO.getUsername());

        if(user.isPresent()){
            return null;
        }

        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRole(Roles.USER);
        newUser = userRepository.save(newUser);

        return newUser;
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    // TODO vraca li User, ili prvo ide preko UserDetails ?

//    @Override
//    public User findById(Long id) {
//        userRepository.findById(id);
//        return null;
//    }

    public User findById(Long id) {
        return this.userRepository.findById(id).orElse(null);
//        if (foundUser.isPresent()) {
//            return foundUser.get();
//        }
//        return null;
    }

    @Override
    public void changeOwnData(UserDTO newData, User currentUser) {
        currentUser.setDisplayName(newData.getDisplayName());
        currentUser.setDescription(newData.getDescription());
        currentUser.setAvatar(newData.getAvatar());
        userRepository.save(currentUser);
    }

    @Override
    public void changeOwnPassword(String password, User foundUser) {
        foundUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(foundUser);
    }

    @Override
    public void remove(User foundUser) {
        userRepository.delete(foundUser);
    }

    @Override
    public boolean isLoggedUser(User subjectUser) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String usernameOfLogged;

        if (principal instanceof UserDetails) {
            usernameOfLogged = ((UserDetails)principal).getUsername();
        } else {
            usernameOfLogged = principal.toString();
        }

        if (subjectUser.getUsername().equals(usernameOfLogged))
            return true;
        return false;
    }

    @Override
    public void banUserFromCommunity(Community community, User userBeingBanned, User moderator) {
        bannedRepository.save(new Banned(moderator, userBeingBanned, community));
    }

    @Override
    public boolean moderatesCommunity(Long communityId, User moderator) {
        return userRepository.findModerator(communityId, moderator.getId()) == 0?false:true;
    }

    @Override
    public boolean isUserBanned(User user, Community community) {
        return bannedRepository.existsBan(user.getId(), community.getId()) == 0?false:true;
    }

}
