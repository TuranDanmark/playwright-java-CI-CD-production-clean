package com.elvira.core.base;

import com.elvira.core.allure.AllureListener;
import com.elvira.core.browser.BrowserFactory;
import com.elvira.core.config.ConfigReader;
import com.elvira.core.extension.RetryExtension;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public abstract class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    private static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    protected Page getPage() {
        return pageThreadLocal.get();
    }

    @ExtendWith({AllureListener.class, RetryExtension.class})
    @BeforeAll
    static void writeEnvironmentInfo() {
    try {
        File resultsDir = new File("target/allure-results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        File envFile = new File(resultsDir, "environment.properties");

        try (PrintWriter writer = new PrintWriter(envFile)) {
            writer.println("Browser=Chromium");
            writer.println("Headless=" + System.getProperty("headless", "false"));
            writer.println("OS=" + System.getProperty("os.name"));
            writer.println("Java=" + System.getProperty("java.version"));
            writer.println("Environment=" + System.getProperty("env", "local"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    
    }

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = BrowserFactory.createBrowser(playwright);

        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(1280, 800)
                        .setRecordVideoDir(Paths.get("target/videos"))
        );

        // Включаем tracing ДО создания страницы
        context.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

        page = context.newPage();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        contextThreadLocal.set(context);
        pageThreadLocal.set(page);


        getPage().navigate(ConfigReader.get("baseUrl"));
        getPage().navigate(ConfigReader.get("baseUrl1"));

        page.setDefaultTimeout(ConfigReader.getInt("timeout"));

    }

        public static Page getPageStatic() {
        return pageThreadLocal.get();
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {

        boolean testFailed = testInfo.getTags().contains("failed");

        try {
            // Если тест упал — прикладываем доказательства
            if (testFailed) {

                // Screenshot
                byte[] screenshot = page.screenshot(
                        new Page.ScreenshotOptions().setFullPage(true)
                );
                Allure.addAttachment(
                        "Screenshot",
                        new ByteArrayInputStream(screenshot)
                );

                // Trace
                Path traceDir = Paths.get("target/traces");
                Files.createDirectories(traceDir);
                Path tracePath = traceDir.resolve(testInfo.getDisplayName() + ".zip");

                context.tracing().stop(
                        new Tracing.StopOptions().setPath(tracePath)
                );

                Allure.addAttachment(
                        "Trace",
                        Files.newInputStream(tracePath)
                );
            } else {
                // Если тест успешный — просто останавливаем tracing
                context.tracing().stop();
            }

        } catch (Exception ignored) {

        }

        // Закрываем строго в правильном порядке
                
        if (context != null)getPage().context().close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
        pageThreadLocal.remove();
        contextThreadLocal.remove();
    }

    }

