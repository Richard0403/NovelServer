package cc.ifinder.novel.utils;

import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.domain.user.repository.RepositoryUser;
import cc.ifinder.novel.security.demain.bean.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Richard on
 * 2017/11/30
 * desc:
 */
@Component
public class TokenUtil {
    private static final int expirationSeconds = 60*60*24*7;//unit===seconds
    private static final String secret = "richard";
    private static final String CLAIM_KEY_USERNAME = "user_name";
    private static final String CLAIM_KEY_CREATED = "create_date";

    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    RepositoryUser repositoryUser;


    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret) //采用什么算法是可以自己选择的，不一定非要采用HS512
                .compact();
    }
    Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public String generateToken(String userName, UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        String token = generateToken(claims);
        User user = repositoryUser.findByUniqueName(userName);//uniqueName作为唯一键值
        user.setToken(token);
        repositoryUser.save(user);
        return token;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expirationSeconds * 1000);
    }

    public String getUsernameFromToken(String authToken) {
        Claims claims = getClaimsFromToken(authToken);
        if(claims!=null){
            String userName = (String) claims.get(CLAIM_KEY_USERNAME);
            return userName;
        }else{
            return null;
        }
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public boolean validateToken(String authToken, UserDetails userDetails) {
        JwtUser jwtUer = (JwtUser) userDetails;
        User user = repositoryUser.findByUniqueName(jwtUer.getUsername());
        if(user != null && authToken.equals(user.getToken())){
            final String username = getUsernameFromToken(authToken);
            final Date created = getCreatedDateFromToken(authToken);
            return (
                    username.equals(jwtUer.getUsername())
                            && !isTokenExpired(authToken)
                            && !isCreatedBeforeLastPasswordReset(created, jwtUer.getLastPasswordResetDate()));
        }else {
            return false;
        }

    }

    public User getUserFromToken(String token){
        String userName = getUsernameFromToken(token);
        return repositoryUser.findByUniqueName(userName);
    }

    public User getUserIdFromHttpReq(HttpServletRequest http){
        String bearToken = http.getHeader(tokenHeader);
        if(!StringUtils.isEmpty(bearToken)){
            String token = bearToken.replace(tokenHead,"");
            User user = getUserFromToken(token);
            return user;
        }
        return null;
    }
}
