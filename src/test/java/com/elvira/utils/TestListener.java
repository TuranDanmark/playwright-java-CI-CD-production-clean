package com.elvira.utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestListener implements TestWatcher, BeforeEachCallback, AfterEachCallback {

    private Page page;

    @Override
    public void beforeEach(ExtensionContext context) {
        Object testInstance = context.getRequiredTestInstance();

        try {
            Field field = testInstance.getClass().getSuperclass().getDeclaredField("page");
            field.setAccessible(true);
            page = (Page) field.get(testInstance);
        } catch (Exception e) {
            page = null;
        }
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {

        if (page == null) return;

        String testName = context.getDisplayName();

        try {
            // Screenshot
            byte[] screenshot = page.screenshot(
                    new Page.ScreenshotOptions().setFullPage(true)
            );

            Allure.getLifecycle().addAttachment(
                    "Screenshot",
                    "image/png",
                    "png",
                    screenshot
            );

            // Trace
            Path traceDir = Paths.get("target/traces");
            Files.createDirectories(traceDir);
            Path tracePath = traceDir.resolve(testName + ".zip");

            page.context().tracing().stop(
                    new Tracing.StopOptions().setPath(tracePath)
            );

            Allure.addAttachment(
                    "Trace",
                    "application/zip",
                    Files.newInputStream(tracePath),
                    ".zip"
            );

            // Video
            if (page.video() != null) {
                Path videoPath = page.video().path();
                Allure.addAttachment(
                        "Video",
                        "video/webm",
                        Files.newInputStream(videoPath),
                        ".webm"
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        page = null;
    }
}