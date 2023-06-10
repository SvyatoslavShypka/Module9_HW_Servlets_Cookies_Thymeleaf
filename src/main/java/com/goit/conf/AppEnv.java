package com.goit.conf;

import java.util.Properties;

import static com.goit.util.Constants.DEFAULT_APP_FILE_NAME;


public class AppEnv {

    private final Properties properties;

    public AppEnv(Properties properties) {
        this.properties = properties;
    }

    public static AppEnv load() {
        try {
            Properties properties = new Properties();
            properties.load(AppEnv.class.getClassLoader().getResourceAsStream(DEFAULT_APP_FILE_NAME));
            return new AppEnv(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getObject(String key) {
        return properties.get(key);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
