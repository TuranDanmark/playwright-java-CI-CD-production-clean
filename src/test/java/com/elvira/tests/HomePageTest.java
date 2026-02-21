package com.elvira.tests;

import com.elvira.core.base.BaseTest;
import com.elvira.pages.HomePage;
import com.elvira.utils.TestListener;

import io.qameta.allure.*;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@ExtendWith(TestListener.class)
@Epic("HomePage App")
@Feature("HomePage Management")
public class HomePageTest extends BaseTest {

    @Tag("regression")
    @Test
    @Story("User can click Get started")
    @Description("Verify that user can asserthat Get started")
    @Severity(SeverityLevel.CRITICAL)
    void UserCanOpenDocsPage() {

        HomePage homePage = new HomePage(getPage());

        homePage.navigate();
        homePage.clickGetStarted();

    assertThat(page.locator("text=Get started")).isVisible();

}

}