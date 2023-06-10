package com.goit.thymeleaf;

import com.goit.conf.AppEnv;
import com.goit.conf.FlywayConfiguration;
import com.goit.conf.LoggingConfiguration;
import com.goit.conf.ThymeleafServletConfiguration;
import com.goit.thymeleaf.exception.ThymeleafAppInternalException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;

import java.net.MalformedURLException;
import java.net.URL;

import static com.goit.util.Constants.BASE_URL;
import static com.goit.util.Constants.HOST_PORT;
import static com.goit.util.Constants.PROTOCOL;
import static com.goit.util.Constants.THYMELEAF;

@Slf4j
public class ThymeleafAppServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) {
        AppEnv env = AppEnv.load();
        new LoggingConfiguration().setup();
        try {
            String baseUrl = getBaseUrl(env);
            new FlywayConfiguration().setup().migrate();
            log.info("INIT ThymeleafAppServlet");
            log.info("Started configuration init");
            TemplateEngine engine = ThymeleafServletConfiguration.setup(config);
            config.getServletContext().setAttribute(THYMELEAF, engine);
            config.getServletContext().setAttribute(BASE_URL, baseUrl);
        } catch (Exception e) {
            log.error("INIT ThymeleafAppServlet failed", e);
            throw new ThymeleafAppInternalException("Configuration setup failed", e);
        }
        log.info("Configuration init finished");
    }

    private static String getBaseUrl(AppEnv env) {
        try {
            String host = env.getProperty(HOST_PORT);
            String protocol = env.getProperty(PROTOCOL);
            URL url = new URL(protocol + "://" + host);
            return url.toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
