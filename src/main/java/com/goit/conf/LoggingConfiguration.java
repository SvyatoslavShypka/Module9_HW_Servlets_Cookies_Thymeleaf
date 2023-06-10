package com.goit.conf;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.util.Properties;

import static com.goit.util.Constants.LOG_ENCODING;
import static com.goit.util.Constants.LOG_FILE;
import static com.goit.util.Constants.LOG_LEVEL;
import static com.goit.util.Constants.LOG_PATTERN;

public class LoggingConfiguration {
    private static final String DEFAULT_FILE_NAME = "application.properties";

    public void setup() {
        final Properties properties = new Properties();
        try {
            properties.load(LoggingConfiguration.class.getClassLoader().getResourceAsStream(DEFAULT_FILE_NAME));

            // creates pattern layout
            PatternLayout layout = new PatternLayout();
            layout.setConversionPattern(properties.getProperty(LOG_PATTERN));

            // creates console appender
            ConsoleAppender consoleAppender = new ConsoleAppender();
            consoleAppender.setLayout(layout);
            consoleAppender.setEncoding(properties.getProperty(LOG_ENCODING));
            consoleAppender.activateOptions();

            // creates file appender
            DailyRollingFileAppender rollingFileAppender = new DailyRollingFileAppender();
            rollingFileAppender.setEncoding(properties.getProperty(LOG_ENCODING));
            rollingFileAppender.setFile(properties.getProperty(LOG_FILE));
            rollingFileAppender.setLayout(layout);
            rollingFileAppender.setDatePattern("'.'yyyy-MM-dd");
            rollingFileAppender.activateOptions();

            // configures the root logger
            Logger rootLogger = Logger.getRootLogger();
            rootLogger.setLevel(Level.toLevel(properties.getProperty(LOG_LEVEL)));
            rootLogger.removeAllAppenders();
            rootLogger.addAppender(consoleAppender);
            rootLogger.addAppender(rollingFileAppender);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
