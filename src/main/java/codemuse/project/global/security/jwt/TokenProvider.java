package codemuse.project.global.security.jwt;

import codemuse.project.global.security.spring.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    private SecretKey key;
    private final CustomUserDetailsService customUserDetailsService;

    @PostConstruct
    public void init(){
        byte[] bytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(bytes);
    }
    public String createAccessToken(Authentication authentication){
        Date now = new Date();
        String accountId = authentication.getName();

        List<String> role = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(accountId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime()+accessTokenExpiration))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public String createRefreshToken(Authentication authentication){
        Date now = new Date();
        String accountId = authentication.getName();

        List<String> role = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(accountId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime()+refreshTokenExpiration))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token){
        if (!StringUtils.hasText(token)) {
            log.info("JWT 토큰이 없습니다.");
            return false;
        }

        try{
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch(SecurityException | MalformedJwtException e){
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.");
        }catch (UnsupportedJwtException e) {
            // SignatureException, MalformedJwtException, UnsupportedJwtException 등 모두 여기서 잡힘
            log.warn("유효하지 않은 JWT 토큰입니다.", e);
        }
        catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }

    public Authentication getAuthentication(String token){
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.getSubject());

        List<?> role = (List<?>)claims.get("role");
        List<SimpleGrantedAuthority> auths = role.stream().map(Object::toString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, token, auths);
    }
}
