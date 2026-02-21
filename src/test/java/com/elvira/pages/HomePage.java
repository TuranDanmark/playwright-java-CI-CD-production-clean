package com.elvira.pages;

import com.elvira.core.config.FrameworkConfig;
import com.microsoft.playwright.Page;

public class HomePage {

    private final Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    public void navigate() {
        page.navigate(FrameworkConfig.baseUrl1());
    }

    public void clickGetStarted() {
        page.locator("text=Get started").click();
    }

    public String getUrl() {
        return page.url();
    }

    public boolean isGetStartedVisible() {
    return page.locator("text=Get started").isVisible();
}

}