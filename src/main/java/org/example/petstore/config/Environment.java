package org.example.petstore.config;

public enum Environment {
    PETSTORE("petstore.properties"),
    // If you have more projects, add them
    OTHER("some-other-project.properties");

    private final String propertyFileName;

    Environment(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    public String getPropertyFileName() {
        return propertyFileName;
    }

    public static Environment fromString(String env) {
        for (Environment e : values()) {
            if (e.name().equalsIgnoreCase(env)) {
                return e;
            }
        }
        // default
        return PETSTORE;
    }
}
