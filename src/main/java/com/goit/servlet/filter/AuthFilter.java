package com.goit.servlet.filter;

import com.goit.conf.FlywayConfiguration;
import com.goit.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Properties;

import static com.goit.servlet.ServletUtils.sendNotAuthorized;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

//@WebFilter(value = "/api/*")
public class AuthFilter extends HttpFilter {
    private static final String DEFAULT_FILE_NAME = "application.properties";

    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_COOKIE = "auth";

    private String token;

    @Override
    public void init() {
        try {
            Properties properties = new Properties();
            properties.load(FlywayConfiguration.class.getClassLoader().getResourceAsStream(DEFAULT_FILE_NAME));
            token = properties.getProperty(Constants.TOKEN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String auth = req.getHeader(AUTH_HEADER);
        String authCookie = findAuthCookie(req);
        String requestToken = nonNull(auth)
                ? auth
                : authCookie;
        if (isNull(requestToken)) {
            sendNotAuthorized(res);
        } else if (requestToken.equals(token)) {
            HttpSession session = req.getSession(true);
            final int seconds = 60;
            session.setMaxInactiveInterval(seconds);
            boolean isNew = session.isNew();
            String id = session.getId();

            chain.doFilter(req, res);
        } else {
            sendNotAuthorized(res);
        }
    }

    private String findAuthCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(AUTH_COOKIE)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
