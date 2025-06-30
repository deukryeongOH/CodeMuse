package codemuse.project.global.security.jwt;

import codemuse.project.global.security.cookie.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = cookieUtil.getToken(request);
        try {
            if (StringUtils.hasText(accessToken) || tokenProvider.validateToken(accessToken)) {
                Authentication auth = tokenProvider.getAuthentication(accessToken);
//
//                UserDetails userDetails = userDetailsService.loadUserByUsername(accountId);
//
//                UsernamePasswordAuthenticationToken auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//
//                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (ExpiredJwtException eje) {
            // 1) 로그아웃 처리: 쿠키 삭제 후 로그인 페이지로
            cookieUtil.clear(response);
            response.sendRedirect("/login?expired");
            return;
            // 2) 또는 리프레시 토큰 검증 후 Access 토큰 재발급 로직 실행...
        }catch (UsernameNotFoundException ex) {
            // DB에 없으면 인증 실패 처리: SecurityContext는 비워두고 체인만 계속
            logger.warn("인증 실패: 사용자 '{}' 없음");
        }
        filterChain.doFilter(request,response);
    }

}
