package rs.ftn.RedditCopyCat.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

    @Value("mySecretString")
    private String secret;

    @Value("3600000")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = this.getClaimsFromToken(token); // username izvlacimo iz subject polja unutar payload tokena
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(this.secret) // izvlacenje celog payloada
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expirationDate;
        try {
            final Claims claims = this.getClaimsFromToken(token); // username izvlacimo iz expiration time polja unutar payload tokena
            expirationDate = claims.getExpiration();
        } catch (Exception e) {
            expirationDate = null;
        }
        return expirationDate;
    }

    /*
     * Provera da li je token istekao tj da li nije prsvto expiration momenat*/
    private boolean isTokenExpired(String token) {
        final Date expirationDate = this.getExpirationDateFromToken(token);
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    /*Provera validnosti tokena: period vazenja i provera username-a korisnika*/
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        SimpleGrantedAuthority sga = (SimpleGrantedAuthority) userDetails.getAuthorities().toArray()[0];
        sga.getAuthority();
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
//        TODO: && if getStringRoleFromToken(token) == ( (SimpleGrantedAuthority) userDetails.getAuthorities().toArray()[0] ).getAuthority().substring(5)
    }

    /*Generisanje tokena za korisnika - postavljanje svih potrebnih informacija,
     * kao sto je rola korisnika.*/
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("sub", userDetails.getUsername());
        claims.put("role", userDetails.getAuthorities().toArray()[0]);
        claims.put("created", new Date(System.currentTimeMillis()));
        return Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public int getExpiredIn() {
        return expiration.intValue();
    }
}
