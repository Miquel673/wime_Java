package com.example.Wime_java.Auth;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Component
public class OAuthSessionStore {

    public void save(HttpSession session, String accessToken, String refreshToken, Long expiresIn) {
        session.setAttribute("google_access_token", accessToken);
        session.setAttribute("google_refresh_token", refreshToken);
        session.setAttribute("google_expires_in", expiresIn);
    }

    public String getAccessToken(HttpSession session) {
        return (String) session.getAttribute("google_access_token");
    }

    public String getRefreshToken(HttpSession session) {
        return (String) session.getAttribute("google_refresh_token");
    }

    public void updateAccessToken(HttpSession session, String newAccessToken) {
    session.setAttribute("google_access_token", newAccessToken);
}

}
