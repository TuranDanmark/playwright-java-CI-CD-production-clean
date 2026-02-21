package com.elvira.core.allure;

import com.elvira.core.config.Environment;

import java.io.File;
import java.io.PrintWriter;

public class AllureEnvironmentWriter {

    public static void write() {

        try {

            File dir = new File("target/allure-results");
            dir.mkdirs();

            File file = new File(dir, "environment.properties");

            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Browser=" + System.getProperty("browser", "chromium"));
                writer.println("Headless=" + System.getProperty("headless", "true"));
                writer.println("Environment=" + Environment.current().name());
                writer.println("OS=" + System.getProperty("os.name"));
                writer.println("Java=" + System.getProperty("java.version"));
            }

        } catch (Exception ignored) {}
    }
}
