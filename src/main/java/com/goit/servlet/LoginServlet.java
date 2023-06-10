package com.goit.servlet;

import com.goit.conf.FlywayConfiguration;
import com.goit.servlet.model.CredentialsModel;
import com.goit.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import static com.goit.servlet.ServletUtils.sendNotAuthorized;
import static com.goit.util.Constants.DEFAULT_APP_FILE_NAME;


//@WebServlet(value = "/api/v1/demo")
@Slf4j
public class LoginServlet extends HttpServlet {
    private Gson gson;
    private String username;
    private String password;
    private String token;

    @Override
    public void init() throws ServletException {
        log.info("INIT METHOD");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            Properties properties = new Properties();
            properties.load(FlywayConfiguration.class.getClassLoader().getResourceAsStream(DEFAULT_APP_FILE_NAME));
            username = properties.getProperty(Constants.USERNAME);
            password = properties.getProperty(Constants.PASSWORD);
            token = properties.getProperty(Constants.TOKEN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie cookie = new Cookie("test2", "test-cookie");
//        cookie.setHttpOnly(true);
        resp.addCookie(cookie);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = String.format("POST METHOD:%s", req.getRequestURI());
        log.info(message);

        byte[] bytes = req.getInputStream().readAllBytes();
        String body = new String(bytes);
        CredentialsModel credentials = gson.fromJson(body, CredentialsModel.class);
        doUserAuth(credentials, resp);
    }

    private void doUserAuth(CredentialsModel credentials, HttpServletResponse resp) throws IOException {
        if (Objects.nonNull(credentials.getUsername()) && credentials.getUsername().equals(username)) {
            if (Objects.nonNull(credentials.getPassword()) && credentials.getPassword().equals(password)) {
                Cookie cookie = new Cookie("auth", token);
                cookie.setHttpOnly(true);
//                cookie.setSecure(true);
                resp.addCookie(cookie);
            }
        } else {
            sendNotAuthorized(resp);
        }
    }

    @Override
    public void destroy() {
        log.info("DESTROY METHOD");
    }
}
