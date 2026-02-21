package com.elvira.core.browser;

import com.elvira.core.config.ConfigReader;
import com.microsoft.playwright.*;
import com.microsoft.playwright.BrowserType;

public class BrowserFactory {

    public static Browser createBrowser(Playwright playwright) {

        String browserName = ConfigReader.get("browser");
        boolean headless = ConfigReader.getBoolean("headless");

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless);

        return switch (browserName.toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> playwright.chromium().launch(options);
        };
    }
}
