package org.example.petstore.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties = new Properties();

    private ConfigManager() {
        String projectName = System.getProperty("projectName", "petstore");
        Environment environment = Environment.fromString(projectName.toUpperCase());

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(environment.getPropertyFileName())) {
            if (input == null) {
                throw new RuntimeException("Property file not found for environment: " + environment);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + e.getMessage(), e);
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public String getAuthToken() {
        return getProperty("auth.token");
    }
}
