package rs.ftn.RedditCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ftn.RedditCopyCat.model.DTO.JwtAuthenticationRequest;
import rs.ftn.RedditCopyCat.model.DTO.UserDTO;
import rs.ftn.RedditCopyCat.model.DTO.UserTokenState;
import rs.ftn.RedditCopyCat.model.entity.Community;
import rs.ftn.RedditCopyCat.model.entity.User;
import rs.ftn.RedditCopyCat.security.TokenUtils;
import rs.ftn.RedditCopyCat.service.CommunityService;
import rs.ftn.RedditCopyCat.service.UserService;
import rs.ftn.RedditCopyCat.service.implementation.UserServiceImpl;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("reddit/users")
public class UserController {

    private CommunityService communityService;
    private UserService userService;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private TokenUtils tokenUtils;

    @Autowired
    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService, TokenUtils tokenUtils){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping()
    public ResponseEntity<UserDTO> registerNewUser(@RequestBody  @Validated UserDTO newUser){

        User createdUser = userService.createUser(newUser);

        if(createdUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO userDTO = new UserDTO(createdUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody @Validated JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.userService.findAll();
    }

    @GetMapping("/whoami")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        User foundUser = this.userService.findById(id);

        if (foundUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserDTO>(new UserDTO(foundUser), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN'")                                             // TODO: check HashMap - it contains old & new password
    public ResponseEntity<Void> changeOwnPassword(@PathVariable Long id, @RequestBody @NotBlank HashMap<String, String> passwords) {
//        if (password == null || "".equals(password.trim()))
        if (passwords == null || passwords.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User subjectUser = this.userService.findById(id);

        if (subjectUser == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!userService.isLoggedUser(subjectUser))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        boolean passwordChangeSuccessful = userService.changeOwnPassword(passwords.get("oldPass"), passwords.get("newPass"), subjectUser);
        if (!passwordChangeSuccessful)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN'")
    public ResponseEntity<UserDTO> changeOwnData(@RequestBody @Validated UserDTO newData) {
        User foundUser = this.userService.findById(newData.getId());

        if (foundUser == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        userService.changeOwnData(newData, foundUser);
        return new ResponseEntity<>(new UserDTO(foundUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN'")
    public ResponseEntity<String> removeUser(@PathVariable Long id) {
        User foundUser = userService.findById(id);
        if(foundUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        userService.remove(foundUser);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/ban")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> banUserFromCommunity(@RequestParam Long communityId, @RequestParam Long userBeingBannedId, Principal principal) {

//        TODO*: check this line, does it return the right logged/auth'd user ?
        User moderator = userService.findByUsername( ((UserDetails)principal).getUsername() );
        if (!userService.moderatesCommunity(communityId, moderator) ) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User userBeingBanned = userService.findById(userBeingBannedId);
        Community community = communityService.findById(communityId);
        if(userBeingBanned == null || community == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.banUserFromCommunity(community, userBeingBanned, moderator);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/unban")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> unbanUserFromCommunity(@RequestParam Long communityId, @RequestParam Long userBeingUnBannedId, Principal principal) {

    //        TODO*: check this line, does it return the right logged/auth'd user ?
        User moderator = userService.findByUsername( ((UserDetails)principal).getUsername() );
        if (!userService.moderatesCommunity(communityId, moderator) ) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User userBeingUnBanned = userService.findById(userBeingUnBannedId);
        Community community = communityService.findById(communityId);
        if(userBeingUnBanned == null || community == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.unbanUserFromCommunity(community, userBeingUnBanned);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> invalidateToken() {
        // TODO:
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
