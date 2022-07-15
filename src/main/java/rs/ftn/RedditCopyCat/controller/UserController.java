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
import rs.ftn.RedditCopyCat.model.entity.User;
import rs.ftn.RedditCopyCat.security.TokenUtils;
import rs.ftn.RedditCopyCat.service.CommunityService;
import rs.ftn.RedditCopyCat.service.UserService;
import rs.ftn.RedditCopyCat.service.implementation.UserServiceImpl;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("reddit/users")
public class UserController {

    CommunityService communityService;
    UserService userService;
    UserDetailsService userDetailsService;
    AuthenticationManager authenticationManager;
    TokenUtils tokenUtils;

    @Autowired
    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService, TokenUtils tokenUtils){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping()                                          /*@Validated*/
    public ResponseEntity<UserDTO> registerNewUser(@RequestBody  UserDTO newUser){

        User createdUser = userService.createUser(newUser);

        if(createdUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO userDTO = new UserDTO(createdUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

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

    // TODO: PUT Password (Encoding)

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN'")                                 /*@NotBlank*/
    public ResponseEntity<String> changeOwnPassword(@PathVariable Long id, @RequestBody String password) {
        User subjectUser = this.userService.findById(id);

        if (subjectUser == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        if (!userService.isLoggedUser(subjectUser))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        // TODO: premestiti u Service

        userService.changeOwnPassword(password, subjectUser);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN'")                                 /*@NotBlank*/
    public ResponseEntity<UserDTO> changeOwnData(@RequestBody UserDTO newData, @PathVariable Long id) {
        User foundUser = this.userService.findById(id);

        if (foundUser == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        userService.changeOwnData(newData, foundUser);
        return new ResponseEntity<UserDTO>(new UserDTO(foundUser), HttpStatus.OK);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> banUserFromCommunity(@RequestParam Long communityId, @RequestParam Long userBeingBannedId, Principal principal) {
        User moderator = userService.findByUsername( ((UserDetails)principal).getUsername() );
        if (!userService.moderatesCommunity(communityId, moderator) ) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(userService.findById(userBeingBannedId) == null || communityService.findById(communityId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.banUserFromCommunity(communityId, userBeingBannedId, moderator.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> invalidateToken() {
        // TODO:
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
