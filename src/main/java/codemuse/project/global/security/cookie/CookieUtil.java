package codemuse.project.global.security.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;


@Component
public class CookieUtil {

    private static final String JWT_COOKIE = "JWT_TOKEN";

    public void create(HttpServletResponse response, String accessToken){
        Cookie cookie = new Cookie(JWT_COOKIE, accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }

    public String getToken(HttpServletRequest req) {
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if (JWT_COOKIE.equals(c.getName())) return c.getValue();
            }
        }
        return null;
    }

    public void clear(HttpServletResponse res) {
        Cookie cookie = new Cookie(JWT_COOKIE, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
    }
}
