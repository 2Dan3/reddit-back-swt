package rs.ftn.RedditCopyCat.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private BannedRepository bannedRepository;
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(@Lazy PasswordEncoder passwordEnc, BannedRepository bannedRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEnc;
        this.bannedRepository = bannedRepository;
        this.userRepository = userRepository;
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

        if ( existsForUsernameOrEmail(userDTO.getUsername(), userDTO.getEmail()) ) {
            return null;
        }

        User newUser = new User();
        newUser.setEmail(userDTO.getEmail());
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRole(Roles.USER);
        newUser.setRegistrationDate(LocalDate.now());
        newUser.setDisplayName(newUser.getUsername());

        newUser = userRepository.save(newUser);

        return newUser;
    }

    private boolean existsForUsernameOrEmail(String username, String email) {
        return userRepository.findFirstByUsername(username).isPresent() ||
                userRepository.findFirstByEmail(email).isPresent();
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User findById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public boolean changeOwnData(UserDTO newData, User currentUser) {
        if (emailBelongsToUser(newData.getEmail(), currentUser.getEmail()) || !emailIsTaken(newData.getEmail()) )
        {
        currentUser.setEmail(newData.getEmail().trim());

        currentUser.setDisplayName(newData.getDisplayName());
        currentUser.setDescription(newData.getDescription());
        currentUser.setAvatar(newData.getAvatar());

        userRepository.save(currentUser);
        return true;
        }
        return false;


    }

    private boolean emailBelongsToUser(String newEmail, String existingUserEmail) {
        return existingUserEmail.equalsIgnoreCase(newEmail.trim() );
    }

    private boolean emailIsTaken(String newEmail) {
        return userRepository.findFirstByEmail(newEmail).isPresent();
    }

    @Override
    public boolean changeOwnPassword(String oldPassword, String newPassword, User foundUser) {
        if (!passwordEncoder.matches(oldPassword, foundUser.getPassword()) )
            return false;
        foundUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(foundUser);
        return true;
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
    public void unbanUserFromCommunity(Community community, User userBeingUnBanned) {
    //  TODO*: check on Iterable of *banRecords*, if it throws errors
        Iterable<Banned> banRecords = bannedRepository.findByCommunityAndUser(community.getId(), userBeingUnBanned.getId());
        if (banRecords.iterator().hasNext()) {
            bannedRepository.deleteAll(banRecords);
        }
    }

    @Override
    public boolean moderatesCommunity(Long communityId, User moderator) {
        return userRepository.findModerator(communityId, moderator.getId()) == 0?false:true;
    }

    @Override
    public boolean isUserBanned(User user, Community community) {
        return bannedRepository.existsBan(user.getId(), community.getId()) == 0?false:true;
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

}
